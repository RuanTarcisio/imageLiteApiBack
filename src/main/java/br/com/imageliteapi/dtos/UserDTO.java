package br.com.imageliteapi.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDate;

public record UserDTO(Long id, String name, String email, String cpf, @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthdate) {

}
