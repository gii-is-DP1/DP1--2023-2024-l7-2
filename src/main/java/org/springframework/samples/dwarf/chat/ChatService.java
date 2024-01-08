package org.springframework.samples.dwarf.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public void delete(Integer id) {
        chatRepository.deleteById(id);
    }


    @Transactional
    public void saveMessage(Chat c, Message msg) {
        msg = messageRepository.save(msg);

        ArrayList<Message> msgs = new ArrayList<>();
        msgs.addAll(c.getMessages());
        msgs.add(msg);

        c.setMessages(msgs);
        chatRepository.save(c);
    }

}
