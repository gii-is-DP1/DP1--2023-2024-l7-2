package org.springframework.samples.dwarf.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    ChatRepository chatRepository;
    MessageRepository messageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Transactional
    public Chat getChatById(Integer id) {
        return chatRepository.findById(id).get();
    }

    @Transactional
    public List<Message> getAllMessages(Integer id) {
        return chatRepository.findById(id).get().getMessages();
    }

    @Transactional()
    public Chat initialize() {
        Chat chat = new Chat();
        save(chat);
        return chat;
    }

    public void delete(Integer id) {
        chatRepository.deleteById(id);
    }
}
