package us.cloud.teachme.forumMessage;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import us.cloud.teachme.forummessage.model.ForumMessage;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ForumMessageApplicationTests {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }/*

	@Test
    public void testValidContent() {
        // Crear un objeto ForumMessage con contenido válido
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("1");
        forumMessage.setStudentId("student123");
        forumMessage.setContent("This is a valid message.");
        forumMessage.setCreationDate(new Date());
        forumMessage.setLastModifDate(new Date());
        forumMessage.setForumId("forum1");

        // Validar el objeto
        var violations = validator.validate(forumMessage);
        Assertions.assertTrue(violations.isEmpty(), "The message should be valid");
    }

    @Test
    public void testContentNotNull() {
        // Crear un objeto ForumMessage con contenido nulo
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("2");
        forumMessage.setStudentId("student456");
        forumMessage.setContent(null); // Contenido nulo
        forumMessage.setCreationDate(new Date());
        forumMessage.setLastModifDate(new Date());
        forumMessage.setForumId("forum2");

        // Validar el objeto
        var violations = validator.validate(forumMessage);
        Assertions.assertFalse(violations.isEmpty(), "The message should not be null");
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The message cannot be null")));
    }

    @Test
    public void testContentNotBlank() {
        // Crear un objeto ForumMessage con contenido vacío
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("3");
        forumMessage.setStudentId("student789");
        forumMessage.setContent(" "); // Contenido vacío
        forumMessage.setCreationDate(new Date());
        forumMessage.setLastModifDate(new Date());
        forumMessage.setForumId("forum3");

        // Validar el objeto
        var violations = validator.validate(forumMessage);
        Assertions.assertFalse(violations.isEmpty(), "The message should not be blank");
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The message cannot be empty")));
    }

    @Test
    public void testContentMaxSize() {
        // Crear un objeto ForumMessage con contenido que excede el límite de caracteres
        ForumMessage forumMessage = new ForumMessage();
        forumMessage.setId("4");
        forumMessage.setStudentId("student101");
        forumMessage.setContent("A".repeat(301)); // 301 caracteres, que excede el límite de 300
        forumMessage.setCreationDate(new Date());
        forumMessage.setLastModifDate(new Date());
        forumMessage.setForumId("forum4");

        // Validar el objeto
        var violations = validator.validate(forumMessage);
        Assertions.assertFalse(violations.isEmpty(), "The message should not exceed 300 characters");
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The message must have a maximum of 300 characters")));
    }*/
	

}
