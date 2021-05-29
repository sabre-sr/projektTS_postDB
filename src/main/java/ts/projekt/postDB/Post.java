package ts.projekt.postDB;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Post implements Serializable {
    public User author;
    private int id;
    private String postBody;
    private LocalDate creationDate;

    public Post(User author, int id, String postBody, LocalDate creationDate) {
        this.author = author;
        this.id = id;
        this.postBody = postBody;
        this.creationDate = creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public String getPostBody() {
        return postBody;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
