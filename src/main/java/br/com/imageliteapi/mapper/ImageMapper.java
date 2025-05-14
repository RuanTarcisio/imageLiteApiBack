package br.com.imageliteapi.mapper;

import br.com.imageliteapi.domain.AbstractImage;
import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.ImageUser;
import br.com.imageliteapi.domain.enums.ImageExtension;
import br.com.imageliteapi.dtos.ImageDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ImageMapper {

    public static Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException {
        Image image = new Image();
        image.setName(name);
        image.setSize(file.getSize());

        try {
            // Converte o MultipartFile para Blob
            Blob blob = new SerialBlob(file.getBytes());
            image.setFile(blob); // Define o Blob na entidade
        } catch (SQLException e) {
            throw new IOException("Erro ao converter arquivo para Blob", e);
        }

        image.setUploadDate(LocalDateTime.now());
        image.setTags(String.join(",", tags));
        image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));

        return image;
    }

    public static ImageUser mapToImage(MultipartFile file) {
        ImageUser image = new ImageUser();

        try {
            image.setName(file.getOriginalFilename()); // Use getOriginalFilename() para o nome do arquivo
            image.setSize(file.getSize());
            image.setFile(new SerialBlob(file.getBytes())); // Converte para Blob
            image.setTags("image_User");
            image.setUploadDate(LocalDateTime.now());
            image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Erro ao converter arquivo para Blob", e);
        }

        return image;
    }

    public static ImageDTO imageToDTO(AbstractImage image, String url) {
        return ImageDTO.builder()
                .url(url)
                .extension(image.getExtension().name())
                .name(image.getName())
                .size(image.getSize())
                .uploadDate(image.getUploadDate().toLocalDate())
                .build();
    }

//    public static byte[] readStringToBlob(Blob blob) throws SQLException {
//        String texto = "Este é um exemplo de string que será convertida para Blob.";
//
//        try {
//            // Passo 1: Converter a String para byte[]
//            byte[] bytes = texto.getBytes();
//
//            // Passo 2: Criar um Blob a partir do byte[]
//            Blob blob = new SerialBlob(bytes);
//
//            // Exibir informações sobre o Blob
//            System.out.println("Blob criado com sucesso!");
//            System.out.println("Tamanho do Blob: " + blob.length());
//            System.out.println("Conteúdo do Blob: " + blob.getBytes(1, (int) blob.length()));
//        } catch (SQLException e) {
//            System.err.println("Erro ao criar Blob: " + e.getMessage());
//        }
//        return blob;
//    }
        public static byte[] readBlobToBytes (Blob blob) throws SQLException {
            if (blob == null) {
                return new byte[0];
            }
            return blob.getBytes(1, (int) blob.length());
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