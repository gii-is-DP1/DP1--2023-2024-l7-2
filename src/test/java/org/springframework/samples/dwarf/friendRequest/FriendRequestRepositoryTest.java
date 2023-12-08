package org.springframework.samples.dwarf.friendRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.dwarf.friendRequest.FriendRequest;
import org.springframework.samples.dwarf.friendRequest.FriendRequestRepository;
import org.springframework.samples.dwarf.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FriendRequestRepositoryTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    public void testFindAll() {
        // Caso positivo
        List<FriendRequest> friendRequests = new ArrayList<>();
        friendRequests.add(new FriendRequest());
        Mockito.when(friendRequestRepository.findAll()).thenReturn(friendRequests);

        Assertions.assertEquals(friendRequests, friendRequestService.findAll());

        // Caso negativo
        Mockito.when(friendRequestRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertTrue(friendRequestService.findAll().isEmpty());
    }

    @Test
    public void testFindById() {
        // Caso positivo
        int requestId = 1;
        FriendRequest friendRequest = new FriendRequest();
        Mockito.when(friendRequestRepository.findById(requestId)).thenReturn(friendRequest);
    
        Assertions.assertEquals(friendRequest, friendRequestService.findById(requestId));
    
        // Caso negativo
        Mockito.when(friendRequestRepository.findById(requestId)).thenReturn(null);
    
        Assertions.assertNull(friendRequestService.findById(requestId));
    }

    @Test
    public void testFindByUser() {
        // Caso positivo
        User user = new User();
        List<FriendRequest> friendRequests = new ArrayList<>();
        friendRequests.add(new FriendRequest());
        Mockito.when(friendRequestRepository.findByUser(user)).thenReturn(friendRequests);

        Assertions.assertEquals(friendRequests, friendRequestService.findByUser(user));

        // Caso negativo
        Mockito.when(friendRequestRepository.findByUser(user)).thenReturn(new ArrayList<>());

        Assertions.assertTrue(friendRequestService.findByUser(user).isEmpty());
    }
}
