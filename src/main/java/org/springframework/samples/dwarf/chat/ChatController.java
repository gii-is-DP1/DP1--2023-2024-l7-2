package org.springframework.samples.dwarf.chat;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chats", description = "The chats management API")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // @GetMapping
    // public List<Chat> getAllChats() {
    //     return chatService.getAllChats();
    // }

    // @GetMapping("/{id}")
    // public Chat getChatById(@PathVariable("id") Integer id) {
    //     return chatService.getChatById(id);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Void> updateChat(@Valid @RequestBody Chat g, @PathVariable("id") Integer id) {
    //     Chat chatToUpdate = getChatById(id);
    //     BeanUtils.copyProperties(g, chatToUpdate, "id", "playerCreator", "playerStart");
    //     chatService.save(chatToUpdate);
    //     return ResponseEntity.noContent().build();
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteChat(@PathVariable("id") Integer id) {
    //     if (getChatById(id) != null)
    //         chatService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

}
