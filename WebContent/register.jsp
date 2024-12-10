<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新規登録</title>
</head>
<body>
<h1>新規ユーザー登録</h1>
<form action="Register" method="post">
<dl>
    <dt>メールアドレス</dt>
    <dd><input type="text" name="email" required /></dd>
    <dt>パスワード</dt>
    <dd><input type="password" name="password" required /></dd>
</dl>
<button type="submit" name="register">登録</button>
</form>

<!-- ログイン画面に戻るリンク -->
<a href="emailInput.jsp">ログイン画面に戻る</a>

</body>
</html>
