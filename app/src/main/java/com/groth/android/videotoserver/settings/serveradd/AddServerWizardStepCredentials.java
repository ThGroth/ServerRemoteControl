package com.groth.android.videotoserver.settings.serveradd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;

public class AddServerWizardStepCredentials extends AbstractAddServerWizardStepFragment {


    @Override
    public String getPageTitle() {
        return getContext().getString(R.string.Credentials);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dataView = inflater.inflate(R.layout.add_server_credentials,
                container, false);

        Switch passwordSwitch = dataView.findViewById(R.id.AddServerUsePassword);
        passwordSwitch.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) ->
            {
              if(b) showPasswordViewGroup(dataView); else showKeyViewGroup(dataView);
            } );

        Button generateKeyButton = dataView.findViewById(R.id.AddServerGenerateKeyButton);
        generateKeyButton.setOnClickListener((view)->{showGenerateKeyFragment();});
        return dataView;
    }



    @Override
    protected void unbind(ConnectionConfig connectionConfig) {
        EditText user = getView().findViewById(R.id.AddServerUsername);
        connectionConfig.setUser(user.getText().toString());

        Switch usePassword = getView().findViewById(R.id.AddServerUsePassword);
        if( usePassword.isChecked() ) {
            EditText password = getView().findViewById(R.id.AddServerPassword);
            connectionConfig.setPassword(password.getText().toString());
        }
        else {
            EditText passphrase = getView().findViewById(R.id.AddServerPassphrase);
            connectionConfig.setPassphrase(passphrase.getText().toString());

            EditText privateKey = getView().findViewById(R.id.AddServerPrivateKey);
            connectionConfig.setPrivateKeyFile(privateKey.getText().toString());
        }
    }

    @Override
    protected void bind(ConnectionConfig connectionConfig) {
        String user = connectionConfig.getUser();
        EditText inputUser = getView().findViewById(R.id.AddServerUsername);
        inputUser.setText( user == null ? "" : user );

        if (connectionConfig.isKeyBased()) {
            String passphrase = connectionConfig.getPassphrase();
            String privateKey = connectionConfig.getPrivateKeyFile();

            EditText inputPassphrase = getView().findViewById(R.id.AddServerPassphrase);
            inputPassphrase.setText(passphrase==null ? "" : passphrase);

            EditText inputPrivateKey = getView().findViewById(R.id.AddServerPrivateKey);
            inputPrivateKey.setText(privateKey == null ? "" : privateKey);

            ((Switch) getView().findViewById(R.id.AddServerUsePassword)).setChecked(false);
        }
        else {
            String password = connectionConfig.getPassword();

            EditText inputPassword = getView().findViewById(R.id.AddServerPassword);
            inputPassword.setText(password==null ? "" : password);

            ((Switch) getView().findViewById(R.id.AddServerUsePassword)).setChecked(true);
        }
    }

    @Override
    boolean isValid() {
        // TODO
        return true;
    }

    private void showPasswordViewGroup(View view)
    {
        view.findViewById(R.id.AddServerPasswordGroup).setVisibility(View.VISIBLE);
        view.findViewById(R.id.AddServerKeyGroup).setVisibility(View.INVISIBLE);
        view.invalidate();
    }

    private void showKeyViewGroup(View view)
    {
        view.findViewById(R.id.AddServerPasswordGroup).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.AddServerKeyGroup).setVisibility(View.VISIBLE);
        view.invalidate();
    }

    private void showGenerateKeyFragment() {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.MainSettingFragmentContainer, new GenerateKeyFragment())
                    .addToBackStack(null)
                    .commit();

    }
}

