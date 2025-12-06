# LocationWebsocket

A Fabric mod for Minecraft that broadcasts player location and rotation data via WebSocket in real-time.

## Features

- Sends player location (x, y, z) and rotation data to a WebSocket server
- Broadcasts data for all players on the server every game tick
- Configurable WebSocket server URL and port
- Automatically creates a default configuration file

## Requirements

- Minecraft 1.21.10
- Fabric Loader 0.18.1 or higher
- Fabric API
- Java 21 or higher

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the LocationWebsocket mod and place it in your `mods` folder

## Configuration

On first run, the mod creates a configuration file at:
```
<minecraft_config_dir>/LocationWebsocket_settings.json
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `websocketPort` | Integer | `8080` | The port for the WebSocket server |
| `websocketURL` | String | `localhost` | The URL/IP address of the WebSocket server |

### Example Configuration

```json
{
  "websocketPort": 8080,
  "websocketURL": "localhost"
}
```

## WebSocket Data Format

The mod sends JSON data containing player information in the following format:

```json
{
  "players": [
    {
      "uuid": "player-uuid-here",
      "location": [x, y, z],
      "rotation": [pitch, yaw]
    }
  ]
}
```

### Fields

- `uuid`: The player's unique identifier
- `location`: An array containing the player's X, Y, and Z coordinates
- `rotation`: An array containing the player's pitch and yaw values

## Building from Source

1. Clone the repository
2. Run the build command:
   ```bash
   ./gradlew build
   ```
3. The compiled mod will be in `build/libs/`

## Development

This mod uses:
- [Fabric Loom](https://github.com/FabricMC/fabric-loom) for development tooling
- [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket) for WebSocket client functionality
- Mixins for injecting into Minecraft's tick events

## License

This project is licensed under CC0 1.0 Universal. See the [LICENSE](LICENSE) file for details.
