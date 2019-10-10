package com.groth.android.videotoserver;


import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

public class ButtonHandler
        extends ContextWrapper
        implements View.OnClickListener, ServiceConnection, SSHServerComands {
    private ConnectionHandler serverConnectionService;

    public ButtonHandler(Context base) {
        super(base);
        connectService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGoto:
                handleServerCommand(COMBI_MONITOR_START);
                break;
            case R.id.leftMouseButton:
                handleMouseClick(MouseClicks.LEFT_CLICK);
                break;
            case R.id.middleMouseButton:
                handleMouseClick(MouseClicks.MIDDLE_CLICK);
                break;
            case R.id.rightMouseButton:
                handleMouseClick(MouseClicks.MIDDLE_CLICK);
                break;
        }

    }

    private void handleMouseClick(MouseClicks type) {
        if (validateServerConnection() )
        {
            serverConnectionService.getConnection().mouseClick(type);
        }
    }

    private void handleServerCommand(String cmd) {
        if (validateServerConnection() )
        {
            serverConnectionService.getConnection().sendShellCommand(cmd);
        }
    }

    private boolean validateServerConnection()
    {
        return serverConnectionService != null;
    }


    private void connectService()
    {
        Intent service = new Intent(this, ConnectionHandler.class);
        bindService(service, this, Context.BIND_IMPORTANT);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (! (iBinder instanceof  ConnectionHandler.ConnectionHandlerBinder))
        {
            //TODO Error !
            return;
        }
        serverConnectionService = ((ConnectionHandler.ConnectionHandlerBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        serverConnectionService = null;
    }



}
