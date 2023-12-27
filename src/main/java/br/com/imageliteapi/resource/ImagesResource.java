package br.com.imageliteapi.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.dto.ImageDto;
import br.com.imageliteapi.domain.enums.ImageExtension;
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

	@CrossOrigin
	@GetMapping
	public ResponseEntity<List<ImageDto>> search(
			@RequestParam(value = "extension", required = false, defaultValue = "") String extension,
			@RequestParam(value = "query", required = false) String query) throws InterruptedException {

		Thread.sleep(3000L);

		var result = service.search(ImageExtension.ofName(extension), query);

		var images = result.stream().map(image -> {
			var url = buildImageURL(image);
			return imageMapper.imageToDTO(image, url.toString());
		}).collect(Collectors.toList());

		return ResponseEntity.ok(images);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<byte[]> find(@PathVariable String id) {
		var possibleImage = service.getById(id);
		if (possibleImage.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		var image = possibleImage.get();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(image.getExtension().getMediaType());
		headers.setContentLength(image.getSize());
		headers.setContentDispositionFormData("inline; filename = \"" + image.getFileName() + "\"",
				image.getFileName());

		return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
	}

	private URI buildImageURL(Image image) {
		String imagePath = "/" + image.getId();
		return ServletUriComponentsBuilder.fromCurrentRequest().path(imagePath).build().toUri();

	}
}
