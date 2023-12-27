package br.com.imageliteapi.service;

import java.util.List;
import java.util.Optional;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.enums.ImageExtension;

public interface ImageService {

	Image save(Image image);
	
	Optional<Image> getById(String id);
	
	List<Image> search(ImageExtension extension, String query);
		
}
