package connection;

public class ClassInfo {
    private int 授業id;
    private String 授業名;
    private int 年度;
    private String 前期後期;
    private String 曜日;
    private int 時限;
    private String 教授名;
    private String 概要;

    public ClassInfo(int 授業id, String 授業名, int 年度, String 前期後期, String 曜日, int 時限, String 教授名, String 概要) {
        this.授業id = 授業id;
        this.授業名 = 授業名;
        this.年度 = 年度;
        this.前期後期 = 前期後期;
        this.曜日 = 曜日;
        this.時限 = 時限;
        this.教授名 = 教授名;
        this.概要 = 概要;
    }

    // Getters and Setters
    public int get授業id() {
        return 授業id;
    }

    public void set授業id(int 授業id) {
        this.授業id = 授業id;
    }

    public String get授業名() {
        return 授業名;
    }

    public void set授業名(String 授業名) {
        this.授業名 = 授業名;
    }

    public int get年度() {
        return 年度;
    }

    public void set年度(int 年度) {
        this.年度 = 年度;
    }

    public String get前期後期() {
        return 前期後期;
    }

    public void set前期後期(String 前期後期) {
        this.前期後期 = 前期後期;
    }

    public String get曜日() {
        return 曜日;
    }

    public void set曜日(String 曜日) {
        this.曜日 = 曜日;
    }

    public int get時限() {
        return 時限;
    }

    public void set時限(int 時限) {
        this.時限 = 時限;
    }

    public String get教授名() {
        return 教授名;
    }

    public void set教授名(String 教授名) {
        this.教授名 = 教授名;
    }

    public String get概要() {
        return 概要;
    }

    public void set概要(String 概要) {
        this.概要 = 概要;
    }
}
