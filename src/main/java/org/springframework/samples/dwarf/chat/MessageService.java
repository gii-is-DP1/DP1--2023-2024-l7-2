package org.springframework.samples.dwarf.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    ChatRepository chatRepository;
    MessageRepository messageRepository;

    @Autowired
    public MessageService(ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Transactional
    public Message save(Message m) {
        return messageRepository.save(m);
    }

    @Transactional
    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).get();
    }

    @Transactional
    public List<Message> getBySentTimeRange(LocalDateTime a, LocalDateTime b) {
        return messageRepository.findBySentTimeRange(a, b);
    }

    public void delete(Integer id) {
        messageRepository.deleteById(id);
    }
}