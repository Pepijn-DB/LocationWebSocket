package com.nyxz.fabric.locationwebsocket;

import com.nyxz.fabric.locationwebsocket.handler.ERRORS;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LocationWebSocket implements ModInitializer {
	public static final String MOD_ID = "LocationWebSocket";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Config CONFIG;

    public static List<ERRORS> errors = new ArrayList<com.nyxz.fabric.locationwebsocket.handler.ERRORS>();


    /**
     * This method is called when the mod is initialized.
     */
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Mod initialized!");


        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("LocationWebsocket_settings.json");
            LocationWebSocket.CONFIG = Config.load(configPath);
        } catch (Exception e) {
            LOGGER.error("Failed to load config: " + e.getMessage());
            LOGGER.error("Mod will not function correctly (or at all) without a valid config.");
        }

    }
}
