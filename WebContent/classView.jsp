<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="connection.ClassInfo" %>
<html>
<head>
<meta charset="UTF-8">
    <title>授業情報</title>
</head>
<body>
    <h2>授業情報一覧</h2>
    
   <%
    List<ClassInfo> classList = (List<ClassInfo>) session.getAttribute("classList");
    if (classList != null && !classList.isEmpty()) {
%>
        <table border="1">
            <tr>
                <th>授業ID</th>
                <th>授業名</th>
                <th>年度</th>
                <th>前期後期</th>
                <th>曜日</th>
                <th>時限</th>
                <th>教授名</th>
                <th>概要</th>
            </tr>
            <%
                for (ClassInfo classInfo : classList) {
            %>
            <tr>
                <td><%= classInfo.get授業id() %></td>
                <td><%= classInfo.get授業名() %></td>
                <td><%= classInfo.get年度() %></td>
                <td><%= classInfo.get前期後期() %></td>
                <td><%= classInfo.get曜日() %></td>
                <td><%= classInfo.get時限() %></td>
                <td><%= classInfo.get教授名() %></td>
                <td><%= classInfo.get概要() %></td>
            </tr>
            <%
                }
            %>
        </table>
<%
    } else {
%>
    <p>授業情報はありません。</p>
<%
    }
%>

</body>
</html>
