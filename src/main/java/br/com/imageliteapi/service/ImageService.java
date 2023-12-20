package br.com.imageliteapi.service;

import java.util.Optional;

import br.com.imageliteapi.domain.Image;

public interface ImageService {

	Image save(Image image);
	
	Optional<Image> getById(String id);
		
}
