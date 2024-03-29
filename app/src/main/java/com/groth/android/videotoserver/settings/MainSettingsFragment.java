package com.groth.android.videotoserver.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.settings.serveradd.AddServerFragment;
import com.groth.android.videotoserver.views.ServerConfigView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MainSettingsFragment extends AbstractConnectionConfigChangeFragment
        implements AdapterView.OnItemSelectedListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.setting_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initServerList();
        initSelectedServer();
        initAddServerButton();
    }


    private void initServerList() {
        Spinner serverSelectSpinner = getView().findViewById(R.id.SettingMainSelectServer );
        /* get the list of servers from Preferences */

        List<String> serverNames = getConnectionConfigList()
                .stream()
                .map(ConnectionConfig::toString)
                .collect(Collectors.toList() );

        ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, serverNames);
        serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSelectSpinner.setAdapter(serverAdapter);
        serverSelectSpinner.setOnItemSelectedListener( this );

    }

    private void initAddServerButton() {
        Button serverAddButton = getView().findViewById(R.id.SettingMainAddServerButton );
        serverAddButton.setOnClickListener(v ->
        {
            AddServerFragment nextFrag = new AddServerFragment();
            getOnConnectionChangeListener().newCurrentConnectionConfig();
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainSettingFragmentContainer, nextFrag)
                .addToBackStack(null)
                .commit();
        });
    }

    private void initSelectedServer() {
        /* get Current server from preferences */
        initSelectedServer( getCurrentConnectionConfig() );
    }

    private void initSelectedServer( ConnectionConfig currentConfig) {
        ServerConfigView serverConfigView = getView().findViewById(R.id.SettingMainCurrentServer);
        if (currentConfig != null) {
            serverConfigView.setServerConfig( currentConfig );
        }
        else {
            serverConfigView.setNoServerConfig();
        }
    }

    private void setSelectedServer(String serverName)
    {
        Optional<ConnectionConfig> currentConfig = getConnectionConfigList().stream()
                .filter(conConf->serverName.equals(conConf.getServer().toString()) )
                .findAny();
        if (currentConfig.isPresent()) {
            getOnConnectionChangeListener().onCurrentConnectionConfigChange(currentConfig.get(),true);
            initSelectedServer(currentConfig.get());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        setSelectedServer(adapterView.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        setSelectedServer("");
    }


    }

