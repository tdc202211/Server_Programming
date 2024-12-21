package banlist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/AddToBanList")
public class AddToBanList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String userId = request.getParameter("userId"); // ユーザIDのみ取得
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // ユーザをバンリストに追加し、is_bannedをTRUEに設定
            boolean isBanned = dbConnection.addUserToBanList(userId);

            if (isBanned) {
                response.sendRedirect("banlistView.jsp"); // バンリスト表示ページ
            } else {
                request.setAttribute("error", "バンリストへの追加に失敗しました");
                request.getRequestDispatcher("addToBanList.jsp").forward(request, response);
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
