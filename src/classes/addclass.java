package classes;

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

@WebServlet("/addClass")
public class addclass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストとレスポンスの文字エンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
       
        String className = request.getParameter("className");
        int year = Integer.parseInt(request.getParameter("year"));
        String semester = request.getParameter("semester");
        String day = request.getParameter("day");
        int period = Integer.parseInt(request.getParameter("period"));
        String instructor = request.getParameter("instructor");
        String description = request.getParameter("description");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とデータベース接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // 授業を追加
            boolean isAdded = dbConnection.addClass(className, year, semester, day, period, instructor, description);

            if (isAdded) {
                // 成功したらリダイレクト
            	List<ClassInfo> classList = dbConnection.getAllClasses();
            	HttpSession session = request.getSession();
            	session.setAttribute("classList", classList);
                response.sendRedirect("classView.jsp");
            } else {
                // エラーの場合
                request.setAttribute("error", "授業の追加に失敗しました");
                request.getRequestDispatcher("addClass.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }
}
