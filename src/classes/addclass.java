package classes;

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

@WebServlet("/addClass")
public class addclass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS対応ヘッダー
        response.setHeader("Access-Control-Allow-Origin", "*"); // Reactのオリジン
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
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
        String className = jsonRequest.getString("className");
        int year = jsonRequest.getInt("year");
        String semester = jsonRequest.getString("semester");
        String day = jsonRequest.getString("day");
        int period = jsonRequest.getInt("period");
        String instructor = jsonRequest.getString("instructor");
        String description = jsonRequest.getString("description");

        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            // SSH接続とデータベース接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // 授業を追加
            boolean isAdded = dbConnection.addClass(className, year, semester, day, period, instructor, description);

            JSONObject jsonResponse = new JSONObject();
            if (isAdded) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "授業が正常に追加されました");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "授業の追加に失敗しました");
            }

            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
        } finally {
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // プリフライトリクエストへの対応
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
