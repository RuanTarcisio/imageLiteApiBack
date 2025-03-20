package br.com.imageliteapi.mapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import br.com.imageliteapi.domain.AbstractImage;
import br.com.imageliteapi.domain.ImageUser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.dto.ImageDTO;
import br.com.imageliteapi.domain.enums.ImageExtension;

@Component
public class ImageMapper {

	public static Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException {
		Image image = new Image();
		image.setName(name);
		image.setSize(file.getSize());
		image.setFile(file.getBytes());
		image.setUploadDate(LocalDateTime.now());
		image.setTags(String.join(",", tags));
		image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));

		return image;
	}

	public static ImageDTO imageToDTO(AbstractImage image, String url){
		return ImageDTO.builder()
				.url(url)
				.extension(image.getExtension().name())
				.name(image.getName())
				.size(image.getSize())
				.uploadDate(image.getUploadDate().toLocalDate())
				.build();
	}

	public static ImageUser mapToImage(MultipartFile file) {
		ImageUser image = new ImageUser();

		try {
			image.setName(file.getName());
			image.setSize(file.getSize());
            image.setFile(file.getBytes());
			image.setTags("image_User");
			image.setUploadDate(LocalDateTime.now());
			image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


		return image;
	}

//	public static Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException {
//		Image image = new Image();
//		image.setName(name);
//		image.setSize(file.getSize());
//		image.setFile(file.getBytes());
//		image.setUploadDate(LocalDateTime.now());
//		image.setTags(String.join(",", tags));
//		image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));
//
//		return image;
//	}
//
//
//	}
//
//	public static ImageDto imageToDTO(Image image, String url){
//		return ImageDto.builder()
//				.url(url)
//				.extension(image.getExtension().name())
//				.name(image.getName())
//				.size(image.getSize())
//				.uploadDate(image.getUploadDate().toLocalDate())
//				.build();
//	}

}