package com.nyxz.fabric.locationwebsocket;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyxz.fabric.locationwebsocket.handler.ERRORS;
import net.minecraft.util.StrictJsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;

public class Config {
    public static int webSocketPort = 8080;
    public static String webSocketUrl = "127.0.0.1";

    public static boolean enableMod = true;


    public static File errorFilePath;
    public static Writer errorFile;

    /**
     * Constructor for the Config file
     * @param webSocketPort The port for the WebSocket server
     * @param webSocketUrl The URL for the WebSocket server
     */
    public Config(int webSocketPort, String webSocketUrl, boolean enableMod) {
        Config.enableMod = enableMod;
        Config.webSocketPort = webSocketPort;
        Config.webSocketUrl = webSocketUrl;
    }

    public Config(int webSocketPort, String webSocketUrl, boolean enableMod, File errorFile) throws IOException {
        Config.enableMod = enableMod;
        Config.webSocketPort = webSocketPort;
        Config.webSocketUrl = webSocketUrl;
        Config.errorFile = new FileWriter(errorFile);
        Config.errorFilePath = errorFile;

        try{
            Files.newByteChannel(errorFilePath.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).close();
        }
        catch (IOException e){
            LocationWebSocket.LOGGER.error("Failed to create error log file", e);
        }
    }

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT
                    .fieldOf("websocketPort")
                    .forGetter(Config::getWebsocketPort),
            Codec.STRING
                    .fieldOf("websocketURL")
                    .forGetter(Config::getWebsocketUrl),
            Codec.BOOL
                    .fieldOf("enableMod")
                    .forGetter(Config::getEnabled)
    ).apply(instance, Config::new));


    public int getWebsocketPort() {
        return webSocketPort;
    }

    public String getWebsocketUrl() {
        return webSocketUrl;
    }

    public boolean getEnabled() {
        return enableMod;
    }

    public Writer getErrorFile() {
        return errorFile;
    }

    //
    private static final Config DEFAULT = new Config(8080, "localhost", true);

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
                config = result.resultOrPartial(LocationWebSocket.LOGGER::error).orElseThrow();
            } catch (IOException e) {
                LocationWebSocket.LOGGER.error("Failed to load config file", e);
            }
        } else if(file.getParentFile().canWrite() || file.canWrite()) {
            DataResult<JsonElement> result = Config.CODEC.encodeStart(JsonOps.INSTANCE, Config.DEFAULT);
            JsonElement json = result.getOrThrow();
            try(Writer writer = new FileWriter(file)) {
                GSON.toJson(json, GSON.newJsonWriter(writer));
            } catch (IOException e) {
                LocationWebSocket.LOGGER.error("Failed to write default config file", e);
            }
        } else {
            LocationWebSocket.LOGGER.info("Config at {} is not writable or readable. Using default config.", configPath);
        }

        if (!config.getEnabled()) {
            LocationWebSocket.LOGGER.info("Mod is disabled.");
            return config;
        }

        try{
            File errorFile = new File(configPath.getParent().toFile(), "error.log");
            config = new Config(
                    config.getWebsocketPort(),
                    config.getWebsocketUrl(),
                    config.getEnabled(),
                    errorFile
            );
        } catch (IOException e) {
            config = new Config(
                    config.getWebsocketPort(),
                    config.getWebsocketUrl(),
                    config.getEnabled()
            );
            LocationWebSocket.LOGGER.error("Failed to create error log file", e);
        }

        return config;
    }

    /**
     * Log errors to the error file
     * @param message The error message
     * @return True if the error was logged, false otherwise
     */
    public static boolean logErrors(String message) {
        if (LocationWebSocket.CONFIG.getErrorFile() != null) {
            try {
                LocationWebSocket.CONFIG.getErrorFile().write("[ERROR]: " + message + "\n");
                LocationWebSocket.CONFIG.getErrorFile().flush();
                LocationWebSocket.errors.remove(ERRORS.ERROR_FILE_WRITE_FAILURE);
                return true;
            } catch (IOException e) {
                if (LocationWebSocket.errors.contains(ERRORS.ERROR_FILE_WRITE_FAILURE)) {
                    return false;
                }
                LocationWebSocket.errors.add(ERRORS.ERROR_FILE_WRITE_FAILURE);
                LocationWebSocket.LOGGER.error("Failed to write to error log file", e);
                return false;
            }
        }
        return false;
    }

    /**
     * Log errors to the error file with throwable
     * @param message The error message
     * @param throwable The throwable
     * @return True if the error was logged, false otherwise
     */
    public static boolean logErrors(String message, Throwable throwable) {
        if (LocationWebSocket.CONFIG.getErrorFile() != null) {
            try {
                LocationWebSocket.CONFIG.getErrorFile().write("[ERROR]: " + message + " " + throwable.toString() + "\n");
                LocationWebSocket.CONFIG.getErrorFile().flush();
                LocationWebSocket.errors.remove(ERRORS.ERROR_FILE_WRITE_FAILURE);
                return true;
            } catch (IOException e) {
                if (LocationWebSocket.errors.contains(ERRORS.ERROR_FILE_WRITE_FAILURE)) {
                    return false;
                }
                LocationWebSocket.errors.add(ERRORS.ERROR_FILE_WRITE_FAILURE);
                LocationWebSocket.LOGGER.error("Failed to write to error log file", e);
                return false;
            }
        }
        return false;
    }

    /**
     * Log info messages to the error file
     * @param message The info message
     * @return True if the info was logged, false otherwise
     */
    public static boolean logInfo(String message) {
        if (LocationWebSocket.CONFIG.getErrorFile() != null) {
            try{
                LocationWebSocket.CONFIG.getErrorFile().write("[INFO]: " + message + "\n");
                LocationWebSocket.CONFIG.getErrorFile().flush();
                LocationWebSocket.errors.remove(ERRORS.ERROR_FILE_WRITE_FAILURE);
                return true;
            } catch (IOException e) {
                if (LocationWebSocket.errors.contains(ERRORS.ERROR_FILE_WRITE_FAILURE)){
                    return false;
                }
                LocationWebSocket.errors.add(ERRORS.ERROR_FILE_WRITE_FAILURE);
                LocationWebSocket.LOGGER.error("Failed to write to error log file", e);
                return false;
            }
        }
        return false;
    }

    /**
     * Log debug messages to the error file
     * @param message The debug message
     * @return True if the debug was logged, false otherwise
     */
    public static boolean logDebug(String message) {
        if (LocationWebSocket.CONFIG.getErrorFile() != null) {
            try {
                LocationWebSocket.CONFIG.getErrorFile().write("[DEBUG]: " + message + "\n");
                LocationWebSocket.CONFIG.getErrorFile().flush();
                LocationWebSocket.errors.remove(ERRORS.ERROR_FILE_WRITE_FAILURE);
                return true;
            } catch (IOException e) {
                if (LocationWebSocket.errors.contains(ERRORS.ERROR_FILE_WRITE_FAILURE)) {
                    return false;
                }
                LocationWebSocket.errors.add(ERRORS.ERROR_FILE_WRITE_FAILURE);
                LocationWebSocket.LOGGER.error("Failed to write to error log file", e);
                return false;
            }
        }
        return false;
    }
}
