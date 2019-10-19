package com.groth.android.videotoserver.connection.ssh;

import android.util.Log;
import android.widget.TextView;

import com.groth.android.videotoserver.MainActivity;
import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ConnectionConfig;
import com.groth.android.videotoserver.connection.MouseClicks;
import com.groth.android.videotoserver.connection.ServerConnection;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


public class ServerConnectionSSHImpl implements ServerConnection, SSHServerCommands {

    private static final String TAG = ServerConnectionSSHImpl.class.getName();
    private final MainActivity statusActivity;
    private ConnectionThread connectionThread;

    private BufferedWriter cmdOutputStream;
    private PipedInputStream cmdInputStream;
    private ConnectionState connectionState = ConnectionState.UNCONNECTED;

    private ConnectionConfig connectionConfig = null;

    public ServerConnectionSSHImpl(final ConnectionConfig connectionConfig, final MainActivity statusActivity)
    {
        this.connectionConfig = connectionConfig;
        this.statusActivity = statusActivity;
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        cmdOutputStream = new BufferedWriter(new OutputStreamWriter(pipedOutputStream));
        try {
            cmdInputStream = new PipedInputStream(pipedOutputStream);
        } catch (IOException e) {
            Log.e(TAG,"Error initializing command output stream",e);
        }
    }

    public void connect()
    {
        ConnectionAsyncTask connectionTask = new ConnectionAsyncTask(this,
            statusActivity,
            connectionConfig);
        connectionTask.execute();
        // The connection flag will be set by the Post Method of the AsyncTask.
    }

    public void startConnectionThread(Session session, Channel channel, PipedOutputStream sshCmdStream)
    {
        connectionThread = new ConnectionThread(session, channel, cmdInputStream, sshCmdStream);
        connectionThread.start();

    }

    @Override
    public void disconnect()
    {
        if (connectionThread != null)
        {
            connectionThread.interrupt();
        }
        TextView statusText = statusActivity.findViewById( R.id.StatusText );
        statusText.setText( R.string.status_not_connected );
        connectionState = ConnectionState.UNCONNECTED;
    }

    @Override
    public ConnectionState getConnectionState()
    {
        if (connectionState.equals(ConnectionState.CONNECTED)  && cmdInputStream == null )
        {
            return ConnectionState.UNCONNECTED;
        }
        else{
            return connectionState;
        }

    }

    @Override
    public void sendShellCommand(ServerCommand cmd)
    {
        sendShellCommand(cmd.getCommand(),cmd.getWithGui());
    }

    public void sendShellCommand(String cmd, boolean withGUI) {
        if (withGUI)
        {
            cmd = modifyCmdForDisplay(cmd);
        }
        if (connectionState.equals(ConnectionState.CONNECTED))
        {
            try {
                cmdOutputStream.write(cmd);
                cmdOutputStream.newLine();
                cmdOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG,"error writing to output stream",e);
            }
        }
        else
        {
            Log.w(TAG,"Could not send Command due to missing connection.");
        }
    }

    private String modifyCmdForDisplay(String cmd) {
        if (!cmd.startsWith("DISPLAY="))
        {
            cmd = "DISPLAY="+connectionConfig.getServer().getXScreen()+" "+cmd;
        }
        return cmd;
    }


    @Override
    public void mouseMove(double dx, double dy) {

        sendShellCommand(MOUSE_MOVE_RELATIVE(dx,dy) );
    }

    @Override
    public void mouseScroll(int dx, int dy) {

    }

    @Override
    public void mouseClick(MouseClicks button) {
        sendShellCommand(MOUSE_CLICK_LEFT);
    }

    public void setIsConnected(ConnectionState state) {
        connectionState = state;
    }


    private class ConnectionThread extends Thread
    {
        private Session session = null;
        private Channel channel= null;
        private BufferedWriter sshWriter = null;
        private BufferedReader inputReader;
        private PipedOutputStream sshCmdStream;
        public ConnectionThread(Session session, Channel channel, InputStream inputStream, PipedOutputStream sshCmdStream)
        {
            super();

            this.session = session;
            this.channel = channel;
            this.inputReader = new BufferedReader(new InputStreamReader(inputStream));
            this.sshCmdStream = sshCmdStream;
        }

        @Override
        public void run() {
            String receivedCmd = null;
            try {
                while ((receivedCmd = inputReader.readLine()) != null)
                {
                    Log.d(TAG,"read: "+receivedCmd);
                    sendShellCommand(receivedCmd);
                    if ( Thread.currentThread().isInterrupted() )
                    {
                        Log.d(TAG,"connection Thread is interrupted!");
                        disconnect();
                        return;
                    }
                }
                Log.d(TAG,"end of thread!");
            }
            catch (IOException e)
            {
                Log.e("tag","error",e);
            }
        }

        public void sendShellCommand(String cmd) {
            if (reconnectAndCheckConnection() )
            {
                try {
                    cmd += "\r\n";
                    sshCmdStream.write(cmd.getBytes());
                    sshCmdStream.flush();

                } catch (IOException e) {
                    Log.e(TAG,"error sending cmd"+cmd,e);
                }
            }


        }

        private BufferedWriter getWritter() {
            if (sshWriter == null)
            {
                try {
                    sshWriter = new BufferedWriter(new OutputStreamWriter(channel.getOutputStream()));
                }
                catch (IOException e) {
                    Log.e(TAG, "error getting output stream for ssh", e);
                }
            }
            return sshWriter;
        }

        private boolean reconnectAndCheckConnection() {
            if (session != null)
            {
                try {
                    if (!session.isConnected()) {
                        session.connect();
                    }
                    if (!channel.isConnected()) {
                        channel.connect();
                    }
                    return true;
                } catch (JSchException e) {
                    Log.e(TAG,"error reconnecting to ssh session",e);
                    return false;
                }
            }
            return false;
        }


        public void disconnect() {
            if (channel.isConnected() )
            {
                channel.disconnect();
            }
            if ( session.isConnected() )
            {
                session.disconnect();
            }
        }
    }



}
