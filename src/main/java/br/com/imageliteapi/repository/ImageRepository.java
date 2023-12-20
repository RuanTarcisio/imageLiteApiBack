package br.com.imageliteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.imageliteapi.domain.Image;

public interface ImageRepository extends JpaRepository<Image, String>{

}
