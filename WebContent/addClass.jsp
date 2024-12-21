<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>新規授業登録</title>
</head>
<body>
	<h1>新しい授業の登録</h1>

	<%
	if (request.getAttribute("error") != null) {
	%>
	<p style="color: red;"><%=request.getAttribute("error")%></p>
	<%
	}
	%>

	<form action="addClass" method="post">
		<table>
			<tr>
				<td><label for="className">授業名:</label></td>
				<td><input type="text" id="className" name="className" required></td>
			</tr>
			<tr>
				<td><label for="year">年度:</label></td>
				<td><input type="text" id="year" name="year" required></td>
			</tr>
			<tr>
				<td><label for="semester">前期/後期:</label></td>
				<td><select id="semester" name="semester">
						<option value="前期">前期</option>
						<option value="後期">後期</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="day">曜日:</label></td>
				<td><select id="day" name="day">
						<option value="月">月</option>
						<option value="火">火</option>
						<option value="水">水</option>
						<option value="木">木</option>
						<option value="金">金</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="period">時限:</label></td>
				<td><input type="number" id="period" name="period" min="1"
					max="7" required></td>
			</tr>
			<tr>
				<td><label for="instructor">教授名:</label></td>
				<td><input type="text" id="instructor" name="instructor"
					required></td>
			</tr>
			<tr>
				<td><label for="description">概要:</label></td>
				<td><textarea id="description" name="description" rows="4"
						cols="40"></textarea></td>
			</tr>
		</table>

		<button type="submit">登録</button>
	</form>

	<br>
	<a href="classView.jsp">クラス一覧に戻る</a>
</body>
</html>
