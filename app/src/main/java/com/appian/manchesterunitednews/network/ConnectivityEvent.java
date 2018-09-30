package com.appian.manchesterunitednews.network;

public class ConnectivityEvent {
    private final boolean isConnected;

    public ConnectivityEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
