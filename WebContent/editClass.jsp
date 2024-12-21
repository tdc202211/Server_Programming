<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>授業情報の編集</title>
</head>
<body>
    <h1>授業情報の編集</h1>
    <form action="EditClass" method="post">
        <label for="classId">授業ID:</label>
        <input type="number" id="classId" name="classId" required><br>

        <label for="className">授業名:</label>
        <input type="text" id="className" name="className" required><br>

        <label for="year">年度:</label>
        <input type="number" id="year" name="year" required><br>

        <label for="term">前期後期:</label>
        <input type="text" id="term" name="term" required><br>

        <label for="day">曜日:</label>
        <input type="text" id="day" name="day" required><br>

        <label for="period">時限:</label>
        <input type="number" id="period" name="period" required><br>

        <label for="professor">教授名:</label>
        <input type="text" id="professor" name="professor" required><br>

        <label for="description">概要:</label>
        <textarea id="description" name="description" required></textarea><br>

        <button type="submit">更新</button>
    </form>

    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>
</body>
</html>
