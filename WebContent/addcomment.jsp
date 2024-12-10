<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>コメントを追加</title>
</head>
<body>
    <h1>コメントを追加</h1>
    <form action="AddComment" method="post">
        <label for="classId">授業ID:</label>
        <input type="number" id="classId" name="classId" required><br><br>

        <label for="userId">ユーザーID:</label>
        <input type="text" id="userId" name="userId" maxlength="10" required><br><br>

        <label for="parentComment">親コメントID (任意):</label>
        <input type="number" id="parentComment" name="parentComment"><br><br>

        <label for="content">コメント本文:</label><br>
        <textarea id="content" name="content" rows="5" cols="50" required></textarea><br><br>

        <button type="submit">送信</button>
    </form>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <%
        }
    %>
</body>
</html>
