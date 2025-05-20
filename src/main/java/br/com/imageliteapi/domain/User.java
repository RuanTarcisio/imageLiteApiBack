package br.com.imageliteapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;

    private LocalDate birthdate;

    @Column(name = "created_At")
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy hh:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserConnectedAccount> connectedAccounts = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ImageUser imageUser;

    private String profileImageUrl;

    @JsonIgnore
    @Column(unique = true)
    private String codToken;

    private boolean enabled = false;

    private boolean fullyRegistered = false;

    public User(String name, String cpf, String email, String password,  LocalDate birthdate) {
        this.birthdate = birthdate;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(OAuth2User oAuth2User) {
        User user = new User();
        user.email = oAuth2User.getAttribute("email");
        user.name = oAuth2User.getAttribute("name");
        user.setFullyRegistered(false);
        user.setEnabled(true);
    }

    public void addConnectedAccount(UserConnectedAccount connectedAccount) {
        connectedAccounts.add(connectedAccount);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
