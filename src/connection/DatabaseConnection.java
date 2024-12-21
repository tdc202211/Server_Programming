package connection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0; // 登録成功の場合はtrueを返す
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // 登録失敗の場合はfalseを返す
	}
	
	// 授業情報の取得
    public List<ClassInfo> getAllClasses() throws SQLException {
        List<ClassInfo> classList = new ArrayList<>();
        String sql = "SELECT 授業id, 授業名, 年度, 前期後期, 曜日, 時限, 教授名, 概要 FROM classes";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ClassInfo classInfo = new ClassInfo(
                        resultSet.getInt("授業id"),
                        resultSet.getString("授業名"),
                        resultSet.getInt("年度"),
                        resultSet.getString("前期後期"),
                        resultSet.getString("曜日"),
                        resultSet.getInt("時限"),
                        resultSet.getString("教授名"),
                        resultSet.getString("概要")
                );
                classList.add(classInfo);
            }
            System.out.println("取得した授業情報数: " + classList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing query: " + sql, e);
        }
        return classList;
    }

 // 授業の追加
    public boolean addClass(String className, int year, String semester, String day, int period, String instructor, String description) {
        String query = "INSERT INTO classes (\"授業名\", \"年度\", \"前期後期\", \"曜日\", \"時限\", \"教授名\", \"概要\") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Debug用: 値を出力して確認
            System.out.println("semester: " + semester);
            System.out.println("instructor: " + instructor);

            // 「前期後期」に値がない場合、デフォルト値を設定
            if (semester == null || semester.isEmpty()) {
                semester = "前期"; // 必要に応じてデフォルト値を変更
            }

            // 「教授名」に値がない場合、デフォルト値を設定
            if (instructor == null || instructor.isEmpty()) {
                instructor = "未定"; // 必要に応じてデフォルト値を変更
            }

            // パラメータ設定
            pstmt.setString(1, className);
            pstmt.setInt(2, year);
            pstmt.setString(3, semester);
            pstmt.setString(4, day);
            pstmt.setInt(5, period);
            pstmt.setString(6, instructor);
            pstmt.setString(7, description);

            // クエリ実行
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 挿入成功の場合は true を返す
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 挿入失敗の場合は false を返す
        }
    }

	
	// パスワードのハッシュ化
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