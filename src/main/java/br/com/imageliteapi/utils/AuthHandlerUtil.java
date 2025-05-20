package br.com.imageliteapi.utils;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandlerUtil {

    private final JwtService jwtService;
    private final boolean isProduction = false;
    private final ApplicationProperties applicationProperties;

    public void authenticateUser(User user, boolean isSoccial, HttpServletResponse response) throws IOException {
        try {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String token = jwtService.generateToken(user).getAccessToken();

            String cookieHeader = String.format(
                    "AUTH_TOKEN=%s; Path=/; HttpOnly; %s SameSite=Lax; Max-Age=%d",
                    token,
                    applicationProperties.isProduction() ? "Secure;" : "",
                    7 * 24 * 60 * 60
            );
            response.addHeader("Set-Cookie", cookieHeader);

            if(isSoccial)
                response.sendRedirect(applicationProperties.getLoginSuccessUrl());

        } catch (Exception e) {
            log.error("Error during user authentication", e);
            response.sendRedirect("/login?error=auth_failed");
        }
    }

    public void setSpringAuthentication(User user) {
        Collection<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void setAuthCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(isProduction);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1 semana

        // Adiciona SameSite para proteção CSRF
        String cookieHeader = String.format(
                "AUTH_TOKEN=%s; Path=/; HttpOnly; %s SameSite=Lax; Max-Age=%d",
                token,
                isProduction ? "Secure;" : "",
                7 * 24 * 60 * 60
        );
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public void handleAuthError(HttpServletResponse response, Exception e) throws IOException {
        log.error("Authentication error", e);
        response.sendRedirect("/login?error=auth_failed");
    }

    public String expiredSession(){
        return String.format(
                "AUTH_TOKEN=; Path=/; HttpOnly; %s SameSite=Lax; Max-Age=0",
                applicationProperties.isProduction() ? "Secure;" : ""
        );
    }
}