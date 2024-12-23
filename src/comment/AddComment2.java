package comment;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/AddComment2")
public class AddComment2 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダー
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json; charset=UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonString = jsonBuilder.toString();
        JSONObject jsonRequest;
        try {
            jsonRequest = new JSONObject(jsonString);

            // 必須フィールドのチェック
            if (!jsonRequest.has("userId") || !jsonRequest.has("classId") || !jsonRequest.has("content")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必須フィールドが不足しています");
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "リクエストのJSON形式が不正です");
            return;
        }

        String userId = jsonRequest.getString("userId");
        int classId = jsonRequest.getInt("classId");
        String content = jsonRequest.getString("content");
        Integer parentCommentId = jsonRequest.optInt("parentComment", -1) == -1 ? null : jsonRequest.getInt("parentComment");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // コメントを追加
            boolean isAdded = dbConnection.addComment(classId, userId, parentCommentId, content);

            JSONObject jsonResponse = new JSONObject();
            if (isAdded) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "コメントが正常に追加されました");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "コメントの追加に失敗しました");
            }

            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // CORSプリフライトリクエストへの対応
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
