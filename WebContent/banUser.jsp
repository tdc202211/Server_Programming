<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="banlist.BanListInfo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>バンユーザ管理</title>
</head>
<body>
    <h1>バンユーザ管理</h1>
    <form method="POST" action="BanUser">
        <table border="1">
            <tr>
                <th>ユーザID</th>
                <th>メールアドレス</th>
                <th>バン操作</th>
            </tr>
            <%
                List<BanListInfo> userList = (List<BanListInfo>) request.getAttribute("userList");
                if (userList != null && !userList.isEmpty()) {
                    for (BanListInfo user : userList) {
            %>
                        <tr>
                            <td><%= user.getUserId() %></td>
                            <td><%= user.getEmail() %></td>
                            <td><button type="submit" name="userId" value="<%= user.getUserId() %>">バン</button></td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="3">表示するユーザがいません。</td>
                </tr>
            <%
                }
            %>
        </table>
    </form>
</body>
</html>
