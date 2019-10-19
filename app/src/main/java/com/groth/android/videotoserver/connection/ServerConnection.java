package com.groth.android.videotoserver.connection;

import com.groth.android.videotoserver.connection.ssh.ServerCommand;

public interface ServerConnection {

    void connect();
    void disconnect();
    ConnectionState getConnectionState();

    void sendShellCommand(ServerCommand cmd);
    void mouseMove(double dx, double dy);
    void mouseScroll(int dx, int dy);
    void mouseClick(MouseClicks button);

    enum ConnectionState{UNCONNECTED,CONNECTED,FAILURE}

}
