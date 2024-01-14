package org.springframework.samples.dwarf.friendRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.dwarf.user.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testAreFriends_Positive() {
        User friend1 = new User();
        friend1.setUsername("f1");
        User friend2 = new User();
        friend2.setUsername("f2");

        FriendRequest fr = new FriendRequest();
        fr.setReceiver(friend1);
        fr.setSender(friend2);
        fr.setStatus(Status.ACCEPTED);

        // Configuramos el mock para que devuelva un resultado positivo
        when(friendRequestRepository.areFriends(friend1, friend2)).thenReturn(true);

        // Llamamos al servicio
        boolean result = friendRequestRepository.areFriends(friend1, friend2);

        // Verificamos que el método del repositorio fue invocado con los parámetros
        // correctos
        verify(friendRequestRepository, times(1)).areFriends(friend1, friend2);

        // Verificamos que el resultado es positivo
        assertTrue(result);
    }

    @Test
    public void testAreFriends_Negative() {
        User friend1 = new User();
        friend1.setUsername("f1");
        User friend2 = new User();
        friend2.setUsername("f2");

        // Configuramos el mock para que devuelva un resultado negativo
        when(friendRequestRepository.areFriends(friend1, friend2)).thenReturn(false);

        // Llamamos al servicio
        boolean result = friendRequestRepository.areFriends(friend1, friend2);

        // Verificamos que el método del repositorio fue invocado con los parámetros
        // correctos
        verify(friendRequestRepository, times(1)).areFriends(friend1, friend2);

        // Verificamos que el resultado es negativo
        assertFalse(result);
    }

}
