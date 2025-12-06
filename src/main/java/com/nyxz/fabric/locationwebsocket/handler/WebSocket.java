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
        if (LocationWebSocket.errors.contains(ERRORS.WEBSOCKET_NOT_CONNECTED)){
            return;
        }
        LocationWebSocket.errors.add(ERRORS.WEBSOCKET_NOT_CONNECTED);
        LocationWebSocket.LOGGER.error("WebSocket error. {}", e.toString());
    }

    public void sendMessage(String message) {
        try {
            if (!isOpen()) {
                connectBlocking();
            }
            send(message);
        } catch (Exception e) {
            if (LocationWebSocket.errors.contains(ERRORS.WEBSOCKET_NOT_CONNECTED)){
                return;
            }
            LocationWebSocket.errors.add(ERRORS.WEBSOCKET_NOT_CONNECTED);
            LocationWebSocket.LOGGER.error("Failed to send WebSocket message.", e);
        }
    }
}
