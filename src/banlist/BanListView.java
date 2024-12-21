package banlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/BanListView")
public class BanListView extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();
        List<BanListInfo> userList = new ArrayList<>();

        try {
            // SSHとDB接続
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());
            System.out.println("データベース接続に成功しました");

            // ユーザリストを取得
            userList = dbConnection.getActiveUsers();
            System.out.println("取得したユーザリスト: " + userList);

            // JSPにリストを渡す
            request.setAttribute("userList", userList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/banUser.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }
}
