package classes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/UpdateDislikeapi")
public class UpdateDislikeApi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORSプリフライトリクエストへの対応
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS ヘッダーを設定
        setCorsHeaders(response);

        // リクエストとレスポンスの文字エンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        int classId;
        try {
            // フロントエンドから送信された classId を取得
            classId = Integer.parseInt(request.getParameter("classId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid classId");
            return;
        }

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とデータベース接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // 「悪いね」の数を更新
            boolean isUpdated = dbConnection.updateDislikeCount(classId);

            if (isUpdated) {
                // 成功したらメッセージを返す
                response.getWriter().write("success");
            } else {
                // 更新失敗の場合の処理
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to update dislike count");
            }

        } catch (Exception e) {
            // サーバーエラーの処理
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            // 接続を閉じる
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // 必要に応じて特定のオリジンに変更
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 必要に応じて設定
    }
}
