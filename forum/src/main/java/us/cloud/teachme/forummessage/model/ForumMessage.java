package us.cloud.teachme.forummessage.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotNull(message = "The studentId cannot be null")
    private String userId;

    @NotNull(message = "The message cannot be null")
    @NotBlank(message = "The message cannot be empty")
    @Size(max = 300, message = "The message must have a maximum of 300 characters")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifDate;
    @NotNull(message = "The forumId cannot be null")
    private String forumId;
    

    
}
