package br.com.imageliteapi.repository;


import static br.com.imageliteapi.repository.specs.ImageSpecs.extensionEqual;
import static org.springframework.data.jpa.domain.Specification.anyOf;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.enums.ImageExtension;
import br.com.imageliteapi.repository.specs.GenericSpecs;
import br.com.imageliteapi.repository.specs.ImageSpecs;

public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

//SELECT * FROM IMAGE WHERE 1 = 1 AND EXTENSION = 'PNG' AND 
//			( NAME LIKE 'QUERY' OR TAG LIKE 'QUERY' )

	default List<Image> findByExtensionAndNameOrTagsLike(ImageExtension extension, String query) {
// SELECT * FROM IMAGE WHERE 1 = 1
		 Specification<Image> spec = where(GenericSpecs.conjunction());

	        if(extension != null){
	            spec = spec.and(extensionEqual(extension));
	        }

	        if(StringUtils.hasText(query)){
	            spec = spec.and(anyOf(ImageSpecs.nameLike(query), ImageSpecs.tagsLike(query)));
	        }

	        return findAll(spec);
	}
}
