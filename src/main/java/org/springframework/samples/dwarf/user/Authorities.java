package org.springframework.samples.dwarf.user;

import org.springframework.samples.dwarf.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity{
	 

	// @JoinColum
	// User user;
		 

		@Column(length = 20)
	String authority;

	
	


}

