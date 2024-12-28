package us.cloud.teachme.forumMessage;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.service.ForumMessageService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ForumMessageServiceIntegrationTest {

    @Autowired
    private ForumMessageService forumMessageService;

    // Test positivos
    @Test
    void testCreateAndRetrieveForumMessage_Positive() {
        ForumMessage message = new ForumMessage();
        message.setContent("Integration Test Content");
        message.setForumId("forum1");

        // Test create
        ForumMessage savedMessage = forumMessageService.createForumMessage(message);
        assertNotNull(savedMessage);
        assertNotNull(savedMessage.getId());

        // Test retrieve
        List<ForumMessage> retrievedMessages = forumMessageService.getForumMessagesByForumId("forum1");
        assertEquals(1, retrievedMessages.size());
        assertEquals("Integration Test Content", retrievedMessages.get(0).getContent());
    }

    @Test
    void testUpdateForumMessage_Positive() {
        ForumMessage message = new ForumMessage();
        message.setContent("Original Content");
        message.setForumId("forum1");

        ForumMessage savedMessage = forumMessageService.createForumMessage(message);

        ForumMessage updatedMessage = new ForumMessage();
        updatedMessage.setContent("Updated Content");

        ForumMessage result = forumMessageService.updateForumMessage(savedMessage.getId(), updatedMessage);

        assertNotNull(result);
        assertEquals("Updated Content", result.getContent());
    }

    @Test
    void testDeleteForumMessage_Positive() {
        
        Integer numberOfForumMessages = forumMessageService.getAllForumMessages().size();
        ForumMessage message = new ForumMessage();
        message.setContent("To be deleted");
        message.setForumId("forum1");
       
        ForumMessage savedMessage = forumMessageService.createForumMessage(message);
        forumMessageService.deleteForumMessage(savedMessage.getId());

        List<ForumMessage> retrievedMessages = forumMessageService.getForumMessagesByForumId("forum1");
        Integer expectedNumberOfForumMessages = forumMessageService.getAllForumMessages().size();
        assertEquals(expectedNumberOfForumMessages,numberOfForumMessages);
    }

    @Test
    void testGetAllForumMessages_Positive() {
        Integer numberOfForummessage = forumMessageService.getAllForumMessages().size();
        ForumMessage message1 = new ForumMessage();
        message1.setContent("Message 1");
        message1.setForumId("forum1");

        ForumMessage message2 = new ForumMessage();
        message2.setContent("Message 2");
        message2.setForumId("forum2");

        forumMessageService.createForumMessage(message1);
        forumMessageService.createForumMessage(message2);

        List<ForumMessage> allMessages = forumMessageService.getAllForumMessages();
        Integer expectedNumberOfForummessage = numberOfForummessage + 2;
        assertEquals(expectedNumberOfForummessage, allMessages.size());
    }

    @Test
    void testDeleteForumMessagesByForumId_Positive() {
        ForumMessage message1 = new ForumMessage();
        message1.setContent("Message 1");
        message1.setForumId("forum1");

        ForumMessage message2 = new ForumMessage();
        message2.setContent("Message 2");
        message2.setForumId("forum1");

        forumMessageService.createForumMessage(message1);
        forumMessageService.createForumMessage(message2);

        forumMessageService.deleteForumMessagesByForumId("forum1");

        List<ForumMessage> remainingMessages = forumMessageService.getForumMessagesByForumId("forum1");
        assertTrue(remainingMessages.isEmpty());
    }

    // Test negativos
    @Test
    void testUpdateForumMessage_Negative() {
        String invalidId = "999";
        ForumMessage updatedMessage = new ForumMessage();
        updatedMessage.setContent("Updated Content");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            forumMessageService.updateForumMessage(invalidId, updatedMessage);
        });

        assertEquals("ForumMessage not found with id " + invalidId, exception.getMessage());
    }

    @Test
    void testDeleteForumMessage_Negative() {
        String invalidId = "999";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            forumMessageService.deleteForumMessage(invalidId);
        });

        assertEquals("ForumMessage not found with id " + invalidId, exception.getMessage());
    }

    @Test
    void testGetForumMessagesByForumId_Negative() {
        String nonExistentForumId = "nonexistent_forum";

        List<ForumMessage> retrievedMessages = forumMessageService.getForumMessagesByForumId(nonExistentForumId);

        assertTrue(retrievedMessages.isEmpty());
    }

    @Test
    void testGetForumMessageById_Negative() {
        String invalidId = "999";

        Optional<ForumMessage> retrievedMessage = forumMessageService.getForumMessageById(invalidId);

        assertTrue(retrievedMessage.isEmpty());
    }
}
