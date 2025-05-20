package br.com.imageliteapi.controllers;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.dtos.CredentialsDTO;
import br.com.imageliteapi.dtos.inputs.InputUserRegister;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.service.AuthService;
import br.com.imageliteapi.service.UserService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;
import br.com.imageliteapi.utils.AuthHandlerUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/v1/auth")
@RestController
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final AuthHandlerUtil authHandlerUtil;

    @Operation(summary = "Cadastrar novo usuário", description = "Realiza o cadastro de um novo usuário com envio de dados via formulário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe"),
    })
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @Parameter(description = "Dados do usuário para cadastro", required = true)
            @ModelAttribute @Valid InputUserRegister dto) {
        try {
            User user = userMapper.inputToUser(dto);
            userService.save(dto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (DuplicatedTupleException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Checar sessão ativa", description = "Verifica se a sessão do usuário ainda está ativa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão válida"),
            @ApiResponse(responseCode = "401", description = "Sessão expirada ou inválida"),
    })
    @GetMapping("/check-session")
    @Cacheable(value = "userSession", key = "#principal.id")
    public ResponseEntity<?> checkSession(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            HttpServletResponse response) {
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

    @Operation(summary = "Autenticar usuário", description = "Realiza o login do usuário e seta o cookie de sessão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @RequestBody CredentialsDTO credentials,
            HttpServletResponse response) throws IOException {

        boolean authenticated = authService.authenticate(credentials.email(), credentials.password(), response);

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Logout do usuário", description = "Encerra a sessão do usuário e remove o cookie de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        var expiredCookie = authHandlerUtil.expiredSession();
        response.addHeader("Set-Cookie", expiredCookie);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
