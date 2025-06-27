package com.norsys.knowvault.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("keycloak.admin")
@Configuration
public class KeycloakConfig {

    private String serverUrl;
    private String clientId;
    private String realm;
    private String grantType;
    private String clientSecret;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .grantType(grantType)
            .clientSecret(clientSecret)
            .build();
    }


    public String getServerUrl() {
        return serverUrl;
    }

    public KeycloakConfig setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public KeycloakConfig setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getRealm() {
        return realm;
    }

    public KeycloakConfig setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public String getGrantType() {
        return grantType;
    }

    public KeycloakConfig setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public KeycloakConfig setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }
}
