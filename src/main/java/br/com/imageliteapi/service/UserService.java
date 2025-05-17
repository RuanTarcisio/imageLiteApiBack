package br.com.imageliteapi.service;

import br.com.imageliteapi.domain.Image;
import br.com.imageliteapi.domain.ImageUser;
import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.dtos.UserDTO;
import br.com.imageliteapi.dtos.inputs.InputUserRegister;
import br.com.imageliteapi.dtos.inputs.InputUserUpdate;
import br.com.imageliteapi.emailTemplates.MailToUsuario;
import br.com.imageliteapi.mapper.UserMapper;
import br.com.imageliteapi.repository.ConnectedAccountRepository;
import br.com.imageliteapi.repository.ImageUserRepository;
import br.com.imageliteapi.repository.UserRepository;
import br.com.imageliteapi.security.AccessToken;
import br.com.imageliteapi.security.JwtService;
import br.com.imageliteapi.service.validation.exception.DuplicatedTupleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static br.com.imageliteapi.mapper.ImageMapper.mapToImage;
import static br.com.imageliteapi.mapper.UserMapper.inputToUser;
import static br.com.imageliteapi.mapper.UserMapper.userToDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ConnectedAccountRepository connectedAccountRepository;
    private final UserMapper userMapper;
    private final ImageUserRepository imageUserRepository;
    private final EmailSenderService emailSenderService;
    private final MailToUsuario mailTo;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.base-url}")
    private String baseUrl;

    public User getByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);

        if (user.isEmpty()) {
            log.warn("Usuário com e-mail {} não encontrado!", email);
            throw new NoSuchElementException("Usuário não encontrado!");
        }
        return user.get();
    }

    public User getById(Long id) {

        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            log.warn("Usuário com o ID {} não encontrado!", id);
            throw new NoSuchElementException("Usuário não encontrado!");
        }
        return user.get();
    }

    public User getByCpf(String cpf) {
        Optional<User> user = repository.findByCpf(cpf);

        if (user.isEmpty()) {
            log.warn("Usuário com o CPF {} não encontrado!", cpf);
            throw new NoSuchElementException("Usuário não encontrado!");
        }
        return user.get();
    }

    @Transactional
    public User save(InputUserRegister input) {

        var possibleUserByEmail = repository.existsByEmail(input.getEmail());
        var possibleUserByCpf = repository.existsByCpf(input.getCpf());

        if (possibleUserByEmail || possibleUserByCpf) {
            throw new DuplicatedTupleException("User already registered!");
        }
        User user = inputToUser(input);
        encodePassword(user);
        String template = mailTo.ativarUsuario(user);
        emailSenderService.enviarEmail(template, "ATIVACAO_DE_CONTA", user.getEmail());
        user = repository.save(user);

        if (input.getProfileImage() != null && !input.getProfileImage().isEmpty()) {
            ImageUser imageUser = mapToImage(input.getProfileImage());
            imageUser.setUser(user); // Relaciona imagem ao usuário
            user.setImageUser(imageUser);String imageUrl = baseUrl + "/v1/users/profile/photo/" + user.getImageUser().getId();
            user.setProfileImageUrl(imageUrl);
            return user;
        }
        return user;

    }

    public UserDTO getUser(Long id) {
        User user = getById(id);
        return userMapper.userToDto(user);
    }

    private void encodePassword(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }

    public UserDTO updateUser(Long id, InputUserUpdate input) {

        User user = getById(id);
        user.setName(input.name());
        user.setEmail(input.email());
        user.setBirthdate(input.birthdate());

        if (!input.file().isEmpty()) {
            try {
                user.setImageUser(mapToImage(input.file()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid image");
            }
        }
        return userToDto(repository.save(user));
    }

    @Transactional
    public Optional<ImageUser> getImageByUserId(Long id) {

        return imageUserRepository.findByUserId(id);
    }

}
