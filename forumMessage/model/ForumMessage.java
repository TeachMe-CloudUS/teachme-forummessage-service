package us.cloud.teachme.forumMessage.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "forumMessage")
public class ForumMessage {
    @Id
    private String studentId;
    private String content;
    private Date creationDate;
    private Date lastModifDate;
    private String forumId;
    

    
}
