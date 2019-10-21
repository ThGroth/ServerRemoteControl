package com.groth.android.videotoserver.connection.ssh;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.connection.ConnectionState;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ConnectionAsyncTask extends AsyncTask<Void , Void , Boolean> {

    private static final String TAG = ConnectionAsyncTask.class.getName();
    private Session sshSession;
    private String errorMsg = "";
    private final ServerConnectionSSHImpl callingConnectionImpl;

    private PipedOutputStream cmdStream;
    private final ConnectionCallbacks connectionCallbackManager;
    private final Context context;
    private final ConnectionConfig connectionConfig;

    private Channel channel;


    public ConnectionAsyncTask(ServerConnectionSSHImpl connectionImp,
                               final Context context,
                               final ConnectionCallbacks connectionCallbackManager,
                               final ConnectionConfig connectionConfig)
    {
        this.context = context;
        this.connectionCallbackManager = connectionCallbackManager;
        this.connectionConfig = connectionConfig;
        this.callingConnectionImpl = connectionImp;
    }


    /*
        During connect show some status indication.
         */
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        String text = context.getResources().getString(R.string.status_connecting);
        connectionCallbackManager.setupStatusPanel(ConnectionState.Connecting,
                String.format(text, connectionConfig.getServer().toString() ));
    }

    /*
    After trying to connect hide the progress bar and update the connection status.
     */
    @Override
    protected void onPostExecute(Boolean result)
    {
        super.onPostExecute(result);

        // and update the status text:
        if ( result.booleanValue() )
        {
            // register the channel and the session and start the worker thread.
            callingConnectionImpl.startConnectionThread(sshSession,channel,cmdStream);
            callingConnectionImpl.setIsConnected(ServerConnectionSSHImpl.ConnectionState.CONNECTED);
            String text = context.getResources().getString(R.string.status_connected);
            connectionCallbackManager.setupStatusPanel(ConnectionState.Connected,
                    String.format(text, connectionConfig.getServer().toString() ));
            connectionCallbackManager.successfullyConnected();
        }
        else {
            String text = context.getResources().getString(R.string.readyToConnect);
            connectionCallbackManager.setupStatusPanel(ConnectionState.ReadyToConnect,text );
            connectionCallbackManager.displayErrorMessage(errorMsg);
            callingConnectionImpl.setIsConnected(ServerConnectionSSHImpl.ConnectionState.FAILURE);
        }

    }



    @Override
    protected Boolean doInBackground(Void... args) // args not used
    {
        JSch jsch = new JSch();
        // Handle known hosts
        /*
        try
        {
            jsch.setKnownHosts(new ByteArrayInputStream(new byte[8192]));
            String hostKey = connectionConfig.getServerHostKey();
            jsch.getHostKeyRepository()
                .add(new HostKey(hostKey, Base64.getDecoder().decode(hostKey.getBytes())), null);
        } catch (JSchException e) {
            Log.w(TAG,"Failed to add host key.");
        } */
        /* temporary until hostKey management is implemented */
        jsch.setConfig("StrictHostKeyChecking", "no");
        if ( connectionConfig.isKeyBased() ) {
            try {
                jsch.addIdentity(connectionConfig.getPrivateKeyFile());
            } catch (JSchException e) {
                Log.w(TAG, "Adding identity failed.");
                Log.w(TAG, Log.getStackTraceString(e));
            }
        }

        try {
            sshSession=jsch.getSession(connectionConfig.getUser(),
                    connectionConfig.getHostIP(),
                    connectionConfig.getHostPort());
        } catch (JSchException e) {
            Log.w(TAG,"loading Session failed.");
            Log.w(TAG,Log.getStackTraceString(e));
            errorMsg = e.getLocalizedMessage();
            return Boolean.FALSE;
        }
        //Set password if not key file based
        if (!connectionConfig.isKeyBased())
        {
            sshSession.setPassword(connectionConfig.getPassword());
        }
        try{
            Log.d(TAG,"Connecting to " + connectionConfig);
            sshSession.connect();

            channel = sshSession.openChannel("shell");
            PipedInputStream in = new PipedInputStream();
            try {
                cmdStream = new PipedOutputStream(in);
            } catch (IOException e)
            {
                Log.e(TAG,"Error init comand stream",e);
            }
            channel.setInputStream(in);
            channel.connect(3000);
        }
        catch( JSchException e){
            Log.w(TAG,"Connection failed.");
            Log.w(TAG,Log.getStackTraceString(e));
            return Boolean.FALSE;
        }
        return Boolean.valueOf(sshSession.isConnected());
    }
}
