package com.groth.android.videotoserver.settings.serveradd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;

public class AddServerWizardStepGeneral extends AbstractAddServerWizardStepFragment {

    public AddServerWizardStepGeneral()
    {
        super();
    }

    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.generalServerInfo);
    }

    @Override
    protected void unbind(ConnectionConfig connectionConfig) {
        EditText serverName =  getView().findViewById(R.id.AddServerName);
        connectionConfig.getServer().setName( serverName.getText().toString() );

        EditText ip = getView().findViewById(R.id.AddServerIP);
        connectionConfig.getServer().setIp( ip.getText().toString() );

        EditText display = getView().findViewById(R.id.AddServerDisplayID);
        connectionConfig.getServer().setXScreen( display.getText().toString() );

        EditText port = getView().findViewById(R.id.AddServerPort);
        connectionConfig.getServer().setPort( Integer.parseInt(port.getText().toString() ));
    }

    @Override
    boolean isValid() {
        // TODO
        return true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dataView = inflater.inflate(R.layout.add_server_general,
                container, false);

        return dataView;
    }
}
