package com.groth.android.videotoserver.settings.serveradd;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.settings.AbstractConnectionConfigChangeFragment;

import java.util.Optional;


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

    private void bindStep() {
        bind( getCurrentConnectionConfig() );
    }

    @Override
    public void onPause() {
        unbindStep();
        super.onPause();
    }

    @Override
    public void onResume() {
        bindStep();
        super.onResume();
    }

    public void unbindStep() {
        // If there is no View any more, we cannot unbind.
        if (getView() == null) {
            return;
        }
        // Step specific implementation here
        ConnectionConfig before = getCurrentConnectionConfig();
        unbind( getCurrentConnectionConfig() );
    }

    abstract public  String getPageTitle();
    protected abstract void unbind(ConnectionConfig connectionConfig);
    protected abstract void bind(ConnectionConfig connectionConfig);
    abstract boolean isValid();


}
