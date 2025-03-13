package br.com.imageliteapi.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
	@Column(name = "created_At")
	@CreatedDate
	@JsonFormat(pattern = "dd/MM/yyyy hh:ss" )
	private LocalDateTime createdAt;
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<UserConnectedAccount> connectedAccounts = new ArrayList<>();

	public User(OAuth2User oAuth2User) {
		User user = new User();
		user.email = oAuth2User.getAttribute("email");
		user.name = oAuth2User.getAttribute("name");

//		if (name != null) {
//			List<String> names = List.of(name.split(" "));
//			if (names.size() > 1) {
//				user.firstName = names.get(0);
//				user.lastName = names.get(1);
//			} else {
//				user.firstName = names.get(0);
//			}
//		}
//		user.verified = true;
//		user.role = Role.USER;
	}

	public void addConnectedAccount(UserConnectedAccount connectedAccount) {
		connectedAccounts.add(connectedAccount);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER" ));
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
		return true;
	}
}
