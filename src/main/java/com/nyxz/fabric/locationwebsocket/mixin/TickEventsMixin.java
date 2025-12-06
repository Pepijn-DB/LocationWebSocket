package com.nyxz.fabric.locationwebsocket.mixin;

import com.nyxz.fabric.locationwebsocket.LocationWebsocket;
import com.nyxz.fabric.locationwebsocket.annotations.POST;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class TickEventsMixin {
	@Inject(at = @At("HEAD"), method = "loadLevel")
	private void init(CallbackInfo info) {
        LocationWebsocket.LOGGER.info("World initialized on server and ready to send tick events.");
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            sendLocation();
        });
	}

    @Unique
    @POST
    private void sendLocation() {
        
    }
}