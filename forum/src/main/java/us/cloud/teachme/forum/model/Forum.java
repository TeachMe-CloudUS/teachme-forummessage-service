package us.cloud.teachme.forum.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import us.cloud.teachme.forummessage.model.ForumMessage;

@Getter
@Setter
@Document(collection = "forum")
public class Forum {
    
    @Id
    private String id;
    @NotNull(message = "The courseId cannot be null")
    private String courseId;
    @NotNull(message = "The name cannot be null")
    @NotBlank(message = "The name cannot be empty")
    @Size(max = 50, message = "The name must have a maximum of 50 characters")
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifDate;
    //private List<ForumMessage> messages;
    

    public Forum( String courseId, String name, Date creationDate, Date lastModifDate) {
        this.courseId = courseId;
        this.name = name;
        this.creationDate = creationDate;
        this.lastModifDate = lastModifDate;
    }


    

    
}
