package com.groth.android.videotoserver.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groth.android.videotoserver.ConnectionConfig;
import com.groth.android.videotoserver.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerConfigPreferenceManager {
    private static final ServerConfigPreferenceManager ourInstance = new ServerConfigPreferenceManager();
    private final Gson gson;
    private static final String SERVER_LIST_KEY = "SERVER_LIST";
    private static final String CURRENT_SERVER = "CURRENT_SERVER";

    public static ServerConfigPreferenceManager getInstance() {
        return ourInstance;
    }


    private ServerConfigPreferenceManager() {
        gson = new Gson();
    }
    public List<ConnectionConfig> getServerConfigFromDB(Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( context );
        return getServerConfigFromDB(pref);
    }

    public Optional<ConnectionConfig> getServerConfigFromDB(String serverName , Context context)
    {
        return getServerConfigFromDB(context)
                .stream()//
                .filter(server -> serverName.equals(server.getServer().getName()))
                .findAny();
    }

    protected List<ConnectionConfig> getServerConfigFromDB(SharedPreferences pref )
    {
        String serverListStr = pref.getString( SERVER_LIST_KEY , "");
        // deserialize if not empty
        if (!serverListStr.isEmpty())
        {
            return gson.fromJson(serverListStr,
                    new TypeToken<ArrayList<ConnectionConfig>>() { }.getType() );
        }
        return new ArrayList<>();
    }

    public Optional<ConnectionConfig> getCurrentServerConfig( Context context )
    {
        return getCurrentServerConfig(PreferenceManager.getDefaultSharedPreferences( context ));
    }

    protected Optional<ConnectionConfig> getCurrentServerConfig(SharedPreferences pref )
    {
        String serverStr = pref.getString( CURRENT_SERVER , "");
        // deserialize if not empty
        if (!serverStr.isEmpty())
        {
            return Optional.of(gson.fromJson(serverStr,
                    new TypeToken<ConnectionConfig>() { }.getType() ));
        }
        return Optional.empty();
    }

    public void setCurrentServerConfig(ConnectionConfig config, Context context)
    {
        setCurrentServerConfig(config, PreferenceManager.getDefaultSharedPreferences( context ));
    }

    protected void setCurrentServerConfig(ConnectionConfig config, SharedPreferences pref )
    {
        pref.edit().putString( CURRENT_SERVER , convertServerToJson(config)).apply();
    }
    /*
     * Persists a single server on the database
     */
    public void addToStore(ConnectionConfig server, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( context );
        List<ConnectionConfig> serverList = getServerConfigFromDB( pref );
        serverList.add(server);
        store(serverList,pref);
    }

    public void store(@NonNull List<ConnectionConfig> servers, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( context );
        store(servers,pref);
    }

    public void store(@NonNull List<ConnectionConfig> servers, SharedPreferences pref) {
        pref.edit().putString(SERVER_LIST_KEY, gson.toJson(servers)).apply();
    }

    private String convertServerToJson(ConnectionConfig server)
    {
        return gson.toJson(server);
    }

    private Server convertToServer(String json)
    {
        return gson.fromJson(json, Server.class);
    }

}
