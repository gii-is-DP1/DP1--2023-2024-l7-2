package org.springframework.samples.dwarf.friendRequest;

import java.time.LocalDateTime;

import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendRequest extends BaseEntity {

	@ManyToOne
    private User sender;
    
	@ManyToOne
    private User receiver;

    @ManyToOne
    private Status status;

    LocalDateTime sendTime;
}
