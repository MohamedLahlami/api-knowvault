package com.norsys.knowvault.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ApplicationStartupLogger implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    private final Environment environment;

    public ApplicationStartupLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        logApplicationStartup();
    }

    private void logApplicationStartup() {
        try {
            String protocol = "http";
            String serverHost = InetAddress.getLocalHost().getHostAddress();

            log.info("\n----------------------------------------------------------\n" +
                    "Application 'KnowVault' is running! Access URLs:\n" +
                    "Local: \t\t{}://localhost:{}\n" +
                    "External: \t{}://{}:{}\n" +
                    "Profile(s): \t{}\n" +
                    "Database URL: \t{}\n" +
                    "Log Level: \tcom.norsys.knowvault = DEBUG\n" +
                    "Log File: \tlogs/knowvault.log\n" +
                    "----------------------------------------------------------",
                    protocol, serverPort,
                    protocol, serverHost, serverPort,
                    String.join(", ", environment.getActiveProfiles()),
                    environment.getProperty("spring.datasource.url", "Not configured"));

            // Log environment-specific information
            if (environment.getActiveProfiles().length == 0) {
                log.warn("No Spring profiles are active. Using default configuration.");
            }

            // Log key configuration values
            log.info("Key Configuration:");
            log.info("  - Server Port: {}", serverPort);
            log.info("  - Database URL: {}", environment.getProperty("spring.datasource.url"));
            log.info("  - OAuth2 Issuer: {}", environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri"));
            log.info("  - File Upload Directory: uploads/");
            log.info("  - AI Summarizer URL: {}", environment.getProperty("ai.summarizer.base-url"));

        } catch (UnknownHostException e) {
            log.warn("Could not determine host address: {}", e.getMessage());
        }
    }
}
