package com.groth.android.videotoserver.views;

import android.content.Context;
import android.view.View;

import com.groth.android.videotoserver.connection.ConnectionHandler;
import com.groth.android.videotoserver.connection.ssh.ServerCommand;

import java.util.Collection;
import java.util.HashMap;

public class ButtonBar implements View.OnClickListener {
    private HashMap<Integer,ServerCommandButton> buttons;
    private ConnectionHandler serverConnectionService;

    public void initNewButton(Context context, int imageResource, ServerCommand serverCommand) {
        ServerCommandButton button = new ServerCommandButton(context, serverCommand);
        button.setImageResource(imageResource);
        button.setOnClickListener( this );
        button.setId(View.generateViewId());
        buttons.put(button.getId(), button);
    }

    public void setServerConnectionService(ConnectionHandler serverConnectionService) {
        this.serverConnectionService = serverConnectionService;
    }

    @Override
    public void onClick(View view) {
        if (buttons.containsKey(view.getId()))
        {
            ServerCommandButton button = buttons.get(view.getId());
            handleServerCommand( button.getCommand() );
        }
    }

    private void handleServerCommand(ServerCommand cmd) {
        if (validateServerConnection() )
        {
            serverConnectionService.getConnection().sendShellCommand(cmd);
        }
    }

    private boolean validateServerConnection() {
        return serverConnectionService != null && serverConnectionService.getConnection() != null;

    }

    public Collection<ServerCommandButton> getButtons() {
        return buttons.values();
    }
}
