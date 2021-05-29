package ts.projekt.postDB;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DatabaseService {
    @GetMapping(path = "/get-posts")
    public ArrayList<Post> getAllPosts(){
        ArrayList<Post> posty = new ArrayList<>();
        try {
            posty = Database.bazaDanych.getAllPosts(20);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return posty;
    }
}
