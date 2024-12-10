package register;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // SSHとDBの接続を確立
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // メールアドレスが既に登録されていないかを確認
            boolean emailExists = dbConnection.isEmailExists(email);

            if (emailExists) {
                // メールアドレスがすでに存在する場合、エラーメッセージを表示
                request.setAttribute("error", "このメールアドレスはすでに登録されています");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                // メールアドレスが存在しない場合、新しいユーザーとして登録
                boolean registrationSuccess = dbConnection.registerUser(email, password);

                if (registrationSuccess) {
                    // 登録成功の場合、ログイン画面にリダイレクト
                    response.sendRedirect("emailInput.jsp");
                } else {
                    // 登録に失敗した場合、エラーメッセージを表示
                    request.setAttribute("error", "ユーザー登録に失敗しました");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
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
