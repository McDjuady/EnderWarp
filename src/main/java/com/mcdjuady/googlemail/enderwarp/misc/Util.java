/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp.misc;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author McDjuady
 */
public class Util {

    public static int getBeaconLevel(World world, int beaconX, int beaconY, int beaconZ) {
        boolean valid = true;
        int level;
        for (level = 0; level < 4; level++) {
            int y = level + 1;
            for (int x = -y; x < y; x++) {
                for (int z = -y; z < y; z++) {
                    Block block = world.getBlockAt(beaconX + x, beaconY - y, beaconZ + z);
                    if (block.getType() != Material.IRON_BLOCK && block.getType() != Material.GOLD_BLOCK && block.getType() != Material.EMERALD_BLOCK && block.getType() != Material.DIAMOND_BLOCK) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    break;
                }
            }
            if (!valid) {
                break;
            }
        }
        return level;
    }

    public static String hideString(String str) {
        char[] data = new char[str.length() * 2];
        for (int i = 0; i < data.length; i += 2) {
            data[i] = ChatColor.COLOR_CHAR;
            data[i + 1] = str.charAt(i / 2);
        }
        return new String(data);
    }

    public static String unhideString(String str) {
        return str.replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "");
    }

    public static ItemStack createWarpEye(Block target) {
        //Bukkit.getLogger().info("createWarpEye");
        ItemStack newItem = new ItemStack(Material.EYE_OF_ENDER);
        final String name = ChatColor.DARK_PURPLE + "Warp Eye"; //TODO move to config
        //Bukkit.getLogger().info("Name: "+name);

        String hiddenLore = Util.hideString("[WarpEye]" + target.getX() + ":" + (target.getY() + 1) + ":" + target.getZ() + ":" + target.getWorld().getName());
        String lore = ChatColor.DARK_PURPLE.toString() + "Warp Eye to " + target.getX() + "/" + target.getY() + "/" + target.getZ(); //TODO named beacons?
        //Bukkit.getLogger().info("Lore: "+lore+"\nHiddenLore: "+Util.unhideString(hiddenLore));

        ItemMeta meta = newItem.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(new String[]{lore, hiddenLore}));
        newItem.setItemMeta(meta);
        //Bukkit.getLogger().info("NewItem: "+newItem.toString());
        return newItem;
    }

    public static boolean isWarpEye(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() < 2) {
            return false;
        }
        String hiddenLore = Util.unhideString(meta.getLore().get(1));
        //e.getPlayer().sendMessage("HiddenLore:" + hiddenLore);
        return hiddenLore.matches("^\\[WarpEye\\](-)?(\\d)+:(-)?(\\d)+:(-)?(\\d)+:(\\w)+$");
    }

}
