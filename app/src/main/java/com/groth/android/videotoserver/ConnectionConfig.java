package com.groth.android.videotoserver;

public class ConnectionConfig {
    
    private Server server;
    private String user;
    private String password;
    private String privateKeyFile;
    /*
    Passphrase to access private Key
     */
    private String passphrase;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getHostIP() {
        return server.getIp();
    }


    public int getHostPort() {
        return server.getPort();
    }


    protected Boolean isKeyBased()
    {
        return privateKeyFile != null; //TODO!
    }

    @Override
    public String toString() {
        return server.toString();
    }

    public String getServerHostKey() {
        return server.getHostKey();
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPassphrase() {
        return passphrase;
    }
}
