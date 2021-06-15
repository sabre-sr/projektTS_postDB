package ts.projekt.postDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class DatabaseService {
    private final static Logger log = LoggerFactory.getLogger(DatabaseService.class);

    private final HttpServletRequest request;

    public DatabaseService(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping(path = "/get-posts")
    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posty = new ArrayList<>();
        try {
            posty = Database.bazaDanych.getAllPosts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return posty;
    }

    @DeleteMapping(path = "/deletePost/{id}")
    public Post deletePost(@PathVariable int id) throws SQLException {
        return Database.bazaDanych.removePost(new Post(id));
    }

    @GetMapping(path = "post/{id}")
    public Post getPost(@PathVariable int id) throws SQLException {
        return Database.bazaDanych.getPost(id);
    }

    @PostMapping(path = "addPost")
    public Post addPost(@RequestBody Post post) throws SQLException {
        return Database.bazaDanych.addPost(post);
    }

    @GetMapping(path = "replies/{id}")
    public ArrayList<Post> getReplies(@PathVariable int id) throws SQLException {
        return Database.bazaDanych.getReplies(id);
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

    @GetMapping(path="getFile/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String name) {
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to read requested file.");
        String imagepath = request.getServletContext().getRealPath("/uploads/") + name;
        File file = new File(imagepath);
        if (!file.exists())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to read requested file.");
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to translate file to bytes.");
        }
    }
}
