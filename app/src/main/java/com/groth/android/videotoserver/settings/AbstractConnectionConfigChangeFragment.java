package com.groth.android.videotoserver.settings;


import android.content.Context;

import androidx.fragment.app.Fragment;

import com.groth.android.videotoserver.connection.ConnectionConfig;

import java.util.List;
import java.util.Optional;


public abstract class AbstractConnectionConfigChangeFragment extends Fragment {

    OnConnectionConfigChangeListener onConnectionChangeListener;

    public OnConnectionConfigChangeListener getOnConnectionChangeListener() {
        return onConnectionChangeListener;
    }

    public void setOnConnectionChangeListener(OnConnectionConfigChangeListener onConnectionChangeListener) {
        this.onConnectionChangeListener = onConnectionChangeListener;
    }

    public Optional<ConnectionConfig> getCurrentConnectionConfig() {
        return getOnConnectionChangeListener().getCurrentConnectionConfig();
    }

    public ConnectionConfig getNewConnectionConfig() {
        return getOnConnectionChangeListener().newCurrentConnectionConfig();
    }


    public List<ConnectionConfig> getConnectionConfigList() {
        return getOnConnectionChangeListener().getConnectionConfigList();
    }

    public interface OnConnectionConfigChangeListener {
        void onCurrentConnectionConfigChange(ConnectionConfig connectionConfig, boolean storeOnDB);
        ConnectionConfig newCurrentConnectionConfig();
        void onConnectionConfigAdded(ConnectionConfig connectionConfig);
        Optional<ConnectionConfig> getCurrentConnectionConfig();
        List<ConnectionConfig> getConnectionConfigList();
    }


}
