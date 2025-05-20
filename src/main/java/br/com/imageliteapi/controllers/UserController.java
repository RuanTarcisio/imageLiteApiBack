package br.com.imageliteapi.controllers;

import br.com.imageliteapi.dtos.UserDTO;
import br.com.imageliteapi.dtos.inputs.InputUserUpdate;
import br.com.imageliteapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static br.com.imageliteapi.mapper.ImageMapper.readBlobToBytes;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e seus perfis")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obter perfil do usuário", description = "Retorna os dados públicos do perfil de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/profile/{id}")
//    @CacheControl(maxAge = 30, maxAgeUnit = TimeUnit.SECONDS)
    public ResponseEntity<UserDTO> getProfile(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @Operation(summary = "Atualizar perfil do usuário", description = "Atualiza os dados de perfil de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserDTO> updateProfile(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(description = "Dados para atualização do usuário", required = true)
            @Valid @RequestBody InputUserUpdate inputUserUpdate) {

        return ResponseEntity.ok().body(userService.updateUser(id, inputUserUpdate));
    }


    @Operation(summary = "Buscar foto de perfil", description = "Retorna a imagem de perfil do usuário em formato binário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Foto encontrada"),
            @ApiResponse(responseCode = "404", description = "Usuário ou imagem não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar a imagem")
    })
    @GetMapping(value = "/profile/{id}/photo")
    @Transactional
    public ResponseEntity<byte[]> find(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {

        var possibleImage = userService.getImageByUserId(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

        try {
            byte[] fileBytes = readBlobToBytes(image.getFile());
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
