package com.groth.android.videotoserver.settings.serveradd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;

public class AddServerWizardStepPrivateKey extends AbstractAddServerWizardStepFragment {

    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.KnownHosts);
    }

    @Override
    protected void unbind(ConnectionConfig connectionConfig) {
        // TODO
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
