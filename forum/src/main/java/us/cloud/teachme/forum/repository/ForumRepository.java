package us.cloud.teachme.forum.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forummessage.model.ForumMessage;

public interface ForumRepository extends MongoRepository<Forum, String>{
    Forum findByCourseId(String courseId);
    Forum findByForumId(String forumId);
    void deleteById(String Id);
    void deleteByCourseId(String courseId);

    
}
