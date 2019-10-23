package com.groth.android.videotoserver.views;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageButton;

import com.groth.android.videotoserver.connection.ssh.ServerCommand;

public class ServerCommandButton extends AppCompatImageButton {

    private ServerCommand command;
    public static final float ALPHA_ENABLED = 1F;
    public static final float ALPHA_DISABLED = 0.15F;

    public ServerCommandButton(Context context) {
        super(context);
        command = ServerCommand.EMPTY;
    }

    public ServerCommandButton(Context context, ServerCommand command) {
        super(context);
        this.command = command;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? ALPHA_ENABLED : ALPHA_DISABLED);
    }


    public ServerCommand getCommand() {
        return command;
    }

    public void setCommand(ServerCommand command) {
        this.command = command;
    }
}
