package ts.projekt.postDB;

import java.sql.*;

public class Database {
    public static Database bazaDanych = new Database();
    private static Connection conn;
    private PreparedStatement statement;
    private ResultSet result;

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
        statement = conn.prepareStatement("""
            SELECT *
            FROM information_schema.tables
            WHERE table_schema = 'posts' 
                AND table_name = 'posts'
            LIMIT 1;
        """);
        result = statement.executeQuery();
        return result.next();
    }

    private void createDb() throws SQLException {
        statement = conn.prepareStatement("""
                
                  
                """);
    }
    public static void main(String[] args){

    }
}
