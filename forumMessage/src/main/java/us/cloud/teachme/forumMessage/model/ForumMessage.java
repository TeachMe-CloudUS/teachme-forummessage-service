package us.cloud.teachme.forummessage.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "forumMessage")
public class ForumMessage {
    @Id
    private String studentId;
    @Max(200)
    private String content;
    private Date creationDate;
    private Date lastModifDate;
    private String forumId;
    

    
}
