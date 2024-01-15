package org.springframework.samples.dwarf.friendRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserRepository;
import org.springframework.samples.dwarf.user.UserService;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @InjectMocks
    private UserService userService;

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

    @Test
    public void testSaveFriendRequest() {
        // Caso positivo
        FriendRequest friendRequest = new FriendRequest();
        Mockito.when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);

        Assertions.assertEquals(friendRequest, friendRequestService.saveFriendRequest(friendRequest));
    }

    @Test
    public void testDeleteFriendRequest() {
        // Caso positivo
        Integer requestId = 1;
        Assertions.assertDoesNotThrow(() -> friendRequestService.deleteFriendRequest(requestId));

    }

    @Test
    public void testGetFriends() {

        // Set up
        MockitoAnnotations.openMocks(this);

        User f1 = new User();
        f1.setUsername("friend1");
        f1.setIsLoggedIn(true);
        User f2 = new User();
        f2.setUsername("friend2");
        f2.setIsLoggedIn(true);

        FriendRequest fr = new FriendRequest();
        fr.setReceiver(f1);
        fr.setSender(f2);
        fr.setStatus(Status.ACCEPTED);

        // Mock the behavior of the friendRequestRepository
        when(friendRequestRepository.findByUser(f1)).thenReturn(Arrays.asList(fr));

        // Call the function being tested
        List<User> friends = friendRequestService.getFriends(f1);

        // Verify the result
        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(f2));

        // Verify interactions with the mock
        verify(friendRequestRepository, times(1)).findByUser(f1);
    }

}
