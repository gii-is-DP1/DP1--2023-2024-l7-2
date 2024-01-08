package org.springframework.samples.dwarf.user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.statistic.Achievement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appusers")
public class User extends NamedEntity {

	String profilePicture;

	@Column(unique = true)
	String username;

	String password;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

	@OneToMany
 	private Collection<Achievement> achievements;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}

	public Boolean isLoggedIn;

}
