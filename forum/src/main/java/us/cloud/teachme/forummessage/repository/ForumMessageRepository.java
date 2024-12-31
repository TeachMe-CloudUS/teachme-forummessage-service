package us.cloud.teachme.forummessage.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.cloud.teachme.forummessage.model.ForumMessage;

public interface ForumMessageRepository extends MongoRepository<ForumMessage, String>{
 //   List<ForumMessage> findByCategory(String category);
    List<ForumMessage> findByForumId(String forumId);
    void deleteByForumId(String forumId);
    List<ForumMessage> findByUserId(String userId);
    
}
