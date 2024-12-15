package us.cloud.teachme.forummessage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.service.ForumMessageService;
import us.cloud.teachme.forummessage.service.BadWordsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/messages")
public class ForumMessageController {
    @Autowired
    private ForumMessageService forumMessageService;

    @Autowired
    private BadWordsService badWordsService;

    public ForumMessageController(ForumMessageService forumMessageService, BadWordsService badWordsService) {

        this.forumMessageService = forumMessageService;

        this.badWordsService = badWordsService;
    }

    // GET /api/${api.version}/messages - Obtiene todos los cursos
    @GetMapping
    public List<ForumMessage> getAllForumMessages() {
        return forumMessageService.getAllForumMessages();
    }

    

    // GET /api/${api.version}/messages /{id} - Obtiene un curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ForumMessage> getForumMessageById(@PathVariable String id) {
        return forumMessageService.getForumMessageById(id)
                .map(forumMessage -> ResponseEntity.ok(forumMessage))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/${api.version}/messages  - Crea un nuevo curso
    @PostMapping
    public ResponseEntity<?> createForumMessage(@Valid @RequestBody ForumMessage forumMessage) {
        if (badWordsService.containsBadWords(forumMessage.getContent())) {
            return ResponseEntity.badRequest().body("The content has bad words");
        }
        ForumMessage createdForumMessage = forumMessageService.createForumMessage(forumMessage);
        return new ResponseEntity<>(createdForumMessage, HttpStatus.CREATED);
    }

    // PUT /api/${api.version}/messages/{id} - Actualiza un curso existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateForumMessage(@PathVariable String id, @Valid @RequestBody ForumMessage forumMessage) {
        try {
            if (badWordsService.containsBadWords(forumMessage.getContent())) {
                return ResponseEntity.badRequest().body("The content has bad words");
            }
            ForumMessage updatedForumMessage = forumMessageService.updateForumMessage(id, forumMessage);
            return ResponseEntity.ok(updatedForumMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages/{id} - Elimina un curso por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForumMessage(@PathVariable String id) {
        try {
            forumMessageService.deleteForumMessage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages - Elimina todos los cursos
    @DeleteMapping
    public ResponseEntity<Void> deleteAllForumMessages() {
        forumMessageService.deleteAllForumMessages();
        return ResponseEntity.noContent().build();
    }
}
