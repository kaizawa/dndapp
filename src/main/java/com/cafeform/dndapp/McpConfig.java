package com.cafeform.dndapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.mcp.server.autoconfigure.McpServerProperties;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider charaTools(CharaMcpService charaMcpService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(charaMcpService)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "true")
    public KeepaliveWebMvcSseServerTransportProvider webMvcSseServerTransportProvider(
            ObjectProvider<ObjectMapper> objectMapperProvider, McpServerProperties serverProperties) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
        return new KeepaliveWebMvcSseServerTransportProvider(
                objectMapper,
                serverProperties.getBaseUrl(),
                serverProperties.getSseMessageEndpoint(),
                serverProperties.getSseEndpoint());
    }

    @Bean
    @ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "true")
    public RouterFunction<ServerResponse> mvcMcpRouterFunction(
            KeepaliveWebMvcSseServerTransportProvider transportProvider) {
        return transportProvider.getRouterFunction();
    }
}
