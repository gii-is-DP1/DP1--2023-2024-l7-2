package org.springframework.samples.dwarf.friendRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendRequestService {

    private FriendRequestRepository friendRequestRepository;
    private GameRepository gr;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, GameRepository gr) {
        this.friendRequestRepository = friendRequestRepository;
        this.gr = gr;
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
        return friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void deleteFriendRequest(Integer id) throws DataAccessException {
        friendRequestRepository.deleteById(id);

    }

    @Transactional(readOnly = true)
    public List<User> getFriends(User u) {
        ArrayList<User> res = new ArrayList<>();
        List<FriendRequest> reqs = friendRequestRepository.findByUser(u);

        for (FriendRequest fr : reqs) {
            if (fr.getStatus().equals(Status.ACCEPTED)) {
                if (fr.getSender().equals(u)) {
                    res.add(fr.getReceiver());
                } else {
                    res.add(fr.getSender());
                }
            }
        }

        return res;
    }

    @Transactional(readOnly = true)
    public List<Game> getAllFriendGames(User u) {
        ArrayList<Game> res = new ArrayList<Game>();
        List<User> friends = getFriends(u);
        
        Set<Game> tempGames = new HashSet<>();
        for (User user:friends) {
            tempGames.addAll(gr.findGamesByUserName(user.getUsername()));
        }
        res.addAll(tempGames);
        return res;
    }

}
