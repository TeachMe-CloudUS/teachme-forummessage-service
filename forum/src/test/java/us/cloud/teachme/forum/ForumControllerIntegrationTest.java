package us.cloud.teachme.forum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import us.cloud.teachme.forum.controller.ForumController;
import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.service.ForumService;
import us.cloud.teachme.forummessage.service.BadWordsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ForumController.class)
class ForumControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ForumService forumService;

    @Autowired
    private BadWordsService badWordsService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ForumController(forumService, badWordsService)).build();
    }

    /*// Test: GET /api/${api.version}/forums - Obtiene todos los foros
    @Test
    void testGetAllForums() throws Exception {
        Forum forum1 = new Forum( "course1", "Forum 1", new Date(), new Date());
        Forum forum2 = new Forum( "course2", "Forum 2", new Date(), new Date());
        forumService.createForum(forum1);
        forumService.createForum(forum2);

        mockMvc.perform(get("/api/v1/forums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Forum 1"))
                .andExpect(jsonPath("$[1].name").value("Forum 2"));
    }

    // Test: GET /api/${api.version}/forums/{id} - Obtiene un foro por ID
    @Test
    void testGetForumById() throws Exception {
        Forum forum = new Forum( "course1", "Test Forum", new Date(), new Date());
        Forum createdForum = forumService.createForum(forum);

        mockMvc.perform(get("/api/v1/forums/{id}", createdForum.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Forum"));
    }

    // Test: POST /api/${api.version}/forums - Crea un nuevo foro
    @Test
    void testCreateForum() throws Exception {
        Forum forum = new Forum( "course1", "New Forum", new Date(), new Date());

        mockMvc.perform(post("/api/v1/forums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(forum)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Forum"));
    }

    // Test: POST /api/${api.version}/forums - Intenta crear un foro con palabras malas
    @Test
    void testCreateForumWithBadWords() throws Exception {
        Forum forum = new Forum( "course1", "Bad Forum with curse", new Date(), new Date());

        mockMvc.perform(post("/api/v1/forums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(forum)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The content has bad words"));
    }

    // Test: PUT /api/${api.version}/forums/{id} - Actualiza un foro existente
    @Test
    void testUpdateForum() throws Exception {
        Forum forum = new Forum( "course1", "Forum to update", new Date(), new Date());
        Forum createdForum = forumService.createForum(forum);

        Forum updatedForum = new Forum( "course1", "Updated Forum", new Date(), new Date());

        mockMvc.perform(put("/api/v1/forums/{id}", createdForum.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedForum)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Forum"));
    }

    // Test: PUT /api/${api.version}/forums/{id} - Intenta actualizar un foro con palabras malas
    @Test
    void testUpdateForumWithBadWords() throws Exception {
        Forum forum = new Forum( "course1", "Forum to update", new Date(), new Date());
        Forum createdForum = forumService.createForum(forum);

        Forum updatedForum = new Forum( "course1", "Updated Forum with curse", new Date(), new Date());

        mockMvc.perform(put("/api/v1/forums/{id}", createdForum.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedForum)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The content has bad words"));
    }

    // Test: DELETE /api/${api.version}/forums/{id} - Elimina un foro por ID
    @Test
    void testDeleteForum() throws Exception {
        Forum forum = new Forum( "course1", "Forum to delete", new Date(), new Date());
        Forum createdForum = forumService.createForum(forum);

        mockMvc.perform(delete("/api/v1/forums/{id}", createdForum.getId()))
                .andExpect(status().isNoContent());

        Optional<Forum> deletedForum = forumService.getForumById(createdForum.getId());
        assertTrue(deletedForum.isEmpty());
    }

    // Test: DELETE /api/${api.version}/forums/{id} - Intenta eliminar un foro inexistente
    @Test
    void testDeleteForumNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/forums/{id}", "non-existent-id"))
                .andExpect(status().isNotFound());
    }

    // Test: DELETE /api/${api.version}/forums - Elimina todos los foros
    @Test
    void testDeleteAllForums() throws Exception {
        Forum forum1 = new Forum( "course1", "Forum 1", new Date(), new Date());
        Forum forum2 = new Forum( "course2", "Forum 2", new Date(), new Date());
        forumService.createForum(forum1);
        forumService.createForum(forum2);

        mockMvc.perform(delete("/api/v1/forums"))
                .andExpect(status().isNoContent());

        List<Forum> allForums = forumService.getAllForums();
        assertTrue(allForums.isEmpty());
    }*/
}

