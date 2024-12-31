package us.cloud.teachme.forum;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.service.ForumService;
import us.cloud.teachme.forum.service.UserService;
import us.cloud.teachme.forummessage.service.BadWordsService;
import us.cloud.teachme.forum.controller.ForumController;

@WebMvcTest(ForumController.class)
public class ForumControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumService forumService;

    @MockBean
    private BadWordsService badWordsService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllForums_shouldReturnForumsList() throws Exception {
        Forum forum1 = new Forum("1", "Course 1", new Date(), new Date());
        Forum forum2 = new Forum("2", "Course 2", new Date(), new Date());

        when(forumService.getAllForums()).thenReturn(Arrays.asList(forum1, forum2));

        mockMvc.perform(get("/api/v1/forums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value("1"))
                .andExpect(jsonPath("$[0].name").value("Course 1"))
                .andExpect(jsonPath("$[1].courseId").value("2"))
                .andExpect(jsonPath("$[1].name").value("Course 2"));
    }

    @Test
    public void getForumById_shouldReturnForum() throws Exception {
        Forum forum = new Forum("1", "Course 1", new Date(), new Date());

        when(forumService.getForumById("1")).thenReturn(Optional.of(forum));

        mockMvc.perform(get("/api/v1/forums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value("1"))
                .andExpect(jsonPath("$.name").value("Course 1"));
    }

    @Test
    public void getForumById_shouldReturnNotFound() throws Exception {
        when(forumService.getForumById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/forums/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Rollback(true)
    public void createForum_shouldReturnCreatedForum() throws Exception {
        Forum forum = new Forum("1", "Course 1", new Date(), new Date());
        Forum createdForum = new Forum("1", "Course 1", new Date(), new Date());

        when(badWordsService.containsBadWords("Course 1")).thenReturn(false);
        when(forumService.createForum(any(Forum.class))).thenReturn(createdForum);

        mockMvc.perform(post("/api/v1/forums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forum)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value("1"))
                .andExpect(jsonPath("$.name").value("Course 1"));
    }

    @Test
    @Rollback(true)
    public void createForum_shouldReturnBadRequestWhenContainsBadWords() throws Exception {
        Forum forum = new Forum("1", "BadWord bitch", new Date(), new Date());

        when(badWordsService.containsBadWords("BadWord bitch")).thenReturn(true);

        mockMvc.perform(post("/api/v1/forums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forum)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The content has bad words"));
    }

    /*@Test
    @Rollback(true)
    public void updateForum_shouldReturnUpdatedForum() throws Exception {
        Forum forum = new Forum("2", "Updated Course", new Date(), new Date());
        Forum updatedForum = new Forum("1", "Updated Course", new Date(), new Date());

        when(badWordsService.containsBadWords("Updated Course")).thenReturn(false);
        when(forumService.updateForum(eq("1"), any(Forum.class))).thenReturn(updatedForum);

        mockMvc.perform(put("/api/v1/forums/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forum)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value("1"))
                .andExpect(jsonPath("$.name").value("Updated Course"));
    }

    @Test
    @Rollback(true)
    public void updateForum_shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Forum forum = new Forum("1", "Updated Course", new Date(), new Date());

        when(badWordsService.containsBadWords("Updated Course")).thenReturn(false);
        when(forumService.updateForum(eq("1"), any(Forum.class))).thenThrow(new RuntimeException("Not Found"));

        mockMvc.perform(put("/api/v1/forums/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forum)))
                .andExpect(status().isNotFound());
    }*/

    @Test
    @Rollback(true)
    public void deleteForum_shouldReturnNoContent() throws Exception {
        doNothing().when(forumService).deleteForumByCourseId("1");

        mockMvc.perform(delete("/api/v1/forums/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Rollback(true)
    public void deleteForum_shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(new RuntimeException("Not Found")).when(forumService).deleteForumByCourseId("1");

        mockMvc.perform(delete("/api/v1/forums/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Rollback(true)
    public void deleteAllForums_shouldReturnNoContent() throws Exception {
        doNothing().when(forumService).deleteAllForums();

        mockMvc.perform(delete("/api/v1/forums"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Rollback(true)
    public void createForum_shouldReturnBadRequestForInvalidPayload() throws Exception {
        String invalidPayload = "{\"name\":null}"; // Missing required fields

        mockMvc.perform(post("/api/v1/forums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
