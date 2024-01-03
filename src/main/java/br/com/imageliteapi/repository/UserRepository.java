package br.com.imageliteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.imageliteapi.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
