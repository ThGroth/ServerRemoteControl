package com.groth.android.videotoserver.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.settings.serveradd.AddServerFragment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MainSettingsPrefFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        final ListPreference customListPref = findPreference("serverList" );
        setListPreferenceData(customListPref, getContext());
        customListPref.setOnPreferenceClickListener(
            (Preference preference) ->
             {
                setListPreferenceData(customListPref, getContext());
                return false;
             });

        customListPref.setTitle(R.string.availableServers);
        customListPref.setSummary(R.string.selectServers);
        customListPref.setPersistent(true);

        findPreference("addServer").setOnPreferenceClickListener(
                (Preference preference) ->
                    {
                        showAddServerDialog();
                        return false;
                    } );

        initCurrentServerInfo();
    }

    private void initCurrentServerInfo() {
        Preference currentServer = findPreference("selectedServerInfo");
        ServerConfigPreferenceManager manager = ServerConfigPreferenceManager.getInstance();
        Optional<ConnectionConfig> connectionConfig = manager.getCurrentServerConfig(getContext());
        if (connectionConfig.isPresent()) {
            currentServer.setTitle(connectionConfig.get().getServer().getName());
            currentServer.setSummary(connectionConfig.get().getServer().getIp());
        }
        else {
            currentServer.setTitle(R.string.NoServerConnected);
        }
    }

    protected static void setListPreferenceData(ListPreference lp, Context context) {
        List<ConnectionConfig> serverList = ServerConfigPreferenceManager.getInstance().getServerConfigFromDB(context);
        List<String> serverNames = serverList
                .stream()
                .map(ConnectionConfig::toString)
                .collect(Collectors.toList() );
        CharSequence[] entries = serverNames.toArray(new CharSequence[serverNames.size()]);
        CharSequence[] entryValues = entries;
        lp.setEntries(entries);
        lp.setDefaultValue("1");
        lp.setEntryValues(entryValues);
    }

    public void showAddServerDialog() {

        View v = getView().findViewById(android.R.id.content);

        ((ViewGroup) v.getParent()).removeView(v);
        AddServerFragment nextFrag= new AddServerFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();



    }

}
