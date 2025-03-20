package br.com.imageliteapi.service;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.enums.ImageExtension;
import br.com.imageliteapi.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository repository;

    @Transactional
    public Image save(Image image) {
        return repository.save(image);
    }

    public Optional<Image> getById(String id) {
        //
        return repository.findById(id);
    }

    public List<Image> search(ImageExtension extension, String query) {
        return repository.findByExtensionAndNameOrTagsLike(extension, query);
    }
}
