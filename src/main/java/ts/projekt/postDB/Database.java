package ts.projekt.postDB;

import java.sql.*;
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
                  `id_odpowiedz` int(11) DEFAULT NULL,
                  `data_postu` datetime DEFAULT NULL,
                  `tresc` varchar(500) NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `table_name_id_uindex` (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Post database';
                """);
        statement.execute();
        statement.close();
    }

    public boolean addPost(Post post) {
        return false;
    }

    public Post getPost(int id) {
        return null;
    }
    public boolean removePost(int id) {
        return false;
    }

    public ArrayList<Post> getReplies(int post_id) {
        return null;
    }
    public ArrayList<Post> getAllPosts(int limit) {
        return null;
    }

    public static void main(String[] args){

    }
}
