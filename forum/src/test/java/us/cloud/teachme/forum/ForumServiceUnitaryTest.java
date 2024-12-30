package us.cloud.teachme.forum;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.repository.ForumRepository;
import us.cloud.teachme.forum.service.ForumService;

@SpringBootTest
class ForumServiceUnitaryTest {

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumService forumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllForums() {
        Forum forum1 = new Forum("course1","Forum 1",  new Date(), new Date());
        Forum forum2 = new Forum( "course2","Forum 2", new Date(), new Date());
        when(forumRepository.findAll()).thenReturn(Arrays.asList(forum1, forum2));

        List<Forum> forums = forumService.getAllForums();

        assertEquals(2, forums.size());
        verify(forumRepository, times(1)).findAll();
    }

    @Test
    void testGetForumById() {
        Forum forum = new Forum("course1","Forum 1",  new Date(), new Date());
        when(forumRepository.findById("1")).thenReturn(Optional.of(forum));

        Optional<Forum> result = forumService.getForumById("1");

        assertTrue(result.isPresent());
        assertEquals("Forum 1", result.get().getName());
        verify(forumRepository, times(1)).findById("1");
    }

    @Test
    @Rollback(true)
    void testCreateForum() {
        Forum forum = new Forum( "course1", "New Forum", null, null);
        when(forumRepository.save(any(Forum.class))).thenAnswer(invocation -> {
            Forum savedForum = invocation.getArgument(0);
            savedForum.setId("1");
            return savedForum;
        });

        Forum createdForum = forumService.createForum(forum);

        assertNotNull(createdForum.getId());
        assertEquals("New Forum", createdForum.getName());
        verify(forumRepository, times(1)).save(any(Forum.class));
    }

    @Test
    @Rollback(true)
    void testUpdateForum() {
        Forum existingForum = new Forum( "course1", "Old Forum", new Date(), new Date());
        Forum updatedForum = new Forum( "course1", "Updated Forum", null, null);
        when(forumRepository.findById("1")).thenReturn(Optional.of(existingForum));
        when(forumRepository.save(any(Forum.class))).thenReturn(existingForum);

        Forum result = forumService.updateForum("1", updatedForum);

        assertEquals("Updated Forum", result.getName());
        verify(forumRepository, times(1)).findById("1");
        verify(forumRepository, times(1)).save(existingForum);
    }

    @Test
    @Rollback(true)
    void testDeleteForum() {
        when(forumRepository.existsById("1")).thenReturn(true);

        forumService.deleteForum("1");

        verify(forumRepository, times(1)).existsById("1");
        verify(forumRepository, times(1)).deleteById("1");
    }

    @Test
    @Rollback(true)
    void testDeleteForumNotFound() {
        when(forumRepository.existsById("1")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> forumService.deleteForum("1"));

        assertEquals("Forum not found with id 1", exception.getMessage());
        verify(forumRepository, times(1)).existsById("1");
        verify(forumRepository, never()).deleteById("1");
    }

    @Test
    @Rollback(true)
    void testDeleteAllForums() {
        forumService.deleteAllForums();

        verify(forumRepository, times(1)).deleteAll();
    }

    @Test
    void testGetForumsByForumId() {
        Forum forum = new Forum( "course1", "Forum by Course", new Date(), new Date());
        when(forumRepository.findByCourseId("course1")).thenReturn(forum);

        Forum result = forumService.getForumsByForumId("course1");

        assertNotNull(result);
        assertEquals("Forum by Course", result.getName());
        verify(forumRepository, times(1)).findByCourseId("course1");
    }

    @Test
    @Rollback(true)
    void testDeleteForumsByForumId() {
        forumService.deleteForumsByForumId("1");

        verify(forumRepository, times(1)).deleteById("1");
    }
    
    @Test
    void testGetForumByIdNotFound() {
        when(forumRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        Optional<Forum> result = forumService.getForumById("non-existent-id");

        assertFalse(result.isPresent(), "El foro no debería existir");
        verify(forumRepository, times(1)).findById("non-existent-id");
    }

    @Test
    @Rollback(true)
    void testUpdateForumNotFound() {
        when(forumRepository.findById("non-existent-id")).thenReturn(Optional.empty());
        Forum updatedForum = new Forum("course1", "Updated Forum", null, null);

        Exception exception = assertThrows(RuntimeException.class, 
            () -> forumService.updateForum("non-existent-id", updatedForum));

        assertEquals("Forum not found with id non-existent-id", exception.getMessage());
        verify(forumRepository, times(1)).findById("non-existent-id");
        verify(forumRepository, never()).save(any(Forum.class));
    }


    

    @Test
    void testGetForumsByCourseIdNotFound() {
        when(forumRepository.findByCourseId("non-existent-course-id")).thenReturn(null);

        Forum result = forumService.getForumsByForumId("non-existent-course-id");

        assertNull(result, "El foro no debería existir para un CourseId inexistente");
        verify(forumRepository, times(1)).findByCourseId("non-existent-course-id");
    }

    @Test
    @Rollback(true)
    void testDeleteForumsByForumIdNotFound() {
        doThrow(new RuntimeException("Forum not found with id non-existent-id"))
            .when(forumRepository).deleteById("non-existent-id");

        Exception exception = assertThrows(RuntimeException.class, 
            () -> forumService.deleteForumsByForumId("non-existent-id"));

        assertEquals("Forum not found with id non-existent-id", exception.getMessage());
        verify(forumRepository, times(1)).deleteById("non-existent-id");
    }

}
