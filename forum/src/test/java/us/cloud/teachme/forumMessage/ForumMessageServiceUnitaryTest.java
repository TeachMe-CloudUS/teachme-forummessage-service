package us.cloud.teachme.forumMessage;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.transaction.annotation.Transactional;

import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.repository.ForumMessageRepository;
import us.cloud.teachme.forummessage.service.ForumMessageService;

@SpringBootTest
@Transactional
class ForumMessageServiceUnitaryTest {

    @Mock
    private ForumMessageRepository forumMessageRepository;

    @InjectMocks
    private ForumMessageService forumMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests positivos
    @Test
    void testGetAllForumMessages_Positive() {
        ForumMessage message1 = new ForumMessage();
        ForumMessage message2 = new ForumMessage();
        List<ForumMessage> messages = Arrays.asList(message1, message2);

        when(forumMessageRepository.findAll()).thenReturn(messages);

        List<ForumMessage> result = forumMessageService.getAllForumMessages();

        assertEquals(2, result.size());
        verify(forumMessageRepository, times(1)).findAll();
    }

    @Test
    void testGetForumMessageById_Positive() {
        String id = "1";
        ForumMessage message = new ForumMessage();
        message.setId(id);

        when(forumMessageRepository.findById(id)).thenReturn(Optional.of(message));

        Optional<ForumMessage> result = forumMessageService.getForumMessageById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(forumMessageRepository, times(1)).findById(id);
    }

    @Test
    void testCreateForumMessage_Positive() {
        ForumMessage message = new ForumMessage();
        message.setContent("Test Content");

        when(forumMessageRepository.save(any(ForumMessage.class))).thenReturn(message);

        ForumMessage result = forumMessageService.createForumMessage(message);

        assertNotNull(result);
        assertEquals("Test Content", result.getContent());
        assertNotNull(result.getCreationDate());
        verify(forumMessageRepository, times(1)).save(message);
    }

    @Test
    void testUpdateForumMessage_Positive() {
        String id = "1";
        ForumMessage existingMessage = new ForumMessage();
        existingMessage.setId(id);
        existingMessage.setContent("Old Content");

        ForumMessage updatedMessage = new ForumMessage();
        updatedMessage.setContent("New Content");

        when(forumMessageRepository.findById(id)).thenReturn(Optional.of(existingMessage));
        when(forumMessageRepository.save(any(ForumMessage.class))).thenReturn(existingMessage);

        ForumMessage result = forumMessageService.updateForumMessage(id, updatedMessage);

        assertNotNull(result);
        assertEquals("New Content", result.getContent());
        verify(forumMessageRepository, times(1)).findById(id);
        verify(forumMessageRepository, times(1)).save(existingMessage);
    }

    @Test
    void testDeleteForumMessage_Positive() {
        String id = "1";

        when(forumMessageRepository.existsById(id)).thenReturn(true);

        forumMessageService.deleteForumMessage(id);

        verify(forumMessageRepository, times(1)).existsById(id);
        verify(forumMessageRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetForumMessagesByForumId_Positive() {
        String forumId = "forum1";
        ForumMessage message1 = new ForumMessage();
        ForumMessage message2 = new ForumMessage();
        List<ForumMessage> messages = Arrays.asList(message1, message2);

        when(forumMessageRepository.findByForumId(forumId)).thenReturn(messages);

        List<ForumMessage> result = forumMessageService.getForumMessagesByForumId(forumId);

        assertEquals(2, result.size());
        verify(forumMessageRepository, times(1)).findByForumId(forumId);
    }

    // Tests negativos
    @Test
    void testGetForumMessageById_Negative() {
        String id = "1";

        when(forumMessageRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ForumMessage> result = forumMessageService.getForumMessageById(id);

        assertFalse(result.isPresent());
        verify(forumMessageRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateForumMessage_Negative() {
        String id = "1";
        ForumMessage updatedMessage = new ForumMessage();

        when(forumMessageRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            forumMessageService.updateForumMessage(id, updatedMessage)
        );

        assertEquals("ForumMessage not found with id " + id, exception.getMessage());
        verify(forumMessageRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteForumMessage_Negative() {
        String id = "1";

        when(forumMessageRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            forumMessageService.deleteForumMessage(id)
        );

        assertEquals("ForumMessage not found with id " + id, exception.getMessage());
        verify(forumMessageRepository, times(1)).existsById(id);
    }

    @Test
    void testGetForumMessagesByForumId_Negative() {
        String forumId = "forum1";

        when(forumMessageRepository.findByForumId(forumId)).thenReturn(Arrays.asList());

        List<ForumMessage> result = forumMessageService.getForumMessagesByForumId(forumId);

        assertTrue(result.isEmpty());
        verify(forumMessageRepository, times(1)).findByForumId(forumId);
    }
}

