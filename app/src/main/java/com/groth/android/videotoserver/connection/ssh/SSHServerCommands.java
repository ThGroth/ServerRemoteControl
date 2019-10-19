package com.groth.android.videotoserver.connection.ssh;

public interface SSHServerCommands {
    ServerCommand MOUSE_CLICK_LEFT = new ServerCommand("xdotool click 1",true);
    ServerCommand MOUSE_CLICK_RIGHT = new ServerCommand("xdotool click 2",true);
    ServerCommand COMBI_MONITOR_START =  new ServerCommand("filmab",false);
    ServerCommand COMBI_MONITOR_STOP =  new ServerCommand("filmaus",false);

    default ServerCommand MOUSE_MOVE_RELATIVE(double dx,double dy)
    {
        return new ServerCommand( "xdotool mousemove_relative %f %f" , true, dx ,dy);
    }

}
