package classes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.ClassInfo;
import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/ClassView")
public class ClassView extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        connection.SSHConnection sshConnection = new SSHConnection();
        connection.DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続
            try {
                sshConnection.connect();
                System.out.println("SSH接続に成功しました");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException("SSH接続エラー", e);
            }

            // データベース接続
            try {
                dbConnection.connect(sshConnection.getLocalPort());
                System.out.println("データベース接続に成功しました");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("データベース接続エラー", e);
            }
            
            // データベースから授業情報を取得
            List<ClassInfo> classList = dbConnection.getAllClasses();
            
            // データが正常に取得できているか確認
            if (classList.isEmpty()) {
                System.out.println("授業情報は取得できませんでした。");
            } else {
                System.out.println("取得した授業情報の数: " + classList.size());
            }
            
            // リクエスト属性として渡す
            request.setAttribute("classList", classList);

            // リクエスト情報が正しくセットされているか確認
            System.out.println("授業情報リスト: " + classList);
            
            // JSPへフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("/classView.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
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
