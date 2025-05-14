package br.com.imageliteapi.dtos;

import java.io.File;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
@Builder
public class ImageDTO {

	
	private String url;
	private String name;
	private String extension;
	private Long size;
	private File file;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate uploadDate;
	
}
