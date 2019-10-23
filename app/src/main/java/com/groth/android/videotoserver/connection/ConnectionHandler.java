package com.groth.android.videotoserver.connection;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.groth.android.videotoserver.connection.ssh.ConnectionCallbacks;
import com.groth.android.videotoserver.connection.ssh.ServerConnectionSSHImpl;

public class ConnectionHandler extends Service
{
    private final IBinder binder = new ConnectionHandlerBinder();

    private ServerConnection connection;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void initNewConnection(ConnectionConfig connectionConfig, ConnectionCallbacks callbackManager,
                                  Context context)
    {

        connection = new ServerConnectionSSHImpl(connectionConfig, context, callbackManager);
    }

    public class ConnectionHandlerBinder extends Binder {
        public ConnectionHandler getService() {
            return ConnectionHandler.this;
        }
    }

    public ServerConnection getConnection() {
        return connection;
    }



}
