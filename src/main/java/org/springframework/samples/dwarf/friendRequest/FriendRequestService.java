package org.springframework.samples.dwarf.friendRequest;


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
    public List<FriendRequest> findAll() throws DataAccessException {
        return friendRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FriendRequest findById(int id) throws DataAccessException {
        return friendRequestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> findByUser(User u) {
        return friendRequestRepository.findByUser(u);
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
