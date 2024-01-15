package org.springframework.samples.dwarf.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/message")
@Tag(name = "Messages", description = "The messages management API")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService ms;
    private final UserService us;

    @Autowired
    public MessageController(MessageService ms, UserService us) {
        this.ms = ms;
        this.us = us;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return ms.getAllMessages();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable("id") Integer id) {
        return ms.getMessageById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Message> createMessage(@Valid @RequestBody String text) {

        User u = us.findCurrentUser();

        Message newMessage = new Message();
        newMessage.setSender(u);
        newMessage.setText(text);

        ms.save(newMessage);

        return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMessage(@Valid @RequestBody String text, @PathVariable("id") Integer id) {
        Message messageToUpdate = getMessageById(id);
        messageToUpdate.setText(text);
        ms.save(messageToUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Integer id) {
        if (getMessageById(id) != null)
            ms.delete(id);
        return ResponseEntity.noContent().build();
    }

}
