package br.com.imageliteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.imageliteapi.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByCpf(String cpf);

	boolean existsByCpf(String cpf);

	boolean existsByEmail(String email);

}
