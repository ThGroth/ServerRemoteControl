package com.groth.android.videotoserver.connection.ssh;

import com.groth.android.videotoserver.connection.ConnectionState;

public interface ConnectionCallbacks {
    void setupStatusPanel(ConnectionState state, String stateText);
    void displayErrorMessage(String msg);
    void successfullyConnected();
}
