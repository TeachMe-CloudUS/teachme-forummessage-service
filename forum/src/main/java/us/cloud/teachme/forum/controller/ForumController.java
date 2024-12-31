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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.cloud.teachme.forum.model.Forum;
import us.cloud.teachme.forum.service.ForumService;

import us.cloud.teachme.forummessage.service.BadWordsService;
import us.cloud.teachme.forum.service.UserService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/forums")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @Autowired
    private BadWordsService badWordsService;

    @Autowired
    private UserService userService;

    public ForumController(ForumService forumService, BadWordsService badWordsService, UserService userService) {

        this.forumService = forumService;

        this.badWordsService = badWordsService;

        this.userService = userService;
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
        if (badWordsService.containsBadWords(forum.getName())) {
            return ResponseEntity.badRequest().body("The content has bad words");
        }
        forum.setId(forum.getCourseId());
        Forum createdForum = forumService.createForum(forum);
        
        return new ResponseEntity<>(createdForum, HttpStatus.CREATED);
    }

    // PUT /api/${api.version}/messages/{id} - Actualiza un curso existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateForum(@RequestHeader("Authorization") String token, @PathVariable String id, @Valid @RequestBody Forum forum) {
        try {
                // Eliminar cualquier espacio extra del token
            token = token.trim();

            // Extraer el UserId del token
            String userId = userService.extractUserId(token);
            System.out.println("UserId extraído: " + userId);

            // Consultar el rol del usuario usando el UserId
            String role = userService.getUserRoleById(userId, token);
            System.out.println("Rol del usuario: " + role);

            // Verificar si el usuario tiene el rol de "ADMIN"
            // Y como solo hay un admin pues no se necesita verificar si es el mismo que esta logueado
            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have permission to do this action.");
            }
            
            if (badWordsService.containsBadWords(forum.getName())) {
                return ResponseEntity.badRequest().body("The content has bad words");
            }
            Forum updatedForum = forumService.updateForum(id, forum);
            return ResponseEntity.ok(updatedForum);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteForum(@PathVariable String courseId) {
        try {
            forumService.deleteForumByCourseId(courseId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllForums() {
        forumService.deleteAllForums();
        return ResponseEntity.noContent().build();
    }
}
