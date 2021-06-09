package ts.projekt.postDB;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DatabaseService {
    @GetMapping(path = "/get-posts")
    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posty = new ArrayList<>();
        try {
            posty = Database.bazaDanych.getAllPosts(20);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return posty;
    }

    @PostMapping(path = "addPost")
    public Post addPost(@RequestBody Post post) throws SQLException {
        return Database.bazaDanych.addPost(post);
    }

    @GetMapping(path="replies/{id}")
    public ArrayList<Post> getReplies(@PathVariable int id) {
        return Database.bazaDanych.getReplies(id);
    }
}
