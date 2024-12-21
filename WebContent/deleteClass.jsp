<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>授業の削除</title>
</head>
<body>
    <h1>授業を削除</h1>
    <form action="DeleteClass" method="post">
        <label for="classId">授業ID:</label>
        <input type="number" id="classId" name="classId" required><br>

        <button type="submit">削除</button>
    </form>

    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>
</body>
</html>
