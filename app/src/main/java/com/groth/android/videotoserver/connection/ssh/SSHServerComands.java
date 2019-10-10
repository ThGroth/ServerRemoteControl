package com.groth.android.videotoserver.connection.ssh;

public interface SSHServerComands {
    String MOUSE_CLICK_LEFT = "xdotool click 1";
    String MOUSE_CLICK_RIGHT = "xdotool click 2";
    String MOUSE_MOVE_RELATIVE = "xdotool mousemove_relative %f %f";
    String COMBI_MONITOR_START = "filmab";
    String COMBI_MONITOR_STOP = "filmaus";

}
