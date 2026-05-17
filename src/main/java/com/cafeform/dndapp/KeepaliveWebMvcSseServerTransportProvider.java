package com.cafeform.dndapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerSession;
import io.modelcontextprotocol.spec.McpServerTransport;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * WebMvcSseServerTransportProvider with SSE keepalive and dead-connection detection.
 *
 * Two failure modes handled:
 * 1. Keepalive write fails → session removed immediately so subsequent POSTs get 404
 * 2. sendMessage write blocks on dead connection → 15s timeout prevents indefinite hang
 */
public class KeepaliveWebMvcSseServerTransportProvider implements McpServerTransportProvider {

    private static final Logger logger = LoggerFactory.getLogger(KeepaliveWebMvcSseServerTransportProvider.class);

    private static final String MESSAGE_EVENT_TYPE = "message";
    private static final String ENDPOINT_EVENT_TYPE = "endpoint";
    private static final Duration SSE_WRITE_TIMEOUT = Duration.ofSeconds(15);

    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String messageEndpoint;
    private final String sseEndpoint;
    private final RouterFunction<ServerResponse> routerFunction;
    private final ConcurrentHashMap<String, McpServerSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService keepaliveExecutor = Executors.newScheduledThreadPool(4);
    private volatile boolean isClosing = false;
    private McpServerSession.Factory sessionFactory;

    public KeepaliveWebMvcSseServerTransportProvider(ObjectMapper objectMapper, String baseUrl,
            String messageEndpoint, String sseEndpoint) {
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.messageEndpoint = messageEndpoint;
        this.sseEndpoint = sseEndpoint;
        this.routerFunction = RouterFunctions.route()
                .GET(this.sseEndpoint, this::handleSseConnection)
                .POST(this.messageEndpoint, this::handleMessage)
                .build();
    }

    @Override
    public void setSessionFactory(McpServerSession.Factory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Mono<Void> notifyClients(String method, Object params) {
        if (sessions.isEmpty()) return Mono.empty();
        return Flux.fromIterable(sessions.values())
                .flatMap(session -> session.sendNotification(method, params)
                        .doOnError(e -> logger.error("Failed to notify session: {}", e.getMessage()))
                        .onErrorComplete())
                .then();
    }

    @Override
    public Mono<Void> closeGracefully() {
        return Flux.fromIterable(sessions.values())
                .doFirst(() -> {
                    this.isClosing = true;
                    keepaliveExecutor.shutdown();
                })
                .flatMap(McpServerSession::closeGracefully)
                .then();
    }

    public RouterFunction<ServerResponse> getRouterFunction() {
        return this.routerFunction;
    }

    private ServerResponse handleSseConnection(ServerRequest request) {
        if (this.isClosing) {
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body("Server is shutting down");
        }

        String sessionId = UUID.randomUUID().toString();
        logger.debug("Creating SSE connection for session: {}", sessionId);

        try {
            return ServerResponse.sse(sseBuilder -> {
                ScheduledFuture<?> keepalive = keepaliveExecutor.scheduleAtFixedRate(() -> {
                    try {
                        sseBuilder.event("ping").data("keepalive");
                        logger.trace("Sent keepalive for session {}", sessionId);
                    } catch (Exception e) {
                        // SSE connection is dead — remove session so subsequent POSTs return 404
                        sessions.remove(sessionId);
                        logger.debug("Keepalive failed for session {}, session removed: {}", sessionId, e.getMessage());
                    }
                }, 30, 30, TimeUnit.SECONDS);

                sseBuilder.onComplete(() -> {
                    keepalive.cancel(false);
                    sessions.remove(sessionId);
                    logger.debug("SSE connection completed for session: {}", sessionId);
                });
                sseBuilder.onTimeout(() -> {
                    keepalive.cancel(false);
                    sessions.remove(sessionId);
                    logger.debug("SSE connection timed out for session: {}", sessionId);
                });

                KeepaliveSessionTransport sessionTransport = new KeepaliveSessionTransport(sessionId, sseBuilder);
                McpServerSession session = sessionFactory.create(sessionTransport);
                this.sessions.put(sessionId, session);

                try {
                    sseBuilder.id(sessionId)
                            .event(ENDPOINT_EVENT_TYPE)
                            .data(this.baseUrl + this.messageEndpoint + "?sessionId=" + sessionId);
                } catch (Exception e) {
                    logger.error("Failed to send endpoint event for session {}: {}", sessionId, e.getMessage());
                    sseBuilder.error(e);
                }
            }, Duration.ZERO);
        } catch (Exception e) {
            logger.error("Failed to establish SSE connection for session {}: {}", sessionId, e.getMessage());
            sessions.remove(sessionId);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ServerResponse handleMessage(ServerRequest request) {
        if (this.isClosing) {
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body("Server is shutting down");
        }
        if (request.param("sessionId").isEmpty()) {
            return ServerResponse.badRequest().body(new McpError("Session ID missing in message endpoint"));
        }

        String sessionId = request.param("sessionId").get();
        McpServerSession session = sessions.get(sessionId);
        if (session == null) {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .body(new McpError("Session not found or SSE connection closed: " + sessionId));
        }

        try {
            String body = request.body(String.class);
            McpSchema.JSONRPCMessage message = McpSchema.deserializeJsonRpcMessage(objectMapper, body);
            session.handle(message).block();
            return ServerResponse.ok().build();
        } catch (IllegalArgumentException | IOException e) {
            logger.error("Failed to deserialize message: {}", e.getMessage());
            return ServerResponse.badRequest().body(new McpError("Invalid message format"));
        } catch (Exception e) {
            logger.error("Error handling message for session {}: {}", sessionId, e.getMessage());
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new McpError(e.getMessage()));
        }
    }

    private class KeepaliveSessionTransport implements McpServerTransport {

        private final String sessionId;
        private final ServerResponse.SseBuilder sseBuilder;

        KeepaliveSessionTransport(String sessionId, ServerResponse.SseBuilder sseBuilder) {
            this.sessionId = sessionId;
            this.sseBuilder = sseBuilder;
        }

        @Override
        public Mono<Void> sendMessage(McpSchema.JSONRPCMessage message) {
            // Run SSE write on a separate thread with a timeout to prevent indefinite blocking
            // when the underlying TCP connection is dead (e.g. Cloud Run cut the stream).
            return Mono.<Void>fromRunnable(() -> {
                try {
                    String jsonText = objectMapper.writeValueAsString(message);
                    sseBuilder.id(sessionId).event(MESSAGE_EVENT_TYPE).data(jsonText);
                } catch (Exception e) {
                    sessions.remove(sessionId);
                    throw new RuntimeException("SSE write failed for session " + sessionId + ": " + e.getMessage(), e);
                }
            })
            .subscribeOn(Schedulers.boundedElastic())
            .timeout(SSE_WRITE_TIMEOUT)
            .doOnError(e -> {
                sessions.remove(sessionId);
                logger.warn("SSE write failed/timed out for session {}: {}", sessionId, e.getMessage());
            });
        }

        @Override
        public <T> T unmarshalFrom(Object data, TypeReference<T> typeRef) {
            return objectMapper.convertValue(data, typeRef);
        }

        @Override
        public Mono<Void> closeGracefully() {
            return Mono.fromRunnable(() -> {
                try { sseBuilder.complete(); } catch (Exception ignored) {}
            });
        }

        @Override
        public void close() {
            try { sseBuilder.complete(); } catch (Exception ignored) {}
        }
    }
}
