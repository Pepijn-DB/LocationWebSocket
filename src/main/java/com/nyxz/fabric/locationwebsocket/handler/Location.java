package com.nyxz.fabric.locationwebsocket.handler;

import com.nyxz.fabric.locationwebsocket.annotations.Socket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Unique;
import oshi.util.tuples.Triplet;
import oshi.util.tuples.Pair;

public class Location implements Runnable{
    public static MinecraftServer server;


    /**
     * Sends the current locations of all players on the server via WebSocket in JSON format.
     */
    @Unique
    @Socket
    public void run() {
        StringBuilder JSONString = new StringBuilder("{ \"players\": [");
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Triplet<Double, Double, Double> location = getPlayerLocation(player);
            Pair<Double, Double> rotation = getPlayerRotation(player);
            JSONString.append("{ \"uuid\": \"").append(player.getUUID()).append("\", \"location\":[").
                    append(location.getA()).append(",").append(location.getB()).append(", ").
                    append(location.getC()).append("], \"rotation\":[").append(rotation.getA()).append(",")
                    .append(rotation.getB()).append("]},");
        }
        if (JSONString.charAt(JSONString.length() - 1) == ',') {
            JSONString.deleteCharAt(JSONString.length() - 1);
        }
        JSONString.append("]}");
        WebSocket WebSocket = new WebSocket();
        WebSocket.sendMessage(JSONString.toString());

    }

    /** Gets the player's current location as a Triplet of doubles (x, y, z).
     *
     * @param player The player whose location is to be retrieved.
     * @return A Triplet containing the x, y, and z coordinates of the player's location.
     */
    public static Triplet<Double, Double, Double> getPlayerLocation(Player player) {
        BlockPos pos = player.blockPosition();
        return new Triplet<>(player.getX() + 0.0, player.getY() + 0.0, player.getZ() + 0.0);
    }

    public static Pair<Double, Double> getPlayerRotation(Player player) {
        Vec2 RotationVector = player.getRotationVector();
        return new Pair<>(RotationVector.x + 0.0, RotationVector.y + 0.0);
    }
}
