package com.groth.android.videotoserver.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.groth.android.videotoserver.R;

public class AddServerWizardStepGeneral extends AbstractAddServerWizardStepFragment {

    public AddServerWizardStepGeneral()
    {
        super();
    }

    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.generalServerInfo);
        //return getString(R.string.generalServerInfo);
    }

    @Override
    void unbind() {
        EditText serverName = ((EditText) getView().findViewById(R.id.AddServerName));
        getNewConnectionConfig().getServer().setName( serverName.getText().toString() );

        EditText ip = ((EditText) getView().findViewById(R.id.AddServerIP));
        getNewConnectionConfig().getServer().setIp( ip.getText().toString() );

        EditText display = ((EditText) getView().findViewById(R.id.AddServerDisplayID));
        getNewConnectionConfig().getServer().setXScreen( display.getText().toString() );

        EditText port = ((EditText) getView().findViewById(R.id.AddServerPort));
        getNewConnectionConfig().getServer().setPort( Integer.parseInt(port.getText().toString() ));
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
