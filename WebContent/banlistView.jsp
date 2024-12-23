<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, connection.DatabaseConnection, connection.SSHConnection" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>バンリスト</title>
    <style>
        table {
            border-collapse: collapse;
            width: 50%;
            margin: 20px auto;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h1 style="text-align: center;">バンリスト</h1>
    <table>
        <tr>
            <th>バンID</th>
            <th>ユーザID</th>
        </tr>
        <%
            SSHConnection sshConnection = new SSHConnection();
            DatabaseConnection dbConnection = new DatabaseConnection();

            try {
                sshConnection.connect();
                dbConnection.connect(sshConnection.getLocalPort());
                Connection connection = dbConnection.getConnection();

                String query = "SELECT \"バンid\", \"ユーザid\" FROM banlist";
                try (PreparedStatement pstmt = connection.prepareStatement(query);
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        int banId = rs.getInt("バンid");
                        String userId = rs.getString("ユーザid");
        %>
        <tr>
            <td><%= banId %></td>
            <td><%= userId %></td>
        </tr>
        <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("<tr><td colspan='2'>エラーが発生しました</td></tr>");
            } finally {
                dbConnection.disconnect();
                sshConnection.disconnect();
            }
        %>
    </table>
</body>
</html>
