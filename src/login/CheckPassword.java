package login;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connection.ClassInfo;
import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/CheckPassword")
public class CheckPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        connection.SSHConnection sshConnection = new SSHConnection();
        connection.DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // パスワードをMD5でハッシュ化
            String hashedPassword = hashMD5(password);

            // パスワードの検証
            boolean isValid = dbConnection.validatePassword(email, hashedPassword);

            if (isValid) {
                // ログイン成功
            	// 授業情報をセッションスコープに保存
            	List<ClassInfo> classList = dbConnection.getAllClasses();
            	HttpSession session = request.getSession();
            	session.setAttribute("classList", classList);

            	// リダイレクト
            	response.sendRedirect("classView.jsp");

                //response.sendRedirect("classView.jsp");
            } else {
                // ログイン失敗（再入力画面に戻る）
                request.setAttribute("error", "パスワードが間違っています");
                request.setAttribute("email", email); // メールアドレスを再度渡す
                request.getRequestDispatcher("passwordInput.jsp").forward(request, response);
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

    // MD5ハッシュ化メソッド
    private String hashMD5(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5アルゴリズムが見つかりませんでした", e);
        }
    }
}