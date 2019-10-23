package com.groth.android.videotoserver.views;

import android.content.Context;
import android.view.ViewGroup;

import com.groth.android.videotoserver.R;

import static com.groth.android.videotoserver.connection.ssh.SSHServerCommands.COMBI_MONITOR_START;
import static com.groth.android.videotoserver.connection.ssh.SSHServerCommands.COMBI_MONITOR_STOP;

public class ButtonBar extends ServerButtonCollection {

    private Context context;
    private ViewGroup container;

    public ButtonBar(Context context) {
        this.context = context;
    }

    public void setContainer(ViewGroup containerView) {
        container = containerView;
    }

    public void initButtonBar() {
        initNewButton(context, R.drawable.monitor_down_24dp, COMBI_MONITOR_START);
        initNewButton(context, R.drawable.monitor_up_24dp, COMBI_MONITOR_STOP);
        getButtons().forEach(container::addView);
    }

}
