package us.cloud.teachme.forum.service;

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
import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.repository.ForumRepository;
import us.cloud.teachme.forummessage.model.ForumMessage;
@Service
public class ForumService {
    @Autowired
    private ForumRepository ForumRepository;

    public List<Forum> getAllForums() {
        return ForumRepository.findAll();
    }

    public Optional<Forum> getForumById(String id) {
        return ForumRepository.findById(id);
    }
    

    public Forum createForum(Forum forum) {
        forum.setCreationDate(new Date());
        forum.setLastModifDate(new Date());
        return ForumRepository.save(forum);
    }

    public Forum updateForum(String id, Forum updatedForum) {
        return ForumRepository.findById(id).map(Forum -> {
            Forum.setName(updatedForum.getName());
            Forum.setLastModifDate(new Date());
            return ForumRepository.save(Forum);
        }).orElseThrow(() -> new RuntimeException("Forum not found with id " + id));
    }

    public void deleteForum(String id) {
        if (ForumRepository.existsById(id)) {
            ForumRepository.deleteById(id);
        } else {
            throw new RuntimeException("Forum not found with id " + id);
        }
    }

    public void deleteAllForums() {
        ForumRepository.deleteAll();
    }

    public Forum getForumsByCourseId(String CourseId) {
        return ForumRepository.findByCourseId(CourseId);
    }

    public void deleteForumsByForumId(String Id) {
        ForumRepository.deleteById(Id);
    }

    public Forum getForumsByForumId(String id) {
        return ForumRepository.findByForumId(id);
    }

    public void deleteForumByCourseId(String courseId) {
        ForumRepository.deleteByCourseId(courseId);
    }
    
    

}
    
