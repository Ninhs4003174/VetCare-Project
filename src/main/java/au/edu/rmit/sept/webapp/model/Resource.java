package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String url;
    private String type;  // Type can be "article", "video", "tutorial", etc.
    private String status; // Can be "PENDING", "APPROVED", "REJECTED"
    private String content; // Field for storing the full article or tutorial content

    public Resource() {}

    public Resource(String title, String description, String url, String type, String content) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.type = type;
        this.content = content;
        this.status = "PENDING"; // Default status when a new resource is created
    }

    // Getters and Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getUrl() { 
        return url; 
    }

    public void setUrl(String url) { 
        this.url = url; 
    }

    public String getType() { 
        return type; 
    }

    public void setType(String type) { 
        this.type = type; 
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() { 
        return content; 
    }

    public void setContent(String content) { 
        this.content = content; 
    }
}
