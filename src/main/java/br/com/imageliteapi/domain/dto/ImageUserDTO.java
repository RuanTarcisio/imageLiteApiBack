package br.com.imageliteapi.domain.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public class ImageUserDTO {
    private String name;
    private byte[] file;
    private String extension;
    private Long size;

}
