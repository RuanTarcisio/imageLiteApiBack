package br.com.imageliteapi.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record InputUserRegister(@NotBlank String name, @NotBlank String email, @NotBlank String password,
                                @CPF String cpf, @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthday, MultipartFile file) {
}
