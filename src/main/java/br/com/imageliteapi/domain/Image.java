package br.com.imageliteapi.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends AbstractImage {

	private String tags;

	@Override
	public String getFileName() {

		return getName().concat(".").concat(getExtension().name());
	}
}
