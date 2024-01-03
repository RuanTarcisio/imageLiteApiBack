package br.com.imageliteapi.service;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.security.AccessToken;

public interface UserService {

	User getByEmail(String email);
	User save(User user);
	AccessToken authenticate(String email, String password);
}
