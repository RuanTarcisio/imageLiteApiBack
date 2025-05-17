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
            image.setFile(blob);
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
            image.setFile(new SerialBlob(file.getBytes()));
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

    public static byte[] readBlobToBytes (Blob blob) throws SQLException {
        if (blob == null) {
            return new byte[0];
        }
        return blob.getBytes(1, (int) blob.length());
    }

}