package org.springframework.samples.dwarf.friendRequest;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;

@Repository

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

	@Query("SELECT fr FROM FriendRequest fr")
	public List<FriendRequest> findAll();

	public FriendRequest findById(int id);

	@Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :u OR fr.receiver = :u")
	public List<FriendRequest> findByUser(User u);

	@Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender = :u1 AND fr.receiver = :u2) OR  (fr.receiver = :u1 AND fr.sender = :u2)")
	public Boolean areFriends(User u1, User u2);
}
