package banlist;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/BanList")
public class BanListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とデータベース接続の確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());
            Connection connection = dbConnection.getConnection();

            // バンリストを取得
            String query = "SELECT \"バンid\", \"ユーザid\" FROM banlist";
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                out.println("<html><body>");
                out.println("<h1>バンリスト</h1>");
                out.println("<table border='1'>");
                out.println("<tr><th>バンID</th><th>ユーザID</th></tr>");

                while (rs.next()) {
                    int banId = rs.getInt("バンid");
                    String userId = rs.getString("ユーザid");
                    out.println("<tr><td>" + banId + "</td><td>" + userId + "</td></tr>");
                }

                out.println("</table>");
                out.println("</body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }
}
