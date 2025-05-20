package br.com.imageliteapi.controllers;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.imageliteapi.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Imagens", description = "Endpoints para upload, busca e recuperação de imagens")
public class ImageController {

	private final ImageService service;

	@Operation(summary = "Upload de imagem", description = "Realiza o upload de uma imagem com nome e tags associadas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Imagem salva com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro ao processar a imagem")
	})
	@PostMapping
	public ResponseEntity<?> save(
			@Parameter(description = "Arquivo da imagem", required = true)
			@RequestParam("file") MultipartFile file,

			@Parameter(description = "Nome da imagem", required = true)
			@RequestParam("name") String name,

			@Parameter(description = "Lista de tags para a imagem", required = true)
			@RequestParam("tags") List<String> tags) throws IOException {

		log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

		Image image = mapToImage(file, name, tags);
		service.save(image);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(uri).body(uri.toString());
	}

	@Operation(summary = "Buscar imagens", description = "Busca imagens por extensão e/ou texto")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	})
	@GetMapping
	public ResponseEntity<List<ImageDTO>> search(
			@Parameter(description = "Extensão da imagem (ex: PNG, JPG)", required = false)
			@RequestParam(value = "extension", required = false, defaultValue = "") String extension,

			@Parameter(description = "Termo de busca (nome ou tag)", required = false)
			@RequestParam(value = "query", required = false) String query) throws InterruptedException {

		Thread.sleep(3000L); // Simula latência para testes

		var result = service.search(ImageExtension.ofName(extension), query);

		var images = result.stream().map(image -> {
			var url = buildImageURL(image);
			return imageToDTO(image, url.toString());
		}).collect(Collectors.toList());

		return ResponseEntity.ok(images);
	}

	@Operation(summary = "Recuperar imagem por ID", description = "Retorna o conteúdo binário da imagem")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagem encontrada"),
			@ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
			@ApiResponse(responseCode = "400", description = "Erro ao processar o arquivo")
	})
	@GetMapping(value = "/{id}")
	@Transactional
	public ResponseEntity<byte[]> find(
			@Parameter(description = "ID da imagem") @PathVariable String id) {

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
