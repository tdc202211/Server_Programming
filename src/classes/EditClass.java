package classes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/EditClass")
public class EditClass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        int classId = Integer.parseInt(request.getParameter("classId"));
        String className = request.getParameter("className");
        int year = Integer.parseInt(request.getParameter("year"));
        String term = request.getParameter("term");
        String day = request.getParameter("day");
        int period = Integer.parseInt(request.getParameter("period"));
        String professor = request.getParameter("professor");
        String description = request.getParameter("description");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            boolean isUpdated = dbConnection.updateClassInfo(classId, className, year, term, day, period, professor, description);

            if (isUpdated) {
                response.sendRedirect("classesView.jsp"); // 編集成功時に全授業一覧ページへ
            } else {
                request.setAttribute("error", "授業情報の編集に失敗しました");
                request.getRequestDispatcher("editClass.jsp").forward(request, response);
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
