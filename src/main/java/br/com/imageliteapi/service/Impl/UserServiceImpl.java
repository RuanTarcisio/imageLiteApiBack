package br.com.imageliteapi.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.repository.UserRepository;
import br.com.imageliteapi.security.AccessToken;
import br.com.imageliteapi.security.JwtService;
import br.com.imageliteapi.service.UserService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;

	@Override
	public User getByEmail(String email) {

		return repository.findByEmail(email).orElseThrow(() -> new RuntimeException(""));
	}

	@Override
	@Transactional
	public User save(User user) {

		var possibleUser = getByEmail(user.getEmail());
		if (possibleUser != null) {
			throw new DuplicatedTupleException("user already exists!");
		}
		encodePassword(user);
		return repository.save(user);
	}

	@Override
	public AccessToken authenticate(String email, String password) {

		var user = getByEmail(email);
		if (user == null) {
			return null;
		}
		if(passwordEncoder.matches(password, user.getPassword())) {
			return jwtService.generateToken(user);
		}
		
		return null;
	}

	private void encodePassword(User user) {
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
	}
}
