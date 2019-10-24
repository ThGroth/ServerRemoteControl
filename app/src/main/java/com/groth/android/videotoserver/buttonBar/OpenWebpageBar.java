package com.groth.android.videotoserver.buttonBar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.preference.PreferenceManager;

import com.groth.android.videotoserver.ConnectionServiceListeners;
import com.groth.android.videotoserver.connection.ConnectionHandler;
import com.groth.android.videotoserver.connection.ssh.SSHServerCommands;

import java.util.HashSet;
import java.util.Set;

public class OpenWebpageBar implements View.OnClickListener, ConnectionServiceListeners, SSHServerCommands {

    public static final String PREFS_URL_HISTORY = "UrlHistory";
    private Context context;
    private Set<String> history;
    private AutoCompleteTextView urlInput;
    private ImageButton sendButton;
    private ConnectionHandler connectionHandler;

    public OpenWebpageBar(Context context) {
        this.context = context;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        history = settings.getStringSet(PREFS_URL_HISTORY, new HashSet<String>());
    }

    public void setSendButton(ImageButton sendButton) {
        this.sendButton = sendButton;
        if (connectionHandler == null) {
            sendButton.setEnabled(false);
            sendButton.setAlpha(ServerCommandButton.ALPHA_DISABLED);
        }
        sendButton.setOnClickListener(this);
    }

    public void setUrlInput(AutoCompleteTextView urlInput) {
        this.urlInput = urlInput;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1,
                history.toArray(new String[history.size()]));
    }

    private void addUrlToHistory(String url) {
        if (!history.contains(url)) {
            history.add(url);
        }
    }

    @Override
    public void onClick(View view) {
        // only if Button is registered
        if (sendButton != null && sendButton.getId() == view.getId()) {
            addUrlToHistory(getUrl());
            // only if connected
            if (connectionHandler != null && connectionHandler.getConnection() != null) {
                connectionHandler.getConnection().sendShellCommand(SEND_URL(getUrl()));
            }
        }
    }

    private String getUrl() {
        if (urlInput != null) {
            return urlInput.getText().toString();
        }
        return "";
    }

    @Override
    public void setServerConnectionService(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        sendButton.setEnabled(true);
        sendButton.setAlpha(ServerCommandButton.ALPHA_ENABLED);
    }

    @Override
    public void onServiceStopped() {
        savePrefs();
        sendButton.setEnabled(false);
        sendButton.setAlpha(ServerCommandButton.ALPHA_DISABLED);

    }

    private void savePrefs() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREFS_URL_HISTORY, history);

        editor.commit();
    }

}
