package login;

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

@WebServlet("/api/check-passwordapi")
public class CheckPasswordApi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // CORSヘッダーの設定

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonString = jsonBuilder.toString();
        JSONObject jsonRequest = new JSONObject(jsonString);
        String email = jsonRequest.getString("email");
        String password = jsonRequest.getString("password");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            String hashedPassword = hashMD5(password); // MD5を使用していますが、BCryptに切り替えることを推奨

            boolean isValid = dbConnection.validatePassword(email, hashedPassword);
            JSONObject jsonResponse = new JSONObject();

            if (isValid) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "ログイン成功");
                jsonResponse.put("redirect", "/classView.jsp");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "パスワードが間違っています");
            }

            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", "サーバーエラーが発生しました");
            response.getWriter().write(errorResponse.toString());
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // プリフライトリクエストに対応
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

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
