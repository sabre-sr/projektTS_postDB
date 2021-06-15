package ts.projekt.postDB;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    public static Database bazaDanych = new Database();
    private static Connection conn;

    private Database() {
        Connection conn = null;
        String path = "jdbc:mysql://localhost:3306/posts";
        try {
            conn = DriverManager.getConnection(path, "root", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (conn != null)
            Database.conn = conn;
        try {
            if (!checkIfDbExists())
                createDb();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkIfDbExists() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                    SELECT *
                    FROM information_schema.tables
                    WHERE table_schema = 'posts' 
                        AND table_name = 'posts'
                    LIMIT 1;
                """);
        ResultSet result = statement.executeQuery();
        boolean exists = result.next();
        result.close();
        statement.close();
        return exists;
    }

    private void createDb() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                CREATE TABLE `posts` (
                  `id` int(11) NOT NULL AUTO_INCREMENT,
                  `id_autor` int(11) NOT NULL,
                  `id_reply` int(11) DEFAULT NULL,
                  `post_date` datetime DEFAULT NULL,
                  `post_body` varchar(500) NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `table_name_id_uindex` (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Post database';
                """);
        statement.execute();
        statement.close();
    }

    public Post addPost(Post post) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO posts (id_autor, id_reply, post_date, post_body, image_filename) VALUES 
                (?, ?, NOW(), ?, ?);
                """, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, post.getAuthor().getId());
        statement.setInt(2, post.getRepliedTo().getId());
        statement.setString(3, post.getPostBody());
        statement.setString(4, post.getFilename());
        statement.execute();
        ResultSet keys = statement.getGeneratedKeys();
        int id = 0;
        if (keys.next())
            id = keys.getInt(1);
        statement.close();
        keys.close();
        if (id != 0)
            return getPost(id);
        else return null;
    }

    public Post getPost(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                        SELECT * FROM posts WHERE id = ?;
                """);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        Post post = null;
        if (result.next()) {
            post = new Post(new User(result.getInt("id_autor")), result.getInt("id"), result.getString("post_body"),
                    result.getDate("post_date").toLocalDate(), result.getString("image_filename"));
        }
        result.close();
        statement.close();
        return post;
    }

    public boolean removePost(int id) {
        return false;
    }

    public ArrayList<Post> getReplies(int post_id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM posts.posts WHERE id_reply = ? ORDER BY post_date DESC;
                """);
        statement.setInt(1, post_id);
        return getPostsArray(statement);
    }

    private ArrayList<Post> getPostsArray(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        ArrayList<Post> lista = new ArrayList<>();
        while (rs.next()) {
            Post post = new Post(new User(
                    rs.getInt("id_autor")),
                    rs.getInt("id"),
                    rs.getString("post_body"),
                    rs.getDate("post_date").toLocalDate(),
                    rs.getString("image_filename"));
            post.setRepliedTo(new Post(rs.getInt("id_reply")));
            lista.add(post);
        }
        return lista;
    }

    public ArrayList<Post> getAllPosts(int limit) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM posts.posts WHERE id_reply IS NULL ORDER BY post_date DESC;
                """);
        return getPostsArray(statement);
    }

    public Post removePost(Post post) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM posts WHERE id = ?;");
        statement.setInt(1, post.getId());
        statement.execute();
        statement.close();
        return post;
    }

    public static void main(String[] args) {

    }
}
