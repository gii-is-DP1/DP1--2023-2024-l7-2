package org.springframework.samples.dwarf.friendRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.friendRequest.FriendRequest;
import org.springframework.samples.dwarf.friendRequest.FriendRequestService;
import org.springframework.samples.dwarf.user.Authorities;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FriendRequestControllerTest {

    @Mock
    private FriendRequestService friendRequestService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendsController friendsController;

    @Test
    public void testFindAll() {
        User mockUser = new User();
        Authorities mockAuthority = new Authorities();
        mockAuthority.setAuthority("ADMIN"); 
        mockUser.setAuthority(mockAuthority);
    
        // Mock the UserService to return the mock user
        when(userService.findCurrentUser()).thenReturn(mockUser);
    
        // Mock the FriendRequestService to return an empty list
        List<FriendRequest> friendRequests = new ArrayList<>();
        when(friendRequestService.findAll()).thenReturn(friendRequests);
    
        
        ResponseEntity<List<FriendRequest>> response = friendsController.findAll();
    
        
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(friendRequests, response.getBody());
    
      
        verify(userService, times(1)).findCurrentUser();
        verify(friendRequestService, times(1)).findAll();
    }
/*
    @Test
    public void testFindFriendRequest() {
        // Caso positivo
        int requestId = 1;
        FriendRequest friendRequest = new FriendRequest();
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(friendRequest);

        ResponseEntity<FriendRequest> response = friendsController.findFriendRequest(requestId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(friendRequest, response.getBody());

        // Caso negativo (solicitud de amistad no encontrada)
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> friendsController.findFriendRequest(requestId));
    }

    @Test
    public void testCreateRequest() {
        // Caso positivo
        FriendRequest friendRequest = new FriendRequest();
        Mockito.when(friendRequestService.saveFriendRequest(friendRequest)).thenReturn(friendRequest);

        ResponseEntity<FriendRequest> response = friendsController.createRequest(friendRequest, Mockito.mock(BindingResult.class));

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(friendRequest, response.getBody());

        // Caso negativo (errores de validación)
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> friendsController.createRequest(friendRequest, bindingResult));
    }

    @Test
    public void testDeleteFriendRequest() {
        // Caso positivo
        int requestId = 1;
        FriendRequest friendRequest = new FriendRequest();
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(friendRequest);

        ResponseEntity<Void> response = friendsController.deleteFriendRequest(requestId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        // Caso negativo (solicitud de amistad no encontrada)
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> friendsController.deleteFriendRequest(requestId));
    }

    @Test
    public void testModifyFriendRequest() {
        // Caso positivo
        int requestId = 1;
        FriendRequest friendRequestToUpdate = new FriendRequest();
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(friendRequestToUpdate);

        ResponseEntity<Void> response = friendsController.modifyFriendRequest(new FriendRequest(), requestId);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Caso negativo (solicitud de amistad no encontrada)
        Mockito.when(friendRequestService.findById(requestId)).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> friendsController.modifyFriendRequest(new FriendRequest(), requestId));
    }*/
}
