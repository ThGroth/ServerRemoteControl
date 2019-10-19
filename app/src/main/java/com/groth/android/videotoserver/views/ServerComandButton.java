package com.groth.android.videotoserver.views;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageButton;

import com.groth.android.videotoserver.connection.ssh.ServerCommand;

class ServerCommandButton extends AppCompatImageButton {

    private ServerCommand command;

    public ServerCommandButton(Context context) {
        super(context);
        command = ServerCommand.EMPTY;
    }

    public ServerCommandButton(Context context, ServerCommand command) {
        super(context);
        this.command = command;
    }

    public ServerCommand getCommand() {
        return command;
    }

    public void setCommand(ServerCommand command) {
        this.command = command;
    }
}
