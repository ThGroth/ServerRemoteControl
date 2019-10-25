package com.groth.android.videotoserver.buttonBar;

import android.content.Context;
import android.view.View;

import com.groth.android.videotoserver.ConnectionServiceListeners;
import com.groth.android.videotoserver.connection.ConnectionHandler;
import com.groth.android.videotoserver.connection.ssh.ServerCommand;

import java.util.Collection;
import java.util.HashMap;

public class ServerButtonCollection implements View.OnClickListener, ConnectionServiceListeners {
    private HashMap<Integer, ServerCommandButton> buttons = new HashMap<>();
    private ConnectionHandler serverConnectionService;

    public ServerCommandButton initNewButton(Context context, int imageResource, ServerCommand serverCommand) {
        ServerCommandButton button = new ServerCommandButton(context, serverCommand);
        button.setImageResource(imageResource);
        button.setOnClickListener(this);
        button.setId(View.generateViewId());
        if (serverConnectionService == null) {
            //         button.setEnabled(false);
        }
        buttons.put(button.getId(), button);
        return button;
    }

    public void registerButton(ServerCommandButton button) {
        buttons.put(button.getId(), button);
    }

    @Override
    public void setServerConnectionService(ConnectionHandler serverConnectionService) {
        this.serverConnectionService = serverConnectionService;
        getButtons().forEach(but -> but.setEnabled(true));
    }

    @Override
    public void onServiceStopped() {
        getButtons().forEach(but -> but.setEnabled(false));

    }

    @Override
    public void onClick(View view) {
        if (buttons.containsKey(view.getId())) {
            ServerCommandButton button = buttons.get(view.getId());
            handleServerCommand(button.getCommand());
        }
    }

    private void handleServerCommand(ServerCommand cmd) {
        if (validateServerConnection()) {
            serverConnectionService.getConnection().sendShellCommand(cmd);
        }
    }

    private boolean validateServerConnection() {
        return serverConnectionService != null && serverConnectionService.getConnection() != null;

    }

    public Collection<ServerCommandButton> getButtons() {
        return buttons.values();
    }

    public HashMap<Integer, ServerCommandButton> getButtonHashMap() {
        return buttons;
    }
}
