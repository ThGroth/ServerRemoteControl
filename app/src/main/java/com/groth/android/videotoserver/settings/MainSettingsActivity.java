package com.groth.android.videotoserver.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.connection.Server;

import java.util.ArrayList;
import java.util.List;


public class MainSettingsActivity extends AppCompatActivity
        implements AbstractConnectionConfigChangeFragment.OnConnectionConfigChangeListener {

    private ServerConfigPreferenceManager dbManager;
    private List<ConnectionConfig> serverList;
    private ConnectionConfig currentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dbManager = ServerConfigPreferenceManager.getInstance();
        serverList = dbManager.getServerConfigFromDB( this );

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.MainSettingFragmentContainer, new MainSettingsFragment(), "MainSettingFragment")
                .commit();
    }

    @Override
    public void onCurrentConnectionConfigChange(ConnectionConfig connectionConfig, boolean save) {
        this.currentConfig = connectionConfig;
        if (save)
        {
            dbManager.setCurrentServerConfig(currentConfig,this);
            dbManager.store(serverList, this);
        }
    }

    @Override
    public ConnectionConfig newCurrentConnectionConfig() {
        currentConfig = new ConnectionConfig();
        currentConfig.setServer(new Server() );
        return currentConfig;
    }

    @Override
    public void onConnectionConfigAdded(ConnectionConfig connectionConfig) {
        this.serverList.add(connectionConfig);
        onCurrentConnectionConfigChange(connectionConfig, true);
    }

    @Override
    public ConnectionConfig getCurrentConnectionConfig() {
        return currentConfig;
    }

    @Override
    public List<ConnectionConfig> getConnectionConfigList() {
        if (serverList==null){
            return new ArrayList<>();
        }
        return serverList;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof AbstractConnectionConfigChangeFragment) {
            ((AbstractConnectionConfigChangeFragment)fragment).setOnConnectionChangeListener(this);
        }
    }

}
