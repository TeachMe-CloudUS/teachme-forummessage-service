package us.cloud.teachme.forummessage.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import us.cloud.teachme.forummessage.model.ForumMessage;
import us.cloud.teachme.forummessage.service.ForumMessageService;
import us.cloud.teachme.forummessage.service.BadWordsService;
import us.cloud.teachme.forum.service.ForumService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/${api.version}/forums/{forumId}/messages")
public class ForumMessageController {
    @Autowired
    private ForumMessageService forumMessageService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private BadWordsService badWordsService;

    public ForumMessageController(ForumMessageService forumMessageService,ForumService forumService, BadWordsService badWordsService) {

        this.forumMessageService = forumMessageService;
        this.forumService = forumService;

        this.badWordsService = badWordsService;
    }

    /*/
    @GetMapping
    public List<ForumMessage> getAllForumMessages(@PathVariable("forumId") String forumId) {
        return forumMessageService.getAllForumMessages();
    }
*/
    

    // GET /api/${api.version}/messages /{id} - Obtiene un curso por ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ForumMessage> getForumMessageById(@PathVariable("forumId") String forumId,@PathVariable String id) {
        return forumMessageService.getForumMessageById(id)
                .map(forumMessage -> ResponseEntity.ok(forumMessage))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ForumMessage> getForumMessageByForumId(@PathVariable("forumId") String forumId) {
        return forumMessageService.getForumMessagesByForumId(forumId)/*
                .stream()
                .map(forumMessage -> ResponseEntity.ok(forumMessage))
                .toList()*/;
    }
    

    // POST /api/${api.version}/messages  - Crea un nuevo curso
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createForumMessage(@PathVariable("forumId") String forumId,@Valid @RequestBody ForumMessage forumMessage ) {
        if (badWordsService.containsBadWords(forumMessage.getContent())) {
            return ResponseEntity.badRequest().body("The content has bad words");
        }
        if (forumMessage.getForumId() == forumId) {
            return ResponseEntity.badRequest().body("The forumId is invalid");
        }
        if (forumMessage.getForumId() == null) {
            return ResponseEntity.badRequest().body("The forumId cannot be null");
        }
        if (forumService.getForumById(forumMessage.getForumId()).isEmpty()) {
            return ResponseEntity.badRequest().body("The forumId does not exist");            
        }
        ForumMessage createdForumMessage = forumMessageService.createForumMessage(forumMessage);
        return new ResponseEntity<>(createdForumMessage, HttpStatus.CREATED);
    }

    // PUT /api/${api.version}/messages/{id} - Actualiza un curso existente
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateForumMessage(@PathVariable("forumId") String forumId,@PathVariable String id, @Valid @RequestBody ForumMessage forumMessage) {
        try {
            if (badWordsService.containsBadWords(forumMessage.getContent())) {
                return ResponseEntity.badRequest().body("The content has bad words");
            }
            if (forumMessage.getForumId() == null) {
                return ResponseEntity.badRequest().body("The forumId cannot be null");
            }
            if (forumService.getForumById(forumMessage.getForumId()).isEmpty()) {
                return ResponseEntity.badRequest().body("The forumId does not exist");            
            }
            ForumMessage updatedForumMessage = forumMessageService.updateForumMessage(id, forumMessage);
            return ResponseEntity.ok(updatedForumMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages/{id} - Elimina un curso por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteForumMessage(@PathVariable("forumId") String forumId,@PathVariable String id) {
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
