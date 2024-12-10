package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import comment_view.Comment_View;

public class DatabaseConnection {
    private static final String DB_USER = "postgres"; // PostgreSQLのユーザー
    private static final String DB_PASSWORD = "pass"; // PostgreSQLのパスワード
    private Connection connection;

    // データベースに接続
    public void connect(int localPort) throws Exception {
        String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/postgres";
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
        System.out.println("PostgreSQLに接続成功");
    }

    // データベース接続を閉じる
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("PostgreSQL接続を切断しました");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
    public boolean isEmailExists(String email) {
        String query = "SELECT 1 FROM users WHERE \"メールアドレス\" = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 存在する場合はtrueを返す
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 存在しない場合はfalseを返す
    }
    
    public boolean validatePassword(String email, String hashedPassword) {
        String query = "SELECT 1 FROM users WHERE \"メールアドレス\" = ? AND \"パスワード\" = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // パスワードが一致する場合はtrueを返す
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // パスワードが一致しない場合はfalseを返す
    }
    
    public List<Comment_View> getComments() {
        String query = "SELECT \"コメントid\", \"授業id\", \"ユーザid\", \"親コメント\", \"日付\", \"コメント本文\" FROM comment";
        List<Comment_View> comments = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Comment_View comment = new Comment_View(
                        rs.getInt("コメントid"),
                        rs.getInt("授業id"),
                        rs.getString("ユーザid"),
                        (Integer) rs.getObject("親コメント"), // NULLを許容
                        rs.getTimestamp("日付"),
                        rs.getString("コメント本文")
                );
                comments.add(comment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }
}
