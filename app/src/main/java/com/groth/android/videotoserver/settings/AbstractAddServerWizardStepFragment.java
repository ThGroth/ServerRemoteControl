package com.groth.android.videotoserver.settings;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.groth.android.videotoserver.ConnectionConfig;
import com.groth.android.videotoserver.ServerConnection;


public abstract class AbstractAddServerWizardStepFragment extends Fragment {

    private Context context;
    private ConnectionConfig newConnectionConfig;

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    public ConnectionConfig getNewConnectionConfig() {
        return newConnectionConfig;
    }

    public void setNewConnectionConfig(ConnectionConfig newConnectionConfig) {
        this.newConnectionConfig = newConnectionConfig;
    }

    abstract public  String getPageTitle();
    abstract void unbind();
    abstract boolean isValid();
}
