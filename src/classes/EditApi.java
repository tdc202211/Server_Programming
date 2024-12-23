package classes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/Editapi")
public class EditApi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダーを追加
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json; charset=UTF-8");

        // パラメータ取得と検証
        int classId;
        int year;
        int period;
        try {
        	request.setCharacterEncoding("UTF-8");
            classId = Integer.parseInt(request.getParameter("classId"));
            year = Integer.parseInt(request.getParameter("year"));
            period = Integer.parseInt(request.getParameter("period"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid number format for classId, year, or period\"}");
            return;
        }

        String className = request.getParameter("className");
        String term = request.getParameter("term");
        String day = request.getParameter("day");
        String professor = request.getParameter("professor");
        String description = request.getParameter("description");

        if (className == null || term == null || day == null || professor == null || description == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing required parameters\"}");
            return;
        }

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSHとデータベース接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // 授業情報を更新
            boolean isUpdated = dbConnection.updateClassInfo(classId, className, year, term, day, period, professor, description);

            if (isUpdated) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Class information updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to update class information\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Server error occurred: " + e.getMessage() + "\"}");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // CORSプリフライトリクエストへの対応
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
