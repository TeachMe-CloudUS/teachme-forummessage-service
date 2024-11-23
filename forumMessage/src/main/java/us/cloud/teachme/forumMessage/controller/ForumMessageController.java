package us.cloud.teachme.forummessage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/${api.version}/messages")
public class ForumMessageController {
    @Autowired
    private ForumMessageService ForumMessageService;

    // GET /api/${api.version}/messages - Obtiene todos los cursos
    @GetMapping
    public List<ForumMessage> getAllForumMessages() {
        return ForumMessageService.getAllForumMessages();
    }

    

    // GET /api/${api.version}/messages /{id} - Obtiene un curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ForumMessage> getForumMessageById(@PathVariable String id) {
        return ForumMessageService.getForumMessageById(id)
                .map(ForumMessage -> ResponseEntity.ok(ForumMessage))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/${api.version}/messages  - Crea un nuevo curso
    @PostMapping
    public ResponseEntity<ForumMessage> createForumMessage(@RequestBody ForumMessage ForumMessage) {
        ForumMessage createdForumMessage = ForumMessageService.createForumMessage(ForumMessage);
        return new ResponseEntity<>(createdForumMessage, HttpStatus.CREATED);
    }

    // PUT /api/${api.version}/messages/{id} - Actualiza un curso existente
    @PutMapping("/{id}")
    public ResponseEntity<ForumMessage> updateForumMessage(@PathVariable String id, @RequestBody ForumMessage ForumMessage) {
        try {
            ForumMessage updatedForumMessage = ForumMessageService.updateForumMessage(id, ForumMessage);
            return ResponseEntity.ok(updatedForumMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/${api.version}/messages/{id} - Elimina un curso por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForumMessage(@PathVariable String id) {
        try {
            ForumMessageService.deleteForumMessage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
