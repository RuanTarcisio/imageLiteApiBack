package br.com.imageliteapi.dtos;

import lombok.Builder;

@Builder
public class ImageUserDTO {
    private String name;
    private byte[] file;
    private String extension;
    private Long size;

}
