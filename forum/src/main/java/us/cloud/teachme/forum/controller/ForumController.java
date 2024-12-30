package us.cloud.teachme.forum.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.service.ForumService;

import us.cloud.teachme.forummessage.service.BadWordsService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/forums")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @Autowired
    private BadWordsService badWordsService;

    public ForumController(ForumService forumService, BadWordsService badWordsService) {

        this.forumService = forumService;

        this.badWordsService = badWordsService;
    }

    // GET /api/${api.version}/messages - Obtiene todos los cursos
    @GetMapping
    public List<Forum> getAllForums() {
        return forumService.getAllForums();
    }

    

    // GET /api/${api.version}/messages /{id} - Obtiene un curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Forum> getForumById(@PathVariable String id) {
        return forumService.getForumById(id)
                .map(forum -> ResponseEntity.ok(forum))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/${api.version}/messages  - Crea un nuevo curso
    @PostMapping
    public ResponseEntity<?> createForum(@Valid @RequestBody Forum forum) {
        System.err.println(forum.getName());
        if (badWordsService.containsBadWords(forum.getName())) {
            return ResponseEntity.badRequest().body("The content has bad words");
        }
        Forum createdForum = forumService.createForum(forum);
        return new ResponseEntity<>(createdForum, HttpStatus.CREATED);
    }

    // PUT /api/${api.version}/messages/{id} - Actualiza un curso existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateForum(@PathVariable String id, @Valid @RequestBody Forum forum) {
        try {
            if (badWordsService.containsBadWords(forum.getName())) {
                return ResponseEntity.badRequest().body("The content has bad words");
            }
            Forum updatedForum = forumService.updateForum(id, forum);
            return ResponseEntity.ok(updatedForum);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages/{id} - Elimina un curso por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForum(@PathVariable String id) {
        try {
            forumService.deleteForum(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages - Elimina todos los cursos
    @DeleteMapping
    public ResponseEntity<Void> deleteAllForums() {
        forumService.deleteAllForums();
        return ResponseEntity.noContent().build();
    }
}
