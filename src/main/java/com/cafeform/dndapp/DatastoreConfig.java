package com.cafeform.dndapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

@Configuration
public class DatastoreConfig {

	private static final Logger log = LoggerFactory.getLogger(DatastoreConfig.class);

	@Bean
	public Datastore datastore() {
		// When DATASTORE_EMULATOR_HOST is set, connects to the emulator (any project ID).
		// Otherwise uses default project and credentials (e.g. on App Engine).
		String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
		if (projectId == null || projectId.isEmpty()) {
			projectId = "dndapp";
		}
		String emulatorHost = System.getenv("DATASTORE_EMULATOR_HOST");
		if (emulatorHost != null && !emulatorHost.trim().isEmpty()) {
			log.info("Datastore: using emulator at {} (project {})", emulatorHost.trim(), projectId);
			return DatastoreOptions.newBuilder()
					.setProjectId(projectId)
					.setCredentials(NoCredentials.getInstance())
					.build()
					.getService();
		}
		log.info("Datastore: using Application Default Credentials (project {})", projectId);
		return DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
	}
}
