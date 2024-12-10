package test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnection {
    private static final String SSH_HOST = "133.20.51.169";
    private static final String SSH_USER = "wsp";
    private static final String SSH_PASSWORD = "wsp$2000";
    private static final int SSH_PORT = 22;

    private static final int LOCAL_PORT = 5433; // ローカルポート
    private static final String REMOTE_HOST = "127.0.0.1"; // PostgreSQLのホスト
    private static final int REMOTE_PORT = 5432; // PostgreSQLのポート

    private Session session;

    // SSH接続を確立しトンネルを構築
    public void connect() throws JSchException {
        // 明示的にJSchライブラリを使用することを記述
        JSch jsch = new JSch();

        session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
        session.setPassword(SSH_PASSWORD);

        // ホストキーのチェックを無効化
        session.setConfig("StrictHostKeyChecking", "no");

        // 接続を確立
        session.connect();
        System.out.println("SSH接続成功");

        // トンネルを設定
        session.setPortForwardingL(LOCAL_PORT, REMOTE_HOST, REMOTE_PORT);
        System.out.println("トンネル構築完了: localhost:" + LOCAL_PORT + " -> " + REMOTE_HOST + ":" + REMOTE_PORT);
    }

    // SSH接続を切断
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            System.out.println("SSH接続を切断しました");
        }
    }

    public int getLocalPort() {
        return LOCAL_PORT;
    }
}
