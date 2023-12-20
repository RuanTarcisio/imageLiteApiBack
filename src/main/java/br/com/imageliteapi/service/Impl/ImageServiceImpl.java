package br.com.imageliteapi.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.repository.ImageRepository;
import br.com.imageliteapi.service.ImageService;
import jakarta.transaction.Transactional;

@Service

public class ImageServiceImpl implements ImageService{

	@Autowired
	private ImageRepository repository;
	
	@Transactional
	public Image save(Image image) {
		return repository.save(image);
	}

	@Override
	public Optional<Image> getById(String id) {
		// 
		return repository.findById(id);
		
	}
		
}
