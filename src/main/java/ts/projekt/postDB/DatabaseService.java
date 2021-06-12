package ts.projekt.postDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DatabaseService {
    private final static Logger log = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private HttpServletRequest request;

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
    public ArrayList<Post> getReplies(@PathVariable int id) throws SQLException {
        return Database.bazaDanych.getReplies(id);
    }

    @GetMapping(path="images/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return null;
    }

    @PostMapping(path = "uploadFile")
    public Post uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String uploadsDir = "/uploads/";
            String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
            if (!new File(realPathtoUploads).exists()) {
                new File(realPathtoUploads).mkdir();
            }

            log.info("realPathtoUploads = {}", realPathtoUploads);


            String orgName = file.getOriginalFilename();
            String filePath = realPathtoUploads + orgName;
            File dest = new File(filePath);
            file.transferTo(dest);
            Post post = new Post();
            post.setFilename(orgName);
            return post;
        }
        return null;
    }
}
