package br.com.imageliteapi.domain;

import br.com.imageliteapi.domain.enums.ImageExtension;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Data// Define que essa classe NÃO gera uma tabela no banco
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private Long size;

    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column
    @CreatedDate
    private LocalDateTime uploadDate;

    private String tags;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] file;

    public abstract String getFileName();
}