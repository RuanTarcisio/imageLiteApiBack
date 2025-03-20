package br.com.imageliteapi.domain.dto;


import br.com.imageliteapi.domain.ImageUser;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDate;

public record UserDTO(Long id, String name, String email, String cpf, @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthdate) {

}
