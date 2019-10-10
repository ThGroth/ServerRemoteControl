package com.groth.android.videotoserver.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.groth.android.videotoserver.R;

public class AddServerWizardStepTest extends AbstractAddServerWizardStepFragment {

    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.TestConnection);
    }

    @Override
    void unbind() {
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
        View dataView = inflater.inflate(R.layout.add_server_test,
                container, false);

        Button addServerButton = dataView.findViewById(R.id.AddServerSaveServer);
        addServerButton.setOnClickListener((View view) -> saveNewServer() );

        return dataView;
    }

    private void saveNewServer()
    {
        ServerConfigPreferenceManager manager = ServerConfigPreferenceManager.getInstance();
        manager.addToStore(getNewConnectionConfig(), getContext());
        manager.setCurrentServerConfig(getNewConnectionConfig(), getContext());
    }
}

