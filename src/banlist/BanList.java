package banlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BanList {
    private Connection connection;

    public BanList(Connection connection) {
        this.connection = connection;
    }

    public boolean isBanned(String email) {
        String query = "SELECT is_banned FROM public.users WHERE メールアドレス = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_banned");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addToBanList(String email) {
        String query = "UPDATE public.users SET is_banned = TRUE WHERE メールアドレス = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
