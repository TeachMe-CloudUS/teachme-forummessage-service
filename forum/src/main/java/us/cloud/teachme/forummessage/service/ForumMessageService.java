package us.cloud.teachme.forummessage.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import us.cloud.teachme.forummessage.config.RestTemplateConfig;
import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.repository.ForumMessageRepository;
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

    public void deleteAllForumMessages() {
        ForumMessageRepository.deleteAll();
    }

    public List<ForumMessage> getForumMessagesByForumId(String forumId) {
        return ForumMessageRepository.findByForumId(forumId);
    }

    public void deleteForumMessagesByForumId(String forumId) {
        ForumMessageRepository.deleteByForumId(forumId);
    }

    
   
    
}
    
