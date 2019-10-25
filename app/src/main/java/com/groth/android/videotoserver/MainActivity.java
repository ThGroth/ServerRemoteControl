package com.groth.android.videotoserver;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.groth.android.videotoserver.buttonBar.ButtonBar;
import com.groth.android.videotoserver.buttonBar.OpenWebpageBar;
import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.connection.ConnectionHandler;
import com.groth.android.videotoserver.connection.ConnectionState;
import com.groth.android.videotoserver.connection.ServerConnection;
import com.groth.android.videotoserver.connection.ssh.ConnectionCallbacks;
import com.groth.android.videotoserver.settings.MainSettingsActivity;
import com.groth.android.videotoserver.settings.ServerConfigPreferenceManager;
import com.groth.android.videotoserver.views.touchfield.MouseButtonHandler;

import java.util.Optional;
import java.util.ServiceLoader;

public class MainActivity extends AppCompatActivity implements ServiceConnection, ConnectionCallbacks {

    private ConnectionHandler connectionHandler;
    private ButtonBar buttonBar;
    private OpenWebpageBar openPageBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initOpenPageBar();
        initButtonBar();
        // Mouse buttons have their own handler
        MouseButtonHandler buttonClickHandler = new MouseButtonHandler(this);
        registerMouseButtons(buttonClickHandler);

        checkPermissions();
        setupStatusPanel(ConnectionState.ServiceLoading,"");
        connectService();
    }

    private void initButtonBar() {
        buttonBar = new ButtonBar(this, getSupportFragmentManager());
        buttonBar.setContainer(findViewById(R.id.buttonBar));
        buttonBar.initButtonBar();
    }

    private void initOpenPageBar() {
        openPageBar = new OpenWebpageBar(this);
        openPageBar.setUrlInput(findViewById(R.id.inputUrl));
        openPageBar.setSendButton(findViewById(R.id.buttonGoto));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void registerMouseButtons(MouseButtonHandler buttonClickHandler) {
        findViewById(R.id.buttonGoto).setOnClickListener(buttonClickHandler);
        findViewById(R.id.leftMouseButton).setOnClickListener(buttonClickHandler);
        findViewById(R.id.middleMouseButton).setOnClickListener(buttonClickHandler);
        findViewById(R.id.rightMouseButton).setOnClickListener(buttonClickHandler);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        10);
            }
        } else {
            // Permission has already been granted
        }



    }

    @Override
    public void setupStatusPanel(ConnectionState readyToConnect, String statusText) {
        TextView statusTextView = findViewById( R.id.StatusText );
        ProgressBar progress = findViewById( R.id.StatusProgress);
        switch (readyToConnect) {
            case ServiceLoading:
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText(R.string.status_not_connected);
                progress.setVisibility(View.VISIBLE);
                break;
            case ReadyToConnect:
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText( statusText );
                progress.setVisibility(View.INVISIBLE);
                break;
            case Connecting:
                progress.setVisibility(View.VISIBLE);
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText(statusText);
                break;
            case Connected:
                progress.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(this, MainSettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_connect:
                setupConnection();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void connectService()
    {
        Intent service = new Intent(getApplicationContext(), ConnectionHandler.class);
        getApplicationContext().bindService( service, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (! (iBinder instanceof  ConnectionHandler.ConnectionHandlerBinder))
        {
            //TODO Error !
            return;
        }
        connectionHandler =
                ((ConnectionHandler.ConnectionHandlerBinder) iBinder).getService();
        setupStatusPanel(ConnectionState.ReadyToConnect,getString(R.string.readyToConnect));
    }

    private void setupConnection() {
        ServerConnection connection = connectionHandler.getConnection();
        if (connection != null)
        {
            connection.disconnect();
        }
        ServerConfigPreferenceManager manager = ServerConfigPreferenceManager.getInstance();
        Optional<ConnectionConfig> config = manager.getCurrentServerConfig(this);
        if ( config.isPresent() ) {
            connectionHandler.initNewConnection(config.get(), this, this);
            connection = connectionHandler.getConnection();
            if (connection.getConnectionState().equals(ServerConnection.ConnectionState.UNCONNECTED))
            {
                connection.connect();
            }
        }
        else {
            displayErrorMessage(getString(R.string.NoConnectionConfigAvailable));
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        // TODO toast or so
    }



    public void displayErrorMessage(String errorMsg) {
        Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void successfullyConnected() {
        ServiceLoader<ConnectionServiceListeners> loader = ServiceLoader.load(ConnectionServiceListeners.class);
        for (ConnectionServiceListeners implementation : loader) {
            implementation.setServerConnectionService(connectionHandler);
        }

    }
}
