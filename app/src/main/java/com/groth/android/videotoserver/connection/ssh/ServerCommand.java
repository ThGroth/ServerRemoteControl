package com.groth.android.videotoserver.connection.ssh;

public class ServerCommand {

    public static ServerCommand EMPTY = new ServerCommand("",false);
    private String command;
    private Boolean withGui;
    private Object[] parameters;

    public ServerCommand(String command, Boolean withGui) {
        this.command = command;
        this.withGui = withGui;
    }

    public ServerCommand(String command, Boolean withGui, Object... params) {
        this.command = command;
        this.withGui = withGui;
        parameters = params;
    }

    public Boolean getWithGui() {
        return withGui;
    }

    public void setWithGui(Boolean withGui) {
        this.withGui = withGui;
    }

    public String getCommand() {
        if (parameters != null)
        {
            return String.format(command,parameters);
        }
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCommand(String command, Object... parameters) {
        this.command = command;
        this.parameters = parameters;
    }
}
