package br.com.imageliteapi.mapper;

import java.time.LocalDateTime;

import br.com.imageliteapi.domain.dto.inputs.InputUserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.domain.dto.UserDTO;

import static br.com.imageliteapi.mapper.ImageMapper.*;

@Component
@RequiredArgsConstructor
public class UserMapper {


	public static User inputToUser(InputUserRegister dto) {
		User user = new User(dto.name(), dto.cpf(), dto.email(), dto.password(), dto.birthday());
		user.setCreatedAt(LocalDateTime.now());

		return user;
	}

	public static UserDTO userToDto(User user)  {

		return new UserDTO(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getCpf(),
				user.getBirthdate());
	}

}
