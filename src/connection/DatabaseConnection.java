package connection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import comment.Comment_View;


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

    // メールアドレスの存在確認
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

    // パスワードの検証
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

	// コメントの追加
	public boolean addComment(int classId, String userId, Integer parentCommentId, String content) {
	    String query = "INSERT INTO comment (\"授業id\", \"ユーザid\", \"親コメント\", \"コメント本文\") VALUES (?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, classId);
	        pstmt.setString(2, userId);
	        if (parentCommentId != null) {
	            pstmt.setInt(3, parentCommentId);
	        } else {
	            pstmt.setNull(3, java.sql.Types.INTEGER);
	        }
	        pstmt.setString(4, content);

	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0; // 挿入成功の場合はtrueを返す
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // 挿入失敗の場合はfalseを返す
	    }
	}

<<<<<<< HEAD
	
    // 新規ユーザー登録
    public boolean registerUser(String email, String password) {
        String hashedPassword = hashPassword(password);
        String query = "INSERT INTO users (\"メールアドレス\", \"パスワード\") VALUES (?, ?)";
=======
	// コメントの取得
	public List<Comment_View> getAllComments() {
	    String query = "SELECT * FROM comment";
	    List<Comment_View> comments = new ArrayList<>();

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            comments.add(Comment_View.fromResultSet(rs));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return comments;
	}



	// 新規ユーザー登録
	public boolean registerUser(String email, String password) {
		String hashedPassword = hashPassword(password);
		String query = "INSERT INTO users (\"メールアドレス\", \"パスワード\") VALUES (?, ?)";
>>>>>>> branch 'master' of https://github.com/tdc202211/Server_Programming.git

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);

<<<<<<< HEAD
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 登録成功の場合はtrueを返す
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 登録失敗の場合はfalseを返す
    }

    // パスワードのハッシュ化
=======
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0; // 登録成功の場合はtrueを返す
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // 登録失敗の場合はfalseを返す
	}
	
	// パスワードのハッシュ化
>>>>>>> branch 'master' of https://github.com/tdc202211/Server_Programming.git
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] hashedBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
