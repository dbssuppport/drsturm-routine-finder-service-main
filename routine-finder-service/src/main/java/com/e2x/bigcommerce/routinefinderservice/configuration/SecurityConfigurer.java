package com.e2x.bigcommerce.routinefinderservice.configuration;

import com.e2x.bigcommerce.routinefindermodel.ApiError;
import com.e2x.bigcommerce.routinefindermodel.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                            response.getOutputStream().write(apiErrorToBytes(new ApiError(ErrorCode.UNAUTHORIZED)));
                        }
                )
                .and();

        // permissions on endpoints
        http.authorizeRequests()
                // public endpoints
                .antMatchers("/public/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-apis/**").permitAll()

                // REST endpoints
                .antMatchers(HttpMethod.GET, "/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/load").permitAll()
                .antMatchers(HttpMethod.GET, "/uninstall").permitAll()
                .antMatchers(HttpMethod.GET, "/bc/store/*/customer/*/routine").permitAll()
                .antMatchers(HttpMethod.PUT, "/bc/store/*/customer/*/routine").permitAll()

                // private endpoints
                .anyRequest().authenticated();
    }

    private byte[] apiErrorToBytes(ApiError errorInResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorInResponse);
        return serialized.getBytes(StandardCharsets.UTF_8);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
