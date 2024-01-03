package br.com.imageliteapi.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.domain.dto.UserDTO;

@Component
public class UserMapper {

	public User mapToUser(UserDTO dto) {
		return User.builder()
				.email(dto.email())
				.name(dto.name())
				.password(dto.password())
				.createdAt(LocalDateTime.now())
				.build();
	}
}
