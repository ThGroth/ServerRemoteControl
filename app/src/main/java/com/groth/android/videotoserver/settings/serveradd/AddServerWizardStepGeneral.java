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
    protected void bind(ConnectionConfig connectionConfig) {

        String serverName = connectionConfig.getServer().getName();
        EditText inputServerName =  getView().findViewById(R.id.AddServerName);
        inputServerName.setText( serverName == null ? "" : serverName );

        String ip = connectionConfig.getServer().getIp();
        EditText inputIp = getView().findViewById(R.id.AddServerIP);
        inputIp.setText( ip == null ? "" : ip );

        String display = connectionConfig.getServer().getXScreen();
        EditText inputDisplay = getView().findViewById(R.id.AddServerDisplayID);
        inputDisplay.setText( display == null ? "" : display );

        int port = connectionConfig.getServer().getPort();
        EditText inputPort = getView().findViewById(R.id.AddServerPort);
        inputPort.setText( String.valueOf(port) );
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
