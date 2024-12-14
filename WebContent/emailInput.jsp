<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>メールアドレス確認</title>
</head>
<body>
	<h1>ログイン</h1>
	<form action="CheckEmail" method="post">
		<label for="email">メールアドレス:</label> <input type="email" id="email"
			name="email" required>
		<button type="submit">次へ</button>
	</form>
	<% if (request.getAttribute("error") != null) { %>
	<p style="color: red;"><%= request.getAttribute("error") %></p>
	<% } %>

	<!-- 新規登録ボタンを追加 -->
	<form action="register.jsp" method="get">
		<button type="submit">新規登録</button>
	</form>

</body>
</html>
