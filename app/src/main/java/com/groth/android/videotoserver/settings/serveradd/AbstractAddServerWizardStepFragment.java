package com.groth.android.videotoserver.settings.serveradd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.groth.android.videotoserver.connection.ConnectionConfig;


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

    public void unbindStep() {
        // If there is no View any more, we cannot unbind.
        if (getView() == null) {
            return;
        }
        // Step specific implementation here
        unbind();
    }

    abstract public  String getPageTitle();
    protected abstract void unbind();
    abstract boolean isValid();
}
