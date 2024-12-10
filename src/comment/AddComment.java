package comment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        int classId = Integer.parseInt(request.getParameter("classId"));
        String content = request.getParameter("content");
        String parentComment = request.getParameter("parentComment");
        Integer parentCommentId = (parentComment != null && !parentComment.isEmpty()) ? Integer.parseInt(parentComment) : null;

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とデータベース接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // コメントを追加
            boolean isAdded = dbConnection.addComment(classId, userId, parentCommentId, content);

            if (isAdded) {
                // 成功したらリダイレクト
                response.sendRedirect("commentView.jsp");
            } else {
                // エラーの場合
                request.setAttribute("error", "コメントの追加に失敗しました");
                request.getRequestDispatcher("addComment.jsp").forward(request, response);
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
