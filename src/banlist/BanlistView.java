package banlist;

public class BanlistView {
    private int banId;
    private String userId;
    private String userName;

    public BanlistView(int banId, String userId, String userName) {
        this.banId = banId;
        this.userId = userId;
        this.userName = userName;
    }

    public int getBanId() {
        return banId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
