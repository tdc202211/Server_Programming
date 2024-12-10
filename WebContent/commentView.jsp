<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, connection.DatabaseConnection, connection.SSHConnection, comment_view.Comment_View" %>
<!DOCTYPE html>
<html>
<head>
    <title>コメント一覧</title>
</head>
<body>
    <h1>コメント一覧</h1>
    <table border="1">
        <thead>
            <tr>
                <th>コメントID</th>
                <th>授業ID</th>
                <th>ユーザーID</th>
                <th>親コメント</th>
                <th>日付</th>
                <th>コメント本文</th>
            </tr>
        </thead>
        <tbody>
            <%
                SSHConnection sshConnection = new SSHConnection();
                DatabaseConnection dbConnection = new DatabaseConnection();

                try {
                    // SSH接続とDB接続を確立
                    sshConnection.connect();
                    dbConnection.connect(sshConnection.getLocalPort());

                    // コメントを取得
                    List<Comment_View> comments = dbConnection.getAllComments();

                    // コメントを表示
                    for (Comment_View comment : comments) {
            %>
                        <tr>
                            <td><%= comment.getCommentId() %></td>
                            <td><%= comment.getClassId() %></td>
                            <td><%= comment.getUserId() %></td>
                            <td><%= comment.getParentComment() %></td>
                            <td><%= comment.getDate() %></td>
                            <td><%= comment.getContent() %></td>
                        </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='6'>エラーが発生しました: " + e.getMessage() + "</td></tr>");
                    e.printStackTrace();
                } finally {
                    dbConnection.disconnect();
                    sshConnection.disconnect();
                }
            %>
        </tbody>
    </table>
</body>
</html>
