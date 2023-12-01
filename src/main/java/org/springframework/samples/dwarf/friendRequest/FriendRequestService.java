package org.springframework.samples.dwarf.friendRequest;


import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class FriendRequestService{

	
	private FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }


    @Transactional(readOnly = true)
    public FriendRequest findByRequestById(int id) throws DataAccessException {
        return friendRequestRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Collection<FriendRequest> findByRequestByUserReceiverName(String username) throws DataAccessException {
        return friendRequestRepository.findByUserReceiverName(username);
    }
    
    @Transactional(readOnly = true)
    public Collection<User> findByRequestUserSenderByUserReceiverName(String username) throws DataAccessException {
        return friendRequestRepository.findUserSenderByUserReceiverName(username);
    }
    
    @Transactional(readOnly = true)
    public Collection<User> findByRequestUserReceiverByUserSenderName(String username) throws DataAccessException {
        return friendRequestRepository.findUserReceiverByUserSenderName(username);
    }
    
    @Transactional(readOnly = true)
    public Collection<FriendRequest> findByRequestByUserSenderName(String username) throws DataAccessException {
        return friendRequestRepository.findByUserSenderName(username);
    }
    
    @Transactional(readOnly = true)
    public Collection<FriendRequest> findByRequestByUserAllFriendRequest(User user) throws DataAccessException {
        return friendRequestRepository.findByUserAllFriendRequest(user);
    }
    
    @Transactional(readOnly = true)
    public Collection<FriendRequest> findByRequestByUserAllFriendRequestAccept(User user) throws DataAccessException {
        return friendRequestRepository.findByUserAllFriendRequestAccept(user);
    }

    @Transactional(readOnly = true)
    public FriendRequest findByRequestByUserReceiverNameAndUserSenderNameAccept(String userReceiver, String userSender) throws DataAccessException {
        return friendRequestRepository.findByUserReceiverNameAndUserSenderNameAccept(userReceiver, userSender);
    }

    @Transactional(readOnly = true)	
	public List<FriendRequest> findAll() throws DataAccessException {
		return friendRequestRepository.findAll();
	}
    
    
    @Transactional
	public FriendRequest saveFriendRequest(FriendRequest friendRequest) throws DataAccessException {
		//creating friendRequest
    	return friendRequestRepository.save(friendRequest);	
	}	
	
	@Transactional
	public void deleteFriendRequest(Integer id) throws DataAccessException {
		//deleting friendRequest
		friendRequestRepository.deleteById(id);	
		
	}
    

    
}
