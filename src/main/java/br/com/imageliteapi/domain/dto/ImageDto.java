package br.com.imageliteapi.domain.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
@Builder
public class ImageDto {

	
	private String url;
	private String name;
	private String extension;
	private Long size;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate uploadDate;
	
}
