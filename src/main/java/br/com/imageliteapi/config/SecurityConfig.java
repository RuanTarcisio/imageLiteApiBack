package br.com.imageliteapi.config;

import br.com.imageliteapi.security.JwtAuthenticationEntryPoint;
import br.com.imageliteapi.security.JwtFilter;
import br.com.imageliteapi.security.Oauth2LoginSuccessHandler;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/v1/auth/**",
                            "/v1/oauth2/authorization/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/v1/images/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/v1/images").authenticated();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/v1/oauth2/authorization")
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*")
                        )
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            log.error("OAuth2 Authentication failed", exception);
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(
                                    String.format("{\"error\": \"OAuth2 Authentication failed\", \"message\": \"%s\"}",
                                            exception.getMessage())
                            );
                        })
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // substitua pelo seu domínio em produção
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.simpleDateFormat("dd/MM/yyyy")
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
