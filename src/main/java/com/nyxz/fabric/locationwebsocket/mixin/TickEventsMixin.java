package com.nyxz.fabric.locationwebsocket.mixin;

import com.nyxz.fabric.locationwebsocket.LocationWebSocket;
import com.nyxz.fabric.locationwebsocket.handler.ERRORS;
import com.nyxz.fabric.locationwebsocket.handler.ERRORS;
import com.nyxz.fabric.locationwebsocket.handler.Location;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class TickEventsMixin {

    /**
     * Initializes the Location handler and registers a tick event to send player locations via WebSocket.
     * @param info CallbackInfo provided by the mixin framework.
     */
	@Inject(at = @At("HEAD"), method = "loadLevel")
	private void init(CallbackInfo info) {
        try {
            LocationWebSocket.LOGGER.info("World initialized on server and ready to send tick events.");
            Location.server = (MinecraftServer) (Object) this;
        }
        catch (Exception e) {
            LocationWebSocket.LOGGER.error("Error during world initialization: " + e.getMessage());
        }
        ServerTickEvents.START_WORLD_TICK.register(client -> {
            try {
                Thread thread = new Thread(new Location());
                thread.start();
            }
            catch (Exception e) {
                if (LocationWebSocket.errors.contains(ERRORS.UNKNOWN_ERROR)) {
                    return;
                }
                LocationWebSocket.errors.add(ERRORS.UNKNOWN_ERROR);
                LocationWebSocket.LOGGER.error("Error during tick event: " + e.getMessage());
            }
        });
	}

}