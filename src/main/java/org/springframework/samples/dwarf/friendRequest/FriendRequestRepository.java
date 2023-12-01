package org.springframework.samples.dwarf.friendRequest;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;



@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest,Integer> {

	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.id =:id")
    public FriendRequest findById(@Param("id") int id);
	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.userSender.user.username =:userSenderName")
    public Collection<FriendRequest> findByUserSenderName(@Param("userSenderName") String userSenderName);
	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.userReceiver.user.username =:userReceiverName")
    public Collection<FriendRequest> findByUserReceiverName(@Param("userReceiverName") String userReceiverName);
	
	@Query("SELECT friendRequest.userSender FROM FriendRequest friendRequest WHERE friendRequest.userReceiver.user.username =:userReceiverName")
    public Collection<User> findUserSenderByUserReceiverName(@Param("userReceiverName") String userReceiverName);

	@Query("SELECT friendRequest.userReceiver FROM FriendRequest friendRequest WHERE friendRequest.userSender.user.username =:userSenderName")
    public Collection<User> findUserReceiverByUserSenderName(@Param("userSenderName") String userSenderName);
	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.userReceiver =:user or friendRequest.userSender =:user")
    public Collection<FriendRequest> findByUserAllFriendRequest(@Param("user") User user);
	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE (friendRequest.userReceiver =:user or friendRequest.userSender =:user)and friendRequest.status =accepted")
    public Collection<FriendRequest> findByUserAllFriendRequestAccept(@Param("user") User user);

	@Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE (friendRequest.userReceiver.user.username =:userReceiverName or friendRequest.userSender.user.username =:userSenderName) and friendRequest.status =accepted")
    public FriendRequest findByUserReceiverNameAndUserSenderNameAccept(@Param("userReceiverName") String userReceiverName,@Param("userSenderName") String userSenderName);
	
	@Query("SELECT friendRequest FROM FriendRequest friendRequest")
	public List<FriendRequest> findAll();
	
	
	
}
