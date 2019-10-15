package com.groth.android.videotoserver.settings.serveradd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.settings.ServerConfigPreferenceManager;

public class AddServerWizardStepTest extends AbstractAddServerWizardStepFragment {

    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.TestConnection);
    }

    @Override
    protected void unbind(ConnectionConfig connectionConfig) {
        // TODO
    }

    @Override
    protected void bind(ConnectionConfig connectionConfig) {

    }

    @Override
    boolean isValid() {
        // TODO
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dataView = inflater.inflate(R.layout.add_server_test,
                container, false);

        Button addServerButton = dataView.findViewById(R.id.AddServerSaveServer);
        addServerButton.setOnClickListener((View view) -> saveNewServer() );

        return dataView;
    }

    private void saveNewServer()
    {
        getOnConnectionChangeListener().onConnectionConfigAdded(getCurrentConnectionConfig() );
    }
}

