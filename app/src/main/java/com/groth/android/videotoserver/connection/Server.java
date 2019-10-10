package com.groth.android.videotoserver.connection;

public class Server {
    private String name;
    private String ip;
    private String hostKey;
    private String xScreen = ":0"; // Unix Screen for content


    private int port;

    public Server(String name) {
        this.name = name;
    }

    public Server() {
        this("");
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXScreen() {
        return xScreen;
    }

    public void setXScreen(String xScreen) {
        this.xScreen = xScreen;
    }

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    @Override
    public String toString() {
        return name + " (" + ip + ")";
    }
}
