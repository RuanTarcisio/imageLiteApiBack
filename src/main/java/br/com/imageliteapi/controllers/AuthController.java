package br.com.imageliteapi.controllers;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.dtos.CredentialsDTO;
import br.com.imageliteapi.dtos.inputs.InputUserRegister;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.service.AuthService;
import br.com.imageliteapi.service.UserService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;
import br.com.imageliteapi.utils.AuthHandlerUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final AuthHandlerUtil authHandlerUtil;


    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@ModelAttribute @Valid InputUserRegister dto) {
        try {
            User user = userMapper.inputToUser(dto);
            userService.save(dto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (DuplicatedTupleException e) {
            Map<String, String> jsonResultado = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResultado);
        }
    }

@GetMapping("/check-session")
public ResponseEntity<?> checkSession(@AuthenticationPrincipal User user, HttpServletResponse response) {
    if (user == null) {
        var expiredCookie = authHandlerUtil.expiredSession();
        response.addHeader("Set-Cookie", expiredCookie);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Map<String, Object> object = new HashMap<>();
    object.put("id", user.getId());
    object.put("email", user.getEmail());
    object.put("name", user.getName());
    object.put("profileImage", user.getProfileImageUrl());

    return ResponseEntity.ok(object);
}

//    @GetMapping("/check-session")
//    public ResponseEntity<?> checkSession(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        // Obter detalhes do usuário autenticado
//        String email = authentication.getName();
//        User user = userService.getByEmail(email);
//
//        // Retornar dados básicos do usuário
//        Map<String, Object> response = new HashMap<>();
//        response.put("id", user.getId());
//        response.put("email", user.getEmail());
//        response.put("name", user.getName());
//        response.put("profileImage", user.getProfileImageUrl());
//
//        return ResponseEntity.ok(response);
//    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @RequestBody CredentialsDTO credentials,
            HttpServletResponse response) throws IOException {

        boolean authenticated = authService.authenticate(credentials.email(), credentials.password(), response);

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        return ResponseEntity.ok().build(); // status 200, cookie já foi setado
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Invalida o cookie AUTH_TOKEN
        var expiredCookie = authHandlerUtil.expiredSession();
        response.addHeader("Set-Cookie", expiredCookie);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }
}
