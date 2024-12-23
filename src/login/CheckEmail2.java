package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/api/check-email")
public class CheckEmail2 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダーを追加
        setCorsHeaders(response);

        // リクエストとレスポンスの文字エンコーディングを設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // クライアントから送信されたメールアドレスを取得
        String email = request.getParameter("email");

        // 接続オブジェクトの初期化
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSHトンネルを確立し、データベースに接続
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // メールアドレスがデータベースに存在するか確認
            boolean exists = dbConnection.isEmailExists(email);

            // JSON形式でレスポンスを返却
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"exists\": " + exists + "}");

        } catch (Exception e) {
            // サーバーエラー時のエラーログとレスポンス
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"exists\": false, \"message\": \"サーバーエラーが発生しました\"}");
        } finally {
            // データベース接続とSSH接続を確実にクローズ
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダーを追加
        setCorsHeaders(response);

        // GETメソッドをサポートしない旨をレスポンス
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("{\"error\": \"GETメソッドはサポートされていません。POSTリクエストを使用してください。\"}");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORSプリフライトリクエストに対応
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * CORS対応ヘッダーを追加する共通メソッド
     * @param response HttpServletResponseオブジェクト
     */
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // 必要に応じて特定のオリジンを指定
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
