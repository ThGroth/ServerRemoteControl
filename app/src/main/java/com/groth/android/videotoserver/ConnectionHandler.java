package com.groth.android.videotoserver;


import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;

public class ConnectionHandler extends Service
{
    private final IBinder binder = new ConnectionHandlerBinder();

    private ServerConnection connection;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void initNewConnection(ConnectionConfig connectionConfig, MainActivity statusActivity)
    {

        connection = new ServerConnectionSSHImpl(connectionConfig,statusActivity);
    }

    public class ConnectionHandlerBinder extends Binder {
        ConnectionHandler getService() {
            return ConnectionHandler.this;
        }
    }

    public ServerConnection getConnection() {
        return connection;
    }



}
