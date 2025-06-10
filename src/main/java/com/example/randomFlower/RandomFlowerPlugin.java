package com.example.randomFlower;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class RandomFlowerPlugin extends JavaPlugin {

    private final Map<UUID, Boolean> playerSettings = new HashMap<>();
    private final Map<UUID, PlayerDeathListener.Flower> pendingFlowers = new HashMap<>();
    private List<PlayerDeathListener.Flower> flowers;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadFlowers();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, flowers, playerSettings, pendingFlowers), this);
        
        getLogger().info("RandomFlowerPlugin has been enabled!");
    }

    @SuppressWarnings("unchecked")
    private void loadFlowers() {
        flowers = new ArrayList<>();
        List<Map<?, ?>> flowerList = getConfig().getMapList("flowers");

        for (Map<?, ?> map : flowerList) {
            try {
                String matString = (String) map.get("material");
                String name = (String) map.get("name");
                List<String> lore = (List<String>) map.get("lore");
                boolean glowing = map.containsKey("glowing") ? (Boolean) map.get("glowing") : false;
                int amount = map.containsKey("amount") ? (Integer) map.get("amount") : 1;
                Map<String, Integer> enchantments = (Map<String, Integer>) map.get("enchantments");
                boolean unbreakable = map.containsKey("unbreakable") ? (Boolean) map.get("unbreakable") : false;
                int customModelData = map.containsKey("custom-model-data") ? (Integer) map.get("custom-model-data") : 0;
                List<String> itemFlags = (List<String>) map.get("item-flags");
                
                Material mat = Material.matchMaterial(matString);
                if (mat != null) {
                    flowers.add(new PlayerDeathListener.Flower(mat, name, lore, glowing, amount, enchantments, unbreakable, customModelData, itemFlags));
                } else {
                    getLogger().warning("Invalid material: " + matString);
                }
            } catch (Exception e) {
                getLogger().warning("Error loading flower from config: " + e.getMessage());
            }
        }
        
        if (flowers.isEmpty()) {
            getLogger().warning("No valid flowers found in config!");
        } else {
            getLogger().info("Loaded " + flowers.size() + " flowers from config!");
        }
    }
    
    public boolean isDefaultEnabled() {
        return getConfig().getBoolean("defaultEnabled", true);
    }
    
    public boolean shouldShowMessages() {
        return getConfig().getBoolean("showMessages", false);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomflowerondeath")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("randomflower.reload")) {
                    reloadConfig();
                    loadFlowers();
                    sender.sendMessage("§aRandomFlowerPlugin config reloaded!");
                    return true;
                } else {
                    sender.sendMessage("§cYou don't have permission to reload the config.");
                    return true;
                }
            }
            
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command can only be used by players.");
                return true;
            }

            if (args.length != 1) {
                player.sendMessage("Usage: /randomflowerondeath <true|false|reload>");
                return true;
            }
            
            boolean toggle;
            if (args[0].equalsIgnoreCase("true")) {
                toggle = true;
            } else if (args[0].equalsIgnoreCase("false")) {
                toggle = false;
            } else {
                player.sendMessage("Invalid argument. Use 'true', 'false', or 'reload'.");
                return true;
            }

            playerSettings.put(player.getUniqueId(), toggle);
            player.sendMessage("Random flower on death is now " + (toggle ? "enabled" : "disabled") + ".");
            return true;
        }
        return false;
    }
}
