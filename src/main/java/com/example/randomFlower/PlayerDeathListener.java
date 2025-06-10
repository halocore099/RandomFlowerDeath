package com.example.randomFlower;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PlayerDeathListener implements Listener {
    
    private final RandomFlowerPlugin plugin;
    private final List<Flower> flowers;
    private final Map<UUID, Boolean> playerSettings;
    private final Map<UUID, Flower> pendingFlowers;
    private final Random random = new Random();
    
    public PlayerDeathListener(RandomFlowerPlugin plugin, List<Flower> flowers, Map<UUID, Boolean> playerSettings, Map<UUID, Flower> pendingFlowers) {
        this.plugin = plugin;
        this.flowers = flowers;
        this.playerSettings = playerSettings;
        this.pendingFlowers = pendingFlowers;
    }
    
    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();
        
        boolean enabled = playerSettings.getOrDefault(playerId, plugin.isDefaultEnabled());
        
        if (enabled && !flowers.isEmpty()) {
            Flower randomFlower = flowers.get(random.nextInt(flowers.size()));
            pendingFlowers.put(playerId, randomFlower);
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        Flower pendingFlower = pendingFlowers.remove(playerId);
        if (pendingFlower != null) {
            ItemStack flowerItem = createFlowerItem(pendingFlower);
            player.getInventory().addItem(flowerItem);
        }
    }
    
    private ItemStack createFlowerItem(Flower flower) {
        ItemStack item = new ItemStack(flower.material, flower.amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name with color codes
            if (flower.name != null && !flower.name.isEmpty()) {
                String coloredName = ChatColor.translateAlternateColorCodes('&', flower.name);
                meta.setDisplayName(coloredName);
            }
            
            // Set lore (description) with color codes
            if (flower.lore != null && !flower.lore.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : flower.lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(coloredLore);
            }
            
            // Set unbreakable
            if (flower.unbreakable) {
                meta.setUnbreakable(true);
            }
            
            // Set custom model data
            if (flower.customModelData > 0) {
                meta.setCustomModelData(flower.customModelData);
            }
            
            // Add enchantments
            if (flower.enchantments != null && !flower.enchantments.isEmpty()) {
                for (Map.Entry<String, Integer> entry : flower.enchantments.entrySet()) {
                    try {
                        Enchantment enchant = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(entry.getKey().toLowerCase()));
                        if (enchant != null) {
                            meta.addEnchant(enchant, entry.getValue(), true); // true = ignore level restrictions
                        } else {
                            plugin.getLogger().warning("Invalid enchantment: " + entry.getKey());
                        }
                    } catch (Exception e) {
                        plugin.getLogger().warning("Error adding enchantment " + entry.getKey() + ": " + e.getMessage());
                    }
                }
            }
            
            // Add glow effect (fake enchantment)
            if (flower.glowing && (flower.enchantments == null || flower.enchantments.isEmpty())) {
                // Only add fake enchantment if no real enchantments exist
                Enchantment unbreaking = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("unbreaking"));
                if (unbreaking != null) {
                    meta.addEnchant(unbreaking, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Hide the enchantment text
                }
            }
            
            // Apply item flags
            if (flower.itemFlags != null && !flower.itemFlags.isEmpty()) {
                for (String flagName : flower.itemFlags) {
                    try {
                        ItemFlag flag = ItemFlag.valueOf(flagName.toUpperCase());
                        meta.addItemFlags(flag);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid item flag: " + flagName);
                    }
                }
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static class Flower {
        public final Material material;
        public final String name;
        public final List<String> lore;
        public final boolean glowing;
        public final int amount;
        public final Map<String, Integer> enchantments;
        public final boolean unbreakable;
        public final int customModelData;
        public final List<String> itemFlags;
        
        public Flower(Material material, String name, List<String> lore, boolean glowing, 
                     int amount, Map<String, Integer> enchantments, boolean unbreakable, 
                     int customModelData, List<String> itemFlags) {
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.glowing = glowing;
            this.amount = Math.max(1, Math.min(64, amount)); // Clamp between 1-64
            this.enchantments = enchantments;
            this.unbreakable = unbreakable;
            this.customModelData = customModelData;
            this.itemFlags = itemFlags;
        }
    }
}