package br.com.imageliteapi.repository;


import br.com.imageliteapi.domain.ImageUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageUserRepository extends JpaRepository<ImageUser, String> {

    Optional<ImageUser> findByUserId(Long userId);

    boolean existsImageUserByUserId(Long id);
}
