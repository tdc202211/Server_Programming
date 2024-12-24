package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import banlist.BanList;
import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/api/check-email")
public class CheckEmail2 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダーを設定
        setCorsHeaders(response);

        // リクエスト文字エンコーディングを設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // クライアントから送信されたメールアドレスを取得
        String email = request.getParameter("email");

        // 接続オブジェクトの初期化
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();
        BanList banList = null;

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());
            banList = new BanList(dbConnection.getConnection());

            // メールアドレスが存在するかと、バンリストに含まれているか確認
            boolean exists = dbConnection.isEmailExists(email);
            boolean isBanned = banList.isBanned(email);

            if (isBanned) {
                // メールアドレスがバンリストに含まれている場合
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"exists\": false, \"banned\": true, \"message\": \"このメールアドレスはバンされています。ログインできません。\"}");
            } else if (exists) {
                // メールアドレスが存在する場合
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"exists\": true, \"banned\": false, \"message\": \"メールアドレスが確認されました。\"}");
            } else {
                // 存在しない場合
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"exists\": false, \"banned\": false, \"message\": \"メールアドレスが見つかりません。\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"exists\": false, \"banned\": false, \"message\": \"サーバーエラーが発生しました。\"}");
        } finally {
            // 接続を閉じる
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
        response.setHeader("Access-Control-Allow-Origin", "*"); // React.jsのオリジン
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
