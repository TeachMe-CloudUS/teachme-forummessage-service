package us.cloud.teachme.forumMessage.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.cloud.teachme.forumMessage.model.ForumMessage;
import us.cloud.teachme.forumMessage.repository.ForumMessageRepository;
@Service
public class ForumMessageService {
    @Autowired
    private ForumMessageRepository ForumMessageRepository;

    public List<ForumMessage> getAllForumMessages() {
        return ForumMessageRepository.findAll();
    }

    public Optional<ForumMessage> getForumMessageById(String id) {
        return ForumMessageRepository.findById(id);
    }
    /*public List<ForumMessage> getForumMessagesByCategory(String category) {
        return ForumMessageRepository.findByCategory(category);
    }*/

    public ForumMessage createForumMessage(ForumMessage ForumMessage) {
        ForumMessage.setCreationDate(new Date());
        return ForumMessageRepository.save(ForumMessage);
    }

    public ForumMessage updateForumMessage(String id, ForumMessage updatedForumMessage) {
        return ForumMessageRepository.findById(id).map(ForumMessage -> {
            ForumMessage.setContent(updatedForumMessage.getContent());
            ForumMessage.setLastModifDate(new Date());
            return ForumMessageRepository.save(ForumMessage);
        }).orElseThrow(() -> new RuntimeException("ForumMessage not found with id " + id));
    }

    public void deleteForumMessage(String id) {
        if (ForumMessageRepository.existsById(id)) {
            ForumMessageRepository.deleteById(id);
        } else {
            throw new RuntimeException("ForumMessage not found with id " + id);
        }
    }


    
}
