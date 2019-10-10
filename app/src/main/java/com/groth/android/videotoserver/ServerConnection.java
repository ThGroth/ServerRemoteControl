package com.groth.android.videotoserver;

public interface ServerConnection {

    void connect();
    void disconnect();
    ConnectionState getConnectionState();

    void sendShellCommand(String cmd);
    void mouseMove(double dx, double dy);
    void mouseScroll(int dx, int dy);
    void mouseClick(MouseClicks button);

    enum ConnectionState{UNCONNECTED,CONNECTED,FAILURE}

}
