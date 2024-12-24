package classes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import connection.ClassInfo;
import connection.DatabaseConnection;
import connection.SSHConnection;

@WebServlet("/api/classesapi")
public class ClassViewApi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();

        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*"); // React.jsのオリジン
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            List<ClassInfo> classList = dbConnection.getAllClasses();

            JSONArray jsonArray = new JSONArray();
            for (ClassInfo classInfo : classList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", classInfo.get授業id());
                jsonObject.put("title", classInfo.get授業名());
                jsonObject.put("year", classInfo.get年度());
                jsonObject.put("semester", classInfo.get前期後期());
                jsonObject.put("day", classInfo.get曜日());
                jsonObject.put("period", classInfo.get時限());
                jsonObject.put("teacher", classInfo.get教授名());
                jsonObject.put("description", classInfo.get概要());
                jsonObject.put("badGoodCount", classInfo.get悪いね()); // 新たに追加
                jsonArray.put(jsonObject);
            }

            response.getWriter().write(jsonArray.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースエラーが発生しました");
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
