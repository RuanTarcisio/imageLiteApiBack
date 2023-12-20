package br.com.imageliteapi.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.mapper.ImageMapper;
import br.com.imageliteapi.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImagesResource {

	@Autowired
	private ImageService service;
	@Autowired
	private ImageMapper imageMapper;

	@PostMapping
	public ResponseEntity<?> save(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
			@RequestParam("tags") List<String> tags) throws IOException {

		log.info("Imagem recebid: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

		Image image = imageMapper.mapToImage(file, name, tags);
		service.save(image);
		URI imageUri = buildImageURL(image);

		return ResponseEntity.created(imageUri).build();

	}

	private URI buildImageURL(Image image) {
		String imagePath = "/" + image.getId();
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(imagePath)
				.build()
				.toUri();
		
	}
}
