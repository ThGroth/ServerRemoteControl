package com.groth.android.videotoserver;

import com.groth.android.videotoserver.connection.ConnectionHandler;

public interface ConnectionServiceListeners {
    void setServerConnectionService(ConnectionHandler connection);

    void onServiceStopped();
}
