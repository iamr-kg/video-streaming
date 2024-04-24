package com.video.streaming.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors( cors -> cors.configurationSource((request)->{
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));
                    return configuration;
                }))
                .authorizeHttpRequests((authz) -> authz.requestMatchers("/vsa/users/welcome").permitAll())
                .authorizeHttpRequests((authz) -> authz.requestMatchers("/vsa/**").authenticated()).
                oauth2ResourceServer(oAuth -> oAuth.jwt(
                        jwt -> jwt.decoder(jwtDecoder())
        ));
        return http.build();
    }
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder =
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = (jwt)-> {
                OAuth2Error error = new OAuth2Error("invalid_token",
                        "The required audience is missing", null);
                for(String aud:audience.split(",")) {
                    if (jwt.getAudience().contains(aud)) {
                        return OAuth2TokenValidatorResult.success();
                    }
                }
                return OAuth2TokenValidatorResult.failure(error);
            };
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

}
