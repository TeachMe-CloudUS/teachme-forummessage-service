package us.cloud.teachme.forumMessage.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.cloud.teachme.forumMessage.model.ForumMessage;

public interface ForumMessageRepository extends MongoRepository<ForumMessage, String>{
 //   List<ForumMessage> findByCategory(String category);
}
