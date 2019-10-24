package com.groth.android.videotoserver.buttonBar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.groth.android.videotoserver.R;

import java.util.HashMap;

import static com.groth.android.videotoserver.connection.ssh.SSHServerCommands.COMBI_MONITOR_START;
import static com.groth.android.videotoserver.connection.ssh.SSHServerCommands.COMBI_MONITOR_STOP;

public class ButtonBar extends ServerButtonCollection implements View.OnLongClickListener {

    private Context context;
    private ViewGroup container;

    public ButtonBar(Context context) {
        this.context = context;
    }

    public void setContainer(ViewGroup containerView) {
        container = containerView;
    }

    public void initButtonBar() {
        initNewButton(context, R.drawable.monitor_down_24dp, COMBI_MONITOR_START)
                .setOnLongClickListener(this);
        initNewButton(context, R.drawable.monitor_up_24dp, COMBI_MONITOR_STOP)
                .setOnLongClickListener(this);
        getButtons().forEach(container::addView);
    }


    @Override
    public boolean onLongClick(View view) {
        HashMap<Integer, ServerCommandButton> buttonMap = getButtonHashMap();
        if (buttonMap.containsKey(view.getId())) {
            showButtonChangeDialog(buttonMap.get(view.getId()));
        }
        return false;
    }

    private void showButtonChangeDialog(ServerCommandButton serverCommandButton) {
        ButtonChangeDialog dialog = new ButtonChangeDialog(context, serverCommandButton);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
