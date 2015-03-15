/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp;

import com.googlemail.mcdjuady.craftutils.CraftUtils;
import com.googlemail.mcdjuady.craftutils.recipes.ShapelessAdvancedRecipe;
import com.mcdjuady.googlemail.enderwarp.listener.ItemUseListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author McDjuady
 */
public class EnderWarp extends JavaPlugin {
    
    private static boolean consumeOnThrow;
    private static int beaconLevel;
    
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            this.saveDefaultConfig();
        }
        updateConfig();
        consumeOnThrow = getConfig().getBoolean("ConsumeOnThrow");
        beaconLevel = Math.min(4,getConfig().getInt("BeaconLevel"));
        ShapelessAdvancedRecipe cloneRecipe = new ShapelessAdvancedRecipe(new ItemStack(Material.EYE_OF_ENDER), new CloneEyeValidator());
        ShapelessAdvancedRecipe deWarpRecipe = new ShapelessAdvancedRecipe(new ItemStack(Material.EYE_OF_ENDER), new DeWarpEyeValidator());
        Bukkit.getPluginManager().registerEvents(new ItemUseListener(), this);
        cloneRecipe.addIngredient(2, Material.EYE_OF_ENDER);
        deWarpRecipe.addIngredient(Material.EYE_OF_ENDER);
        CraftUtils.getRecipeManager().addRecipe(cloneRecipe);
        CraftUtils.getRecipeManager().addRecipe(deWarpRecipe);
    }
    
    private void updateConfig() {
        FileConfiguration config = this.getConfig();
        Configuration defaultConfig = config.getDefaults();
        for (String key : defaultConfig.getKeys(true)) {
            if (config.get(key, null) == null) {
                config.set(key, defaultConfig.get(key));
            }
        }
        this.saveConfig();
    }

    /**
     * @return the consumeOnThrow
     */
    public static boolean consumeOnThrow() {
        return consumeOnThrow;
    }

    /**
     * @return the beaconLevel
     */
    public static int getBeaconLevel() {
        return beaconLevel;
    }
    
}
