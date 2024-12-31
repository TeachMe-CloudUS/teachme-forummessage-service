package us.cloud.teachme.forum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.service.ForumService;
import us.cloud.teachme.forummessage.service.BadWordsService;
import us.cloud.teachme.forum.controller.ForumController;

@SpringBootTest
class ForumControllerUnitaryTest {

    @Mock
    private ForumService forumService;

    @Mock
    private BadWordsService badWordsService;

    @InjectMocks
    private ForumController forumController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllForums() {
        List<Forum> mockForums = Arrays.asList(new Forum("1", "Forum 1", new Date(), new Date()), new Forum("2", "Forum 2", new Date(), new Date()));
        when(forumService.getAllForums()).thenReturn(mockForums);

        List<Forum> forums = forumController.getAllForums();

        assertEquals(2, forums.size());
        verify(forumService, times(1)).getAllForums();
    }

    @Test
    void testGetForumById_Found() {
        Forum mockForum = new Forum("1", "Forum 1", new Date(), new Date());
        when(forumService.getForumById("1")).thenReturn(Optional.of(mockForum));

        ResponseEntity<Forum> response = forumController.getForumById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockForum, response.getBody());
        verify(forumService, times(1)).getForumById("1");
    }

    @Test
    void testGetForumById_NotFound() {
        when(forumService.getForumById("1")).thenReturn(Optional.empty());

        ResponseEntity<Forum> response = forumController.getForumById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(forumService, times(1)).getForumById("1");
    }

    @Test
    @Rollback(true)
    void testCreateForum_Valid() {
        Forum forumToCreate = new Forum(null, "Valid Forum", new Date(), new Date());
        Forum createdForum = new Forum("1", "Valid Forum", new Date(), new Date());

        when(badWordsService.containsBadWords("Valid Forum")).thenReturn(false);
        when(forumService.createForum(forumToCreate)).thenReturn(createdForum);

        ResponseEntity<?> response = forumController.createForum(forumToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdForum, response.getBody());
        verify(badWordsService, times(1)).containsBadWords("Valid Forum");
        verify(forumService, times(1)).createForum(forumToCreate);
    }

    @Test
    @Rollback(true)
    void testCreateForum_WithBadWords() {
        Forum forumToCreate = new Forum(null, "Bad Forum", new Date(), new Date());

        when(badWordsService.containsBadWords("Bad Forum")).thenReturn(true);

        ResponseEntity<?> response = forumController.createForum(forumToCreate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The content has bad words", response.getBody());
        verify(badWordsService, times(1)).containsBadWords("Bad Forum");
        verifyNoInteractions(forumService);
    }

  /*  @Test
    @Rollback(true)
    void testUpdateForum_Valid() {
        Forum forumToUpdate = new Forum(null, "Updated Forum", new Date(), new Date());
        Forum updatedForum = new Forum("1", "Updated Forum", new Date(), new Date());

        when(badWordsService.containsBadWords("Updated Forum")).thenReturn(false);
        when(forumService.updateForum("1", forumToUpdate)).thenReturn(updatedForum);

        ResponseEntity<?> response = forumController.updateForum("1", forumToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedForum, response.getBody());
        verify(badWordsService, times(1)).containsBadWords("Updated Forum");
        verify(forumService, times(1)).updateForum("1", forumToUpdate);
    }

    @Test
    @Rollback(true)
    void testUpdateForum_NotFound() {
        Forum forumToUpdate = new Forum(null, "Updated Forum", new Date(), new Date());

        when(badWordsService.containsBadWords("Updated Forum")).thenReturn(false);
        doThrow(new RuntimeException("Not Found")).when(forumService).updateForum("1", forumToUpdate);

        ResponseEntity<?> response = forumController.updateForum("1", forumToUpdate);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(badWordsService, times(1)).containsBadWords("Updated Forum");
        verify(forumService, times(1)).updateForum("1", forumToUpdate);
    }*/

    @Test
    @Rollback(true)
    void testDeleteForum_Valid() {
        doNothing().when(forumService).deleteForumByCourseId("1");

        ResponseEntity<Void> response = forumController.deleteForum("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(forumService, times(1)).deleteForumByCourseId("1");
    }

    @Test
    @Rollback(true)
    void testDeleteForum_NotFound() {
        doThrow(new RuntimeException("Not Found")).when(forumService).deleteForumByCourseId("1");

        ResponseEntity<Void> response = forumController.deleteForum("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(forumService, times(1)).deleteForumByCourseId("1");
    }

    @Test
    @Rollback(true)
    void testDeleteAllForums() {
        doNothing().when(forumService).deleteAllForums();

        ResponseEntity<Void> response = forumController.deleteAllForums();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(forumService, times(1)).deleteAllForums();
    }
}

