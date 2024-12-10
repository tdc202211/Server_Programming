<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="test.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>コメント一覧</title>
</head>
<body>
    <h1>コメント一覧</h1>
    <%
        SSHConnection sshConnection = new SSHConnection();
        DatabaseConnection dbConnection = new DatabaseConnection();
        List<Comment> comments = new ArrayList<>();

        try {
            // SSH接続とDB接続を確立
            sshConnection.connect();
            dbConnection.connect(sshConnection.getLocalPort());

            // コメント内容を取得
            comments = dbConnection.getComments();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 接続を解放
            dbConnection.disconnect();
            sshConnection.disconnect();
        }
    %>

    <table border="1">
        <tr>
            <th>コメントID</th>
            <th>授業ID</th>
            <th>ユーザID</th>
            <th>親コメント</th>
            <th>日付</th>
            <th>コメント本文</th>
        </tr>
        <% for (Comment comment : comments) { %>
            <tr>
                <td><%= comment.getCommentId() %></td>
                <td><%= comment.getClassId() %></td>
                <td><%= comment.getUserId() %></td>
                <td><%= comment.getParentComment() == null ? "なし" : comment.getParentComment() %></td>
                <td><%= comment.getDate() %></td>
                <td><%= comment.getContent() %></td>
            </tr>
        <% } %>
    </table>
</body>
</html>
