package org.springframework.samples.dwarf.friendsRequest;

import java.util.Collection;

import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "friendRequests")
public class FriendRequest extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "userSender")
    private User userSender;
    
	@ManyToOne
    @JoinColumn(name = "userReceiver")
    private User userReceiver;

    @Column(name="status")
    private Status status;

    @ManyToOne
    private Collection<User> friends;
	
}
