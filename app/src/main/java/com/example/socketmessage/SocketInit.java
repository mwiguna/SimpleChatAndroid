package com.example.socketmessage;

import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketInit {

    public String URLServer = "http://192.168.100.6:3000";
    public Socket mSocket;

    public SocketInit(){
        initSocket();
    }

    public void initSocket(){
        try {
            IO.Options options = new IO.Options();
            options.secure = true;
            options.transports = new String[]{WebSocket.NAME};
            options.reconnection = true;
            options.forceNew = true;

            mSocket = IO.socket(URLServer, options);
            mSocket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();
    }
}
