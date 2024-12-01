package us.cloud.teachme.forumMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.cloud.teachme.forummessage.controller.ForumMessageController;
import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.service.ForumMessageService;
import us.cloud.teachme.forummessage.service.BadWordsService;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ForumMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ForumMessageService forumMessageService;

    @Mock
    private BadWordsService badWordsService;

    private ForumMessageController forumMessageController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        forumMessageController = new ForumMessageController(forumMessageService, badWordsService);
        mockMvc = MockMvcBuilders.standaloneSetup(forumMessageController).build();
    }
/*
    @Test
    public void testGetAllForumMessages() throws Exception {
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setContent("This is a test message");
        forumMessage.setStudentId("student123");
        forumMessage.setForumId("forum1");

        List<ForumMessage> forumMessages = Arrays.asList(forumMessage);

        when(forumMessageService.getAllForumMessages()).thenReturn(forumMessages);

        mockMvc.perform(get("/api/v1/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].content").value("This is a test message"));
    }
    /*@Test
    public void testGetForumMessageById() throws Exception {
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setContent("This is a test message");
        forumMessage.setStudentId("student123");
        forumMessage.setForumId("forum1");

        when(forumMessageService.getForumMessageById("1")).thenReturn(java.util.Optional.of(forumMessage));

        mockMvc.perform(get("/api/${api.version}/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.content").value("This is a test message"));
    }
    @Test
    public void testCreateForumMessage() throws Exception {
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setContent("This is a valid message");
        forumMessage.setStudentId("student123");
        forumMessage.setForumId("forum1");
        
        // Simulamos la validación de malas palabras, en este caso, el mensaje es válido
        when(badWordsService.containsBadWords(forumMessage.getContent())).thenReturn(false);
        when(forumMessageService.createForumMessage(any(ForumMessage.class))).thenReturn(forumMessage);

        mockMvc.perform(post("/api/${api.version}/messages")
                        .contentType("application/json")
                        .content("{\"content\":\"This is a valid message\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.content").value("This is a valid message"));
    }

    

    @Test
    public void testUpdateForumMessage() throws Exception {
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setContent("Updated content");
        forumMessage.setStudentId("student123");
        forumMessage.setForumId("forum1");

        when(badWordsService.containsBadWords(forumMessage.getContent())).thenReturn(false);
        when(forumMessageService.updateForumMessage(eq("1"), any(ForumMessage.class))).thenReturn(forumMessage);

        mockMvc.perform(put("/api/${api.version}/messages/1")
                        .contentType("application/json")
                        .content("{\"content\":\"Updated content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.content").value("Updated content"));
    }
    @Test
    public void testDeleteForumMessage() throws Exception {
        doNothing().when(forumMessageService).deleteForumMessage("1");
    
        mockMvc.perform(delete("/api/${api.version}/messages/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testDeleteAllForumMessages() throws Exception {
        doNothing().when(forumMessageService).deleteAllForumMessages();

        mockMvc.perform(delete("/api/${api.version}/messages"))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testGetForumMessageById_notFound() throws Exception {
        // Simulamos que el mensaje con ID "1" no existe
        when(forumMessageService.getForumMessageById("1")).thenReturn(java.util.Optional.empty());
    
        mockMvc.perform(get("/api/${api.version}/messages/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreateForumMessage_emptyContent() throws Exception {
        // Creamos un mensaje con contenido vacío
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setContent("");
    
        mockMvc.perform(post("/api/${api.version}/messages")
                        .contentType("application/json")
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The message cannot be empty"));
    }
    
    @Test
    public void testCreateForumMessage_nullContent() throws Exception {
        // Creamos un mensaje con contenido nulo
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setContent(null);
    
        mockMvc.perform(post("/api/${api.version}/messages")
                        .contentType("application/json")
                        .content("{\"content\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The message cannot be null"));
    }
    @Test
    public void testCreateForumMessage_tooLongContent() throws Exception {
        // Creamos un mensaje con más de 300 caracteres
        String longContent = "A".repeat(301); // Genera una cadena de 301 caracteres

        mockMvc.perform(post("/api/${api.version}/messages")
                        .contentType("application/json")
                        .content("{\"content\":\"" + longContent + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The message must have a maximum of 300 characters"));
    }
    @Test
    public void testCreateForumMessage_withBadWords() throws Exception {
        // Creamos un mensaje con malas palabras
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setContent("This is a bad word message bitch");
    
        // Simulamos que el mensaje contiene malas palabras
        when(badWordsService.containsBadWords(forumMessage.getContent())).thenReturn(true);
    
        mockMvc.perform(post("/api/${api.version}/messages")
                        .contentType("application/json")
                        .content("{\"content\":\"This is a bad word message\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The content has bad words"));
    }
    @Test
    public void testUpdateForumMessage_emptyContent() throws Exception {
        // Simulamos que el contenido está vacío
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setContent("");

        mockMvc.perform(put("/api/${api.version}/messages/1")
                        .contentType("application/json")
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The message cannot be empty"));
    }

    @Test
    public void testUpdateForumMessage_nullContent() throws Exception {
        // Simulamos que el contenido es nulo
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setContent(null);

        mockMvc.perform(put("/api/${api.version}/messages/1")
                        .contentType("application/json")
                        .content("{\"content\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The message cannot be null"));
    }
    @Test
    public void testUpdateForumMessage_notFound() throws Exception {
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setContent("Updated content");
    
        // Simulamos que el mensaje con ID "1" no existe
        when(forumMessageService.updateForumMessage(eq("1"), any(ForumMessage.class))).thenThrow(new RuntimeException("Message not found"));
    
        mockMvc.perform(put("/api/${api.version}/messages/1")
                        .contentType("application/json")
                        .content("{\"content\":\"Updated content\"}"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeleteForumMessage_notFound() throws Exception {
        // Simulamos que no se puede eliminar el mensaje con ID "1"
        doThrow(new RuntimeException("Message not found")).when(forumMessageService).deleteForumMessage("1");

        mockMvc.perform(delete("/api/${api.version}/messages/1"))
                .andExpect(status().isNotFound());
    }*/



}
