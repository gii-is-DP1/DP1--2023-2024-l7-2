package org.springframework.samples.dwarf.friendRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.friendRequest.FriendRequest;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.friendRequest.Status;

import java.time.LocalDateTime;

public class FriendRequestTest {

    @Test
    public void testFriendRequestProperties() {
        // Crear usuarios
        User senderUser = new User();
        User receiverUser = new User();

        // Crear un objeto Status (asegúrate de que exista la clase Status)
        Status status = new Status();

        // Crear una fecha y hora
        LocalDateTime sendTime = LocalDateTime.now();

        // Crear FriendRequest
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(senderUser);
        friendRequest.setReceiver(receiverUser);
        friendRequest.setStatus(status);
        friendRequest.setSendTime(sendTime);

        // Verificar las propiedades
        Assertions.assertEquals(senderUser, friendRequest.getSender());
        Assertions.assertEquals(receiverUser, friendRequest.getReceiver());
        Assertions.assertEquals(status, friendRequest.getStatus());
        Assertions.assertEquals(sendTime, friendRequest.getSendTime());
    }
}
