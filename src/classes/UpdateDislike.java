package classes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/UpdateDislike")
public class UpdateDislike extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストとレスポンスの文字エンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        int classId = Integer.parseInt(request.getParameter("classId"));

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
                // エラーの場合
                response.getWriter().write("error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }
}
