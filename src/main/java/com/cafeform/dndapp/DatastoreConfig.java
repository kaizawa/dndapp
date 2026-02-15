package com.cafeform.dndapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

@Configuration
public class DatastoreConfig {

	private static final Logger log = LoggerFactory.getLogger(DatastoreConfig.class);

	@Bean
	public Datastore datastore() {
		String emulatorHost = System.getenv("DATASTORE_EMULATOR_HOST");
		if (emulatorHost != null && !emulatorHost.trim().isEmpty()) {
			// Emulator: use explicit project (e.g. GOOGLE_CLOUD_PROJECT or default for local).
			String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
			if (projectId == null || projectId.isEmpty()) {
				projectId = "dndapp";
			}
			log.info("Datastore: using emulator at {} (project {})", emulatorHost.trim(), projectId);
			return DatastoreOptions.newBuilder()
					.setProjectId(projectId)
					.setCredentials(NoCredentials.getInstance())
					.build()
					.getService();
		}
		// Production (e.g. App Engine): use default project so GAE/Cloud env is used.
		String projectId = ServiceOptions.getDefaultProjectId();
		if (projectId == null || projectId.isEmpty()) {
			projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
			if (projectId == null || projectId.isEmpty()) {
				projectId = "dndapp";
			}
		}
		log.info("Datastore: using Application Default Credentials (project {})", projectId);
		return DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
	}
}
