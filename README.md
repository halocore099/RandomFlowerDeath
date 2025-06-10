# RandomFlowerPlugin

A Minecraft Paper plugin that gives players a random flower when they die and respawn.

## Features

- 🌸 **Random Flower Drops**: Players receive a random flower in their inventory when they respawn after death
- ⚙️ **Player Toggle**: Each player can enable/disable the feature for themselves
- 🎨 **Colored Flower Names**: Flowers have custom colored display names
- 📝 **Rich Item Customization**: Support for lore, enchantments, glowing effects, and more
- 🔄 **Hot Reload**: Reload configuration without restarting the server
- 🎯 **Permission-Based**: Control who can use commands and reload configs

## Installation

1. Download the latest `RandomFlowerDeath.jar` from the [Releases](../../releases) page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will automatically create a configuration file at `plugins/RandomFlowerPlugin/config.yml`

## Requirements

- **Server Software**: Paper 1.21+ (or compatible forks)
- **Java Version**: Java 21+
- **Minecraft Version**: 1.21+

## Commands

| Command | Description | Permission | Usage |
|---------|-------------|------------|-------|
| `/randomflowerondeath <true\|false>` | Toggle flower drops for yourself | `randomflower.toggle` | `/randomflowerondeath true` |
| `/randomflowerondeath reload` | Reload the plugin configuration | `randomflower.reload` | `/randomflowerondeath reload` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `randomflower.toggle` | Allows players to toggle the feature | `true` (all players) |
| `randomflower.reload` | Allows reloading the configuration | `op` (operators only) |

## Configuration

The plugin creates a configuration file at `plugins/RandomFlowerPlugin/config.yml`:

### Basic Example
```yaml
flowers:
  - material: "DANDELION"
    name: "&eSunny Dandelion"
  - material: "POPPY"
    name: "&cCrimson Poppy"
    lore:
      - "&7A vibrant red flower"
      - "&7Symbolizes love and passion"

defaultEnabled: true
showMessages: false
```

### Advanced Example
```yaml
flowers:
  # Glowing enchanted flower
  - material: "BLUE_ORCHID"
    name: "&9&lMystical Blue Orchid"
    lore:
      - "&bA rare magical flower"
      - "&7Glows with ethereal light"
    glowing: true
    enchantments:
      UNBREAKING: 3
      MENDING: 1
    unbreakable: true
    amount: 1
    item-flags:
      - "HIDE_ENCHANTS"
```

## Flower Attributes

Each flower in the config supports the following attributes:

### Required Attributes
| Attribute | Type | Description | Example |
|-----------|------|-------------|---------|
| `material` | String | Minecraft material name | `"DANDELION"` |

### Optional Attributes
| Attribute | Type | Default | Description | Example |
|-----------|------|---------|-------------|---------|
| `name` | String | Material name | Custom display name with color codes | `"&eMagical Flower"` |
| `lore` | List | None | Description lines under the item name | `["&7Line 1", "&7Line 2"]` |
| `glowing` | Boolean | `false` | Adds enchantment glow effect | `true` |
| `amount` | Integer | `1` | Number of items to give (1-64) | `3` |
| `enchantments` | Map | None | Enchantments with levels | `{UNBREAKING: 3, MENDING: 1}` |
| `unbreakable` | Boolean | `false` | Makes item unbreakable | `true` |
| `custom-model-data` | Integer | `0` | Custom model data for resource packs | `1001` |
| `item-flags` | List | None | Flags to hide item information | `["HIDE_ENCHANTS", "HIDE_ATTRIBUTES"]` |

### Available Item Flags
- `HIDE_ENCHANTS` - Hide enchantment information
- `HIDE_ATTRIBUTES` - Hide attribute modifiers
- `HIDE_UNBREAKABLE` - Hide unbreakable status
- `HIDE_DESTROYS` - Hide "can destroy" information
- `HIDE_PLACED_ON` - Hide "can be placed on" information
- `HIDE_POTION_EFFECTS` - Hide potion effects
- `HIDE_DYE` - Hide dye information

### Color Codes
Use `&` followed by a character for colors and formatting:

#### Colors
- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White

#### Formatting
- `&l` - Bold
- `&o` - Italic
- `&n` - Underline
- `&m` - Strikethrough
- `&k` - Magic/Obfuscated
- `&r` - Reset formatting

### Common Enchantments
- `UNBREAKING` - Increases durability
- `MENDING` - Repairs with experience
- `EFFICIENCY` - Faster tool usage
- `FORTUNE` - Better drops
- `SILK_TOUCH` - Collect blocks as-is
- `PROTECTION` - Reduces damage
- `SHARPNESS` - Increases weapon damage
- `POWER` - Increases bow damage
- `INFINITY` - Infinite arrows

## Plugin Settings

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `defaultEnabled` | Boolean | `true` | Whether new players have the feature enabled by default |
| `showMessages` | Boolean | `false` | Whether to show chat messages when flowers are given |

## How It Works

1. When a player dies, the plugin randomly selects a flower from the configuration
2. The selected flower is stored temporarily
3. When the player respawns, the flower is added to their inventory with all configured attributes
4. Players can toggle this feature on/off for themselves using the command

## Example Configurations

### Simple Setup
```yaml
flowers:
  - material: "DANDELION"
    name: "&eDandelion"
  - material: "POPPY"
    name: "&cPoppy"
  - material: "BLUE_ORCHID"
    name: "&9Blue Orchid"

defaultEnabled: true
showMessages: false
```

### Advanced Setup with Special Effects
```yaml
flowers:
  # Rare glowing flower
  - material: "BLUE_ORCHID"
    name: "&9&k||&r &b&lMystical Orchid &9&k||"
    lore:
      - "&7A flower touched by magic"
      - "&bGlows with ethereal energy"
      - "&8Rarity: &6Legendary"
    glowing: true
    enchantments:
      UNBREAKING: 10
    unbreakable: true
    item-flags:
      - "HIDE_ENCHANTS"
      - "HIDE_UNBREAKABLE"
  
  # Bouquet of flowers
  - material: "RED_TULIP"
    name: "&cRed Tulip Bouquet"
    lore:
      - "&7A beautiful arrangement"
    amount: 5
  
  # Custom resource pack flower
  - material: "ALLIUM"
    name: "&dSpecial Allium"
    custom-model-data: 2001
    glowing: true

defaultEnabled: true
showMessages: true
```

## Building from Source

1. Clone this repository
2. Make sure you have Java 21+ installed
3. Run the build command:
   ```bash
   ./gradlew build
   ```
4. The compiled JAR will be in `build/libs/`

## Support

If you encounter any issues or have suggestions:
1. Check the [Issues](../../issues) page
2. Create a new issue if your problem isn't already reported
3. Include your server version, plugin version, and any error messages

## License

This project is open source. Feel free to modify and distribute according to your needs.

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

---

**Enjoy your magical flowers! 🌸✨**