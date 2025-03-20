package br.com.imageliteapi.resource;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.domain.dto.CredentialsDTO;
import br.com.imageliteapi.domain.dto.inputs.InputUserRegister;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.service.UserService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;

	@PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> save(@ModelAttribute @Valid InputUserRegister dto) {
		try {
			User user = userMapper.inputToUser(dto);
			userService.save(dto);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
					.toUri();
			return ResponseEntity.created(uri).build();
		} catch (DuplicatedTupleException e) {
			Map<String, String> jsonResultado = Map.of("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResultado);
		}
	}


	@PostMapping("/signin")
	    public ResponseEntity authenticate(@RequestBody CredentialsDTO credentials){
	        var token = userService.authenticate(credentials.email(), credentials.password());

	        if(token == null){
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        return ResponseEntity.ok(token);
	    }
	}
