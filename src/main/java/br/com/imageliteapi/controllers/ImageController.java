package br.com.imageliteapi.controllers;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.imageliteapi.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.dtos.ImageDTO;
import br.com.imageliteapi.domain.enums.ImageExtension;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static br.com.imageliteapi.mapper.ImageMapper.*;

@RestController
@RequestMapping("v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImageController {

	private final ImageService service;

	@PostMapping
	public ResponseEntity<?> save(@RequestParam("file") MultipartFile file,
								  @RequestParam("name") String name,
								  @RequestParam("tags") List<String> tags) throws IOException {

		log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

		Image image = mapToImage(file, name, tags);
		service.save(image);  // Salva a imagem

		// Gera o URI para o recurso da imagem criada
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(image.getId())
				.toUri();
		System.out.println("Imagem URI: " + uri.toString());  // Verifique no console se a URI está correta

		return ResponseEntity.created(uri).body(uri.toString());  // Retorna a URI no corpo da resposta
	}


	@GetMapping
	public ResponseEntity<List<ImageDTO>> search(
			@RequestParam(value = "extension", required = false, defaultValue = "") String extension,
			@RequestParam(value = "query", required = false) String query) throws InterruptedException {

		Thread.sleep(3000L);

		var result = service.search(ImageExtension.ofName(extension), query);

		var images = result.stream().map(image -> {
			var url = buildImageURL(image);
			return imageToDTO(image, url.toString());
		}).collect(Collectors.toList());

		return ResponseEntity.ok(images);
	}

	@GetMapping(value = "/{id}")
	@Transactional
	public ResponseEntity<byte[]> find(@PathVariable String id) {
		Optional<Image> possibleImage = service.getById(id);
		if (possibleImage.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Image image = possibleImage.get();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(image.getExtension().getMediaType());
		headers.setContentLength(image.getSize());
		headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

		try {
			byte[] fileBytes = readBlobToBytes(image.getFile());
			return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	private URI buildImageURL(Image image) {
		String imagePath = "/" + image.getId();
		return ServletUriComponentsBuilder.fromCurrentRequest().path(imagePath).build().toUri();

	}
}
