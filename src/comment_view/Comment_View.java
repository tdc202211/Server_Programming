package comment_view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Comment_View {
    private int commentId;
    private int classId;
    private String userId;
    private Integer parentComment;
    private Timestamp date;
    private String content;

    // デフォルトコンストラクタ
    public Comment_View() {}

    // 全フィールドを初期化するコンストラクタ
    public Comment_View(int commentId, int classId, String userId, Integer parentComment, Timestamp date, String content) {
        this.commentId = commentId;
        this.classId = classId;
        this.userId = userId;
        this.parentComment = parentComment;
        this.date = date;
        this.content = content;
    }

    // ResultSetからComment_Viewを作成するファクトリメソッド
    public static Comment_View fromResultSet(ResultSet rs) throws SQLException {
        return new Comment_View(
            rs.getInt("コメントid"),
            rs.getInt("授業id"),
            rs.getString("ユーザid"),
            (Integer) rs.getObject("親コメント"), // NULL値を許容
            rs.getTimestamp("日付"),
            rs.getString("コメント本文")
        );
    }

    // ゲッター
    public int getCommentId() {
        return commentId;
    }

    public int getClassId() {
        return classId;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getParentComment() {
        return parentComment;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    // セッター
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setParentComment(Integer parentComment) {
        this.parentComment = parentComment;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // デバッグ用toStringメソッド
    @Override
    public String toString() {
        return "Comment_View{" +
                "commentId=" + commentId +
                ", classId=" + classId +
                ", userId='" + userId + '\'' +
                ", parentComment=" + parentComment +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}
