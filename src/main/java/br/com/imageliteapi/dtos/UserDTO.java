package br.com.imageliteapi.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;


import java.time.LocalDate;

public record UserDTO(
        @Schema(description = "Nome completo", example = "Ruan Tarcísio")
        String name,
        @Schema(description = "E-mail do usuário", example = "ruan@example.com")
        String email,
        @Schema(description = "CPF usuário", example = "12343212311")
        String cpf, @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthdate) {

}
