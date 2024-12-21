package banlist;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;

@WebServlet("/BanlistServlet")
public class BanlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        try {
            dbConnection.connect(new connection.SSHConnection().getLocalPort());
            List<BanlistView> banlist = dbConnection.getBanlist();
            request.setAttribute("banlist", banlist);
            request.getRequestDispatcher("banlistView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
        }
    }
}
