package br.com.imageliteapi.resource;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.domain.dto.CredentialsDTO;
import br.com.imageliteapi.domain.dto.UserDTO;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.security.AccessToken;
import br.com.imageliteapi.service.UserService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody UserDTO dto) {
		try {
			User user = userMapper.mapToUser(dto);
			userService.save(user);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
					.toUri();
			return ResponseEntity.created(uri).build();
		} catch (DuplicatedTupleException e) {
			Map<String, String> jsonResultado = Map.of("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResultado);
		}
	}
	
	  @PostMapping("/auth")
	    public ResponseEntity autheticate(@RequestBody CredentialsDTO credentials){
	        var token = userService.authenticate(credentials.email(), credentials.password());

	        if(token == null){
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        return ResponseEntity.ok(token);
	    }
	}
