package br.com.imageliteapi.resource;

import br.com.imageliteapi.domain.dto.UserDTO;
import br.com.imageliteapi.domain.dto.inputs.InputUserUpdate;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody  InputUserUpdate inputUserUpdate) {
        return ResponseEntity.ok().body(userService.updateUser(id, inputUserUpdate));
    }

    @GetMapping(value = "/profile/{id}/photo")
    public ResponseEntity<byte[]> find(@PathVariable Long id) {
        var possibleImage = userService.getImageByUserId(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename = \"" + image.getFileName() + "\"",
                image.getFileName());

        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

}
