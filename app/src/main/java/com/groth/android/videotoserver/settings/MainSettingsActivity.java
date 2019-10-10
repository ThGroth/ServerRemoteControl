package com.groth.android.videotoserver.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.groth.android.videotoserver.ConnectionConfig;
import com.groth.android.videotoserver.R;

import java.util.List;


public class MainSettingsActivity extends AppCompatActivity  {

    private ServerConfigPreferenceManager manager;
    private List<ConnectionConfig> serverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.MainSettingFragmentContainer, new MainSettingsFragment(), "MainSettingFragment")
                .commit();
    }
}
