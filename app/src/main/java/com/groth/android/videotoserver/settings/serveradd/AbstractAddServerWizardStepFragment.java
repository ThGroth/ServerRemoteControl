package com.groth.android.videotoserver.settings.serveradd;


import android.content.Context;

import androidx.annotation.Nullable;

import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.settings.AbstractConnectionConfigChangeFragment;


public abstract class AbstractAddServerWizardStepFragment
        extends AbstractConnectionConfigChangeFragment {
    private Context context;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void unbindStep() {
        // If there is no View any more, we cannot unbind.
        if (getView() == null) {
            return;
        }
        // Step specific implementation here
        unbind(getCurrentConnectionConfig().orElse(getNewConnectionConfig()));
        if (getOnConnectionChangeListener() != null);
        {
            getOnConnectionChangeListener().
                    onCurrentConnectionConfigChange(getCurrentConnectionConfig().get(),false);
        }
    }

    abstract public  String getPageTitle();
    protected abstract void unbind(ConnectionConfig connectionConfig);
    abstract boolean isValid();



}
