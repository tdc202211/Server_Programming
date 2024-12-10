package comment_view;

import java.sql.Timestamp;

public class Comment_View {
    private int commentId;
    private int classId;
    private String userId;
    private Integer parentComment; // 親コメントはnullを許容する
    private Timestamp date;
    private String content;

    // コンストラクタ
    public Comment_View(int commentId, int classId, String userId, Integer parentComment, Timestamp date, String content) {
        this.commentId = commentId;
        this.classId = classId;
        this.userId = userId;
        this.parentComment = parentComment;
        this.date = date;
        this.content = content;
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
}
