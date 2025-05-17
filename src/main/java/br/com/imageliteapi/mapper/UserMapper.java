package br.com.imageliteapi.mapper;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.dtos.UserDTO;
import br.com.imageliteapi.dtos.inputs.InputUserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static User inputToUser(InputUserRegister dto) {

        User user = new User(dto.getName(), dto.getCpf(), dto.getEmail(), dto.getPassword(), dto.getBirthdate());
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public static UserDTO userToDto(User user) {


        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getBirthdate());
    }

}
