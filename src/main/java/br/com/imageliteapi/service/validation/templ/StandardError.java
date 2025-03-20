package br.com.imageliteapi.service.validation.templ;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@RequiredArgsConstructor

public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer status;
	private String message;
	private String timeStamp;
	

	public StandardError(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timeStamp = converterParaTimeBrasil();
	}

	public static String converterParaTimeBrasil() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String time = ZonedDateTime.now().format(formatter);
		
		return time;
	}



}

