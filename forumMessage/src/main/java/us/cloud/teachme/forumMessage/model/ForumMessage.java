package us.cloud.teachme.forummessage.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "forumMessage")
public class ForumMessage {
    
    @Id
    private String id;
    private String studentId;

    @NotNull(message = "The message cannot be null")
    @NotBlank(message = "The message cannot be empty")
    @Size(max = 300, message = "The message must have a maximum of 300 characters")
    private String content;
    private Date creationDate;
    private Date lastModifDate;
    private String forumId;
    

    
}
