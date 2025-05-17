package br.com.imageliteapi.security;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.domain.UserConnectedAccount;
import br.com.imageliteapi.repository.ConnectedAccountRepository;
import br.com.imageliteapi.repository.UserRepository;
import br.com.imageliteapi.utils.ApplicationProperties;
import br.com.imageliteapi.utils.AuthHandlerUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ConnectedAccountRepository connectedAccountRepository;
    private final UserRepository userRepository;
    private final AuthHandlerUtil authHandlerUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
            String provider = authenticationToken.getAuthorizedClientRegistrationId();
            String providerId = authenticationToken.getName();
            String email = authenticationToken.getPrincipal().getAttribute("email");

            log.info("OAuth2 Login Success - Provider: {}, ProviderId: {}, Email: {}", provider, providerId, email);

            Optional<UserConnectedAccount> connectedAccount = connectedAccountRepository.findByProviderAndProviderId(provider, providerId);
            if (connectedAccount.isPresent()) {
                log.info("User already connected - UserId: {}", connectedAccount.get().getUser().getId());
                authHandlerUtil.authenticateUser(connectedAccount.get().getUser(), response);
                return;
            }

            User existingUser = userRepository.findByEmail(email).orElse(null);
            if (existingUser != null) {
                log.info("Existing user found - UserId: {}", existingUser.getId());
                UserConnectedAccount newConnectedAccount = new UserConnectedAccount(provider, providerId, existingUser);
                existingUser.addConnectedAccount(newConnectedAccount);
                existingUser = userRepository.save(existingUser);
                connectedAccountRepository.save(newConnectedAccount);
                authHandlerUtil.authenticateUser(existingUser, response);
            } else {
                log.info("Creating new user from OAuth2");
                User newUser = createUserFromOauth2User(authenticationToken);
                authHandlerUtil.authenticateUser(newUser, response);
            }
        } catch (Exception e) {
            log.error("Error during OAuth2 login success handling", e);
            response.sendRedirect("/login?error=true");
        }
    }

    private User createUserFromOauth2User(OAuth2AuthenticationToken authentication) {
        User user = new User(authentication.getPrincipal());
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");String provider = authentication.getAuthorizedClientRegistrationId();
        String providerId = authentication.getName();
        UserConnectedAccount connectedAccount = new UserConnectedAccount(provider, providerId, user);
        user.setName(name);
        user.setEmail(email);
        user.addConnectedAccount(connectedAccount);
        user = userRepository.save(user);
        connectedAccountRepository.save(connectedAccount);
        return user;
    }
}