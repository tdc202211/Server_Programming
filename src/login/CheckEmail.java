package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/CheckEmail")
public class CheckEmail extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        connection.SSHConnection sshConnection = new SSHConnection();
        connection.DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // メールアドレスの存在確認
            boolean exists = dbConnection.isEmailExists(email);

            if (exists) {
                // メールアドレスが存在する場合、パスワード入力画面へ遷移
                request.setAttribute("email", email); // 次の画面に渡す
                request.getRequestDispatcher("passwordInput.jsp").forward(request, response);
            } else {
                // 存在しない場合、エラーメッセージを表示
                request.setAttribute("error", "メールアドレスが見つかりません");
                request.getRequestDispatcher("emailInput.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            // 接続を閉じる
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }
}
