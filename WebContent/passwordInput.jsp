<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>パスワード入力</title>
</head>
<body>
    <h1>ログイン</h1>
    <p>メールアドレス: <%= request.getAttribute("email") %></p>
    <form action="CheckPassword" method="post">
        <input type="hidden" name="email" value="<%= request.getAttribute("email") %>">
        <label for="password">パスワード:</label>
        <input type="password" id="password" name="password" required>
        <button type="submit">ログイン</button>
    </form>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>
</body>
</html>
