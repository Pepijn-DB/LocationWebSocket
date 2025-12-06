package com.nyxz.fabric.locationwebsocket;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StrictJsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;

public class Config {
    public static int WEBSOCKET_PORT = 8080;
    public static String WEBSOCKET_URL = "ws://localhost";

    public Config(int WEBSOCKET_PORT, String WEBSOCKET_URL) {
        Config.WEBSOCKET_PORT = WEBSOCKET_PORT;
        Config.WEBSOCKET_URL = WEBSOCKET_URL;
    }

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT
                    .fieldOf("unixSocketEnabled")
                    .forGetter(Config::setWEBSOCKET_PORT),
            Codec.STRING
                    .fieldOf("unixSocketPath")
                    .forGetter(Config::setWEBSOCKET_URL)
    ).apply(instance, Config::new));

    public int setWEBSOCKET_PORT() {
        return WEBSOCKET_PORT;
    }

    public String setWEBSOCKET_URL() {
        return WEBSOCKET_URL;
    }

    //
    private static final Config DEFAULT = new Config(8080, "localhost");

    /**
     * Load configuration from file, or create default if not present
     * @param configPath Path to config file
     * @return Loaded configuration
     */
    public static Config load(Path configPath) {
        File file = configPath.toFile();

        Config config = Config.DEFAULT;
        if(file.exists()) {
            try {
                DataResult<Config> result = Config.CODEC.parse(
                        JsonOps.INSTANCE,
                        StrictJsonParser.parse(Files.readString(configPath))
                );
                config = result.resultOrPartial(LocationWebsocket.LOGGER::error).orElseThrow();
            } catch (IOException e) {
                LocationWebsocket.LOGGER.error("Failed to load config file", e);
            }
        } else if(file.getParentFile().canWrite() || file.canWrite()) {
            DataResult<JsonElement> result = Config.CODEC.encodeStart(JsonOps.INSTANCE, Config.DEFAULT);
            JsonElement json = result.getOrThrow();
            try(Writer writer = new FileWriter(file)) {
                GSON.toJson(json, GSON.newJsonWriter(writer));
            } catch (IOException e) {
                LocationWebsocket.LOGGER.error("Failed to write default config file", e);
            }
        } else {
            LocationWebsocket.LOGGER.info("Config at {} is not writable. Using default config.", configPath);
        }

        return config;
    }
}
