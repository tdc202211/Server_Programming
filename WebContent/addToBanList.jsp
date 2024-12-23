<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ユーザをバンリストに追加</title>
</head>
<body>
    <h1>ユーザをバンリストに追加</h1>
    <form action="AddToBanList" method="post">
        <label for="userId">ユーザID:</label>
        <input type="text" id="userId" name="userId" required><br>

        <button type="submit">追加</button>
    </form>

    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>
</body>
</html>
