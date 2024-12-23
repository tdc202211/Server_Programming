package banlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/api/banlistapi")
public class BanListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response); // CORSヘッダーの設定
        response.setContentType("application/json; charset=UTF-8");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();
        List<JSONObject> banlist = new ArrayList<>();
        Connection connection = null;

        try {
            // SSH接続を開始
            sshConnection.connect();
            writeLog("SSH connection established.");

            // データベース接続を開始
            dbConnection.connect(sshConnection.getLocalPort());
            writeLog("Database connection established.");
            connection = dbConnection.getConnection();

            // クエリ実行
            String query = "SELECT \"バンid\", \"ユーザid\" FROM banlist";
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JSONObject banEntry = new JSONObject();
                    banEntry.put("banId", rs.getInt("バンid"));
                    banEntry.put("userId", rs.getString("ユーザid"));
                    banlist.add(banEntry);
                }
                writeLog("Query executed successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースエラーが発生しました: " + e.getMessage());
            return;
        } finally {
            // リソースをクリーンアップ
            try {
                if (connection != null) connection.close();
                dbConnection.disconnect();
                sshConnection.disconnect();
                writeLog("Resources closed successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // JSONレスポンスを返却
        JSONArray jsonArray = new JSONArray(banlist);
        response.getWriter().write(jsonArray.toString());
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response); // プリフライトリクエスト用CORS設定
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // フロントエンドのオリジン
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private void writeLog(String message) {
        System.out.println("[BanListServlet] " + message);
    }
}