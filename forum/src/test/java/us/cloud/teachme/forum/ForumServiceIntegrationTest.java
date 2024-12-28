package us.cloud.teachme.forum;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.repository.ForumRepository;
import us.cloud.teachme.forum.service.ForumService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ForumServiceIntegrationTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private ForumRepository forumRepository;

    @Test
    void testCreateForum() {
        Forum forum = new Forum( "course1", "Integration Test Forum", new Date(), new Date());

        Forum savedForum = forumService.createForum(forum);

        assertNotNull(savedForum.getId());
        assertEquals("Integration Test Forum", savedForum.getName());
        assertEquals("course1", savedForum.getCourseId());
    }

    @Test
    void testGetAllForums() {
        Integer numberOfForums = forumService.getAllForums().size();
        Forum forum1 = new Forum( "course1", "Forum 1", new Date(), new Date());
        Forum forum2 = new Forum( "course2", "Forum 2", new Date(), new Date());
        forumRepository.save(forum1);
        forumRepository.save(forum2);

        List<Forum> forums = forumService.getAllForums();

        Integer expectedForums = numberOfForums + 2;
        assertEquals(expectedForums, forums.size());
    }

    @Test
    void testGetForumById() {
        Forum forum = new Forum( "course1", "Test Forum", new Date(), new Date());
        Forum savedForum = forumRepository.save(forum);

        Optional<Forum> retrievedForum = forumService.getForumById(savedForum.getId());

        assertTrue(retrievedForum.isPresent());
        assertEquals("Test Forum", retrievedForum.get().getName());
    }

    @Test
    void testUpdateForum() {
        Forum forum = new Forum( "course1", "Old Forum", new Date(), new Date());
        Forum savedForum = forumRepository.save(forum);

        Forum updatedForum = new Forum( "course1", "Updated Forum", null, null);
        Forum result = forumService.updateForum(savedForum.getId(), updatedForum);

        assertEquals("Updated Forum", result.getName());
    }

    @Test
    void testDeleteForum() {
        Forum forum = new Forum( "course1", "Forum to delete", new Date(), new Date());
        Forum savedForum = forumRepository.save(forum);

        forumService.deleteForum(savedForum.getId());

        assertFalse(forumRepository.existsById(savedForum.getId()));
    }

    @Test
    void testGetForumByIdNotFound() {
        Optional<Forum> result = forumService.getForumById("non-existent-id");

        assertFalse(result.isPresent(), "El foro no debería existir");
    }

    @Test
    void testUpdateForumNotFound() {
        Forum updatedForum = new Forum("course1", "Updated Forum", null, null);

        Exception exception = assertThrows(RuntimeException.class, 
            () -> forumService.updateForum("non-existent-id", updatedForum));

        assertEquals("Forum not found with id non-existent-id", exception.getMessage());
    }

    @Test
    void testDeleteForumNotFound() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> forumService.deleteForum("non-existent-id"));

        assertEquals("Forum not found with id non-existent-id", exception.getMessage());
    }

    

}

