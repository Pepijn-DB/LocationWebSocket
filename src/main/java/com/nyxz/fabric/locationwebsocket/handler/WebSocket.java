package com.nyxz.fabric.locationwebsocket.handler;

import com.nyxz.fabric.locationwebsocket.Config;
import com.nyxz.fabric.locationwebsocket.LocationWebSocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocket extends WebSocketClient {
    public WebSocket(URI serverUri) {
        super(serverUri);
    }

    public WebSocket(){
        super(URI.create("ws://" + Config.WEBSOCKET_URL + ":" + Config.WEBSOCKET_PORT));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        LocationWebSocket.LOGGER.info("WebSocket connection opened.");
        //No need for a logger here, creates a lot of spam. (only debug purpose)
    }

    @Override
    public void onMessage(String message) {
        // No action needed for incoming messages in this context
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
//        LocationWebSocket.LOGGER.info("WebSocket connection closed: {}", reason);
        //No need for a logger here, creates a lot of spam. (only debug purpose)
    }

    @Override
    public void onError(Exception e) {
        LocationWebSocket.LOGGER.error("WebSocket error. {}", e.toString());
    }

    public void sendMessage(String message) {
        try {
            if (!isOpen()) {
                connectBlocking();
            }
            send(message);
        } catch (Exception e) {
            LocationWebSocket.LOGGER.error("Failed to send WebSocket message.", e);
        }
    }

    public static boolean canConnect() {
        try {
            URI uri = new URI("ws://" + Config.WEBSOCKET_URL + ":" + Config.WEBSOCKET_PORT);
            WebSocket webSocket = new WebSocket(uri);
            webSocket.connectBlocking();
            webSocket.close();
            return true;
        } catch (Exception e) {
            LocationWebSocket.LOGGER.error("WebSocket connection test failed.", e);
            return false;
        }
    }
}
