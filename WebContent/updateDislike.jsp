<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>授業IDで悪いねを増加</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // 「悪いね」ボタンがクリックされたときの処理
        function updateDislikeByInput() {
            const classId = $("#classIdInput").val(); // ユーザーが入力した授業IDを取得
            if (!classId) {
                alert("授業IDを入力してください。");
                return;
            }

            $.ajax({
                type: "POST",
                url: "UpdateDislike",
                data: { classId: classId },
                success: function(response) {
                    if (response === "success") {
                        alert("「悪いね」が1つ増えました");
                    } else {
                        alert("エラーが発生しました。もう一度お試しください。");
                    }
                },
                error: function() {
                    alert("サーバーとの通信中にエラーが発生しました。");
                }
            });
        }
    </script>
</head>
<body>
    <h1>悪いねを増加させる</h1>

    <!-- 授業IDを入力するフォーム -->
    <div>
        <label for="classIdInput">授業IDを入力してください:</label>
        <input type="text" id="classIdInput" name="classId" placeholder="例: 1">
        <button onclick="updateDislikeByInput()">悪いねを押す</button>
    </div>
</body>
</html>
