package comment;


import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/api/comments")
public class Comment_ViewApi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // CORS対応ヘッダー
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json; charset=UTF-8");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // すべてのコメントを取得
            List<Comment_View> allComments = dbConnection.getAllComments();

            // JSON配列を作成
            JSONArray jsonArray = new JSONArray();
            for (Comment_View comment : allComments) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commentId", comment.getCommentId());
                jsonObject.put("classId", comment.getClassId());
                jsonObject.put("userId", comment.getUserId());
                jsonObject.put("parentComment", comment.getParentComment());
                jsonObject.put("date", comment.getDate().toString());
                jsonObject.put("content", comment.getContent());
                jsonArray.put(jsonObject);
            }

            // クライアントに返却
            response.getWriter().write(jsonArray.toString());
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
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
