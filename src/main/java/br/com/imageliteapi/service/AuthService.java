package br.com.imageliteapi.service;

import br.com.imageliteapi.repository.UserRepository;
import br.com.imageliteapi.security.AccessToken;
import br.com.imageliteapi.security.JwtService;
import br.com.imageliteapi.utils.AuthHandlerUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthHandlerUtil authHandlerUtil;

    public boolean authenticate(String email, String password, HttpServletResponse response) throws IOException {

        var user = service.getByEmail(email);

        if (passwordEncoder.matches(password, user.getPassword())) {
            var token=  jwtService.generateToken(user);
            authHandlerUtil.authenticateUser(user, response);
            return true;
        }
        return false;
    }
}
