package org.springframework.samples.dwarf.friendRequest;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;



@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest,Integer> {

	public List<FriendRequest> findAll();	
    
	public FriendRequest findById(int id);

	@Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :u OR fr.receiver = :u")
	public List<FriendRequest> findByUser(User u);
}
