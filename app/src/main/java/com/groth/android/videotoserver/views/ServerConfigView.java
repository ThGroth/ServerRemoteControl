package com.groth.android.videotoserver.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;

public class ServerConfigView extends LinearLayout {


    public ServerConfigView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.server_config_view, this, true);
    }

    public ServerConfigView(Context context) {
        this(context, null);
    }

    public void setServerConfig(ConnectionConfig config) {
        if (config==null || config.getServer()==null) {
            return;
        }
        // First child is just the label, so we skip this one and start with index 1
        getTextAtChild(1).setText(config.getServer().getName());

        String ipAndPort = config.getServer().getIp()+":"+config.getServer().getPort();
        getTextAtChild(2).setText(ipAndPort);

        getTextAtChild(3).setText( config.getUser() );

        String authMeth = config.isKeyBased() ? "Key" : "Password";
        getTextAtChild(4).setText( authMeth );
    }


    private TextView getTextAtChild(int index){
        LinearLayout lin = (LinearLayout) getChildAt(index);
        return (TextView) lin.getChildAt(1);
    }




}
