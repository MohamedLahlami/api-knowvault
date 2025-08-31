package com.norsys.knowvault.config;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    public static final String CLAIM_KEY = "roles";
    private final String rolePrefix = "ROLE_";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
            extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
            jwt,
            authorities,
            jwt.getClaim(JwtClaimNames.SUB)
        );
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        String authorityClaimName = "realm_access";
        var resourceAccess = new HashMap<>(jwt.getClaim(authorityClaimName));
        Collection<String> resourceRoles;

        resourceRoles = (Collection<String>) resourceAccess.get(CLAIM_KEY);
        return resourceRoles
            .stream()
            .map(role -> new SimpleGrantedAuthority(rolePrefix + role))
            .collect(Collectors.toSet());
    }
}
