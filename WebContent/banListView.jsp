<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="banlist.BanlistView" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Banlist</title>
</head>
<body>
    <h1>Banlist</h1>
    <table border="1">
        <thead>
            <tr>
                <th>バンID</th>
                <th>ユーザID</th>
                <th>ユーザ名</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<BanlistView> banlist = (List<BanlistView>) request.getAttribute("banlist");
                if (banlist != null && !banlist.isEmpty()) {
                    for (BanlistView item : banlist) {
            %>
            <tr>
                <td><%= item.getBanId() %></td>
                <td><%= item.getUserId() %></td>
                <td><%= item.getUserName() %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="3">現在、登録されているユーザーはいません。</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</body>
</html>
