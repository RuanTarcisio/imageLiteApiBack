package br.com.imageliteapi.controllers;

import br.com.imageliteapi.dtos.UserDTO;
import br.com.imageliteapi.dtos.inputs.InputUserUpdate;
import br.com.imageliteapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static br.com.imageliteapi.mapper.ImageMapper.readBlobToBytes;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody InputUserUpdate inputUserUpdate) {
        return ResponseEntity.ok().body(userService.updateUser(id, inputUserUpdate));
    }

    @GetMapping(value = "/profile/{id}/photo")
    @Transactional
    public ResponseEntity<byte[]> find(@PathVariable Long id) {
        var possibleImage = userService.getImageByUserId(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

        try {
            byte[] fileBytes = readBlobToBytes(image.getFile());
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
