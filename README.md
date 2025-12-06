# LocationWebSocket

A server-side Fabric mod for Minecraft that broadcasts player location and rotation data via WebSocket in real-time.

## Features

- **Real-time Location Broadcasting**: Sends player coordinates (X, Y, Z) and rotation (pitch, yaw) via WebSocket every server tick
- **Multi-player Support**: Tracks and broadcasts location data for all players on the server
- **Configurable WebSocket Endpoint**: Customize the WebSocket server URL and port through a configuration file
- **JSON Format**: Location data is sent in an easy-to-parse JSON format

## Requirements

- Minecraft 1.21.10
- Fabric Loader 0.18.1 or higher
- Fabric API
- Java 21 or higher

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.10
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the latest release of LocationWebSocket from the [Releases](https://github.com/Pepijn-DB/LocationWebSocket/releases) page
4. Place the downloaded JAR file in your Minecraft `mods` folder
5. Launch Minecraft with the Fabric profile

## Configuration

On first run, the mod creates a configuration file at:
```
.minecraft/config/LocationWebSocket/config.json
```

### Configuration Options

| Option | Default | Description |
|--------|---------|-------------|
| `websocketPort` | `8080` | The port number for the WebSocket connection |
| `websocketURL` | `127.0.0.1` | The WebSocket server URL/hostname (e.g., `localhost` or `127.0.0.1`) |
| `enableMod` | `true` | Whether the mod is enabled. Set to `false` to disable without removing the mod |

### Example Configuration

```json
{
  "websocketPort": 8080,
  "websocketURL": "localhost",
  "enableMod": true
}
```

## WebSocket Data Format

The mod sends player location data in the following JSON format:

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
- `location`: Array containing X, Y, Z coordinates as doubles
- `rotation`: Array containing pitch and yaw values as doubles

## Building from Source

### Prerequisites

- JDK 21 or higher
- Git

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Pepijn-DB/LocationWebSocket.git
   cd LocationWebSocket
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. The compiled JAR file will be located in `build/libs/`

## Use Cases

- **Live Map Integration**: Connect to external mapping applications to display real-time player positions
- **Stream Overlays**: Create custom overlays showing player coordinates for streaming
- **Server Monitoring**: Track player movements across your server
- **Custom Applications**: Build applications that react to player positions in real-time

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
