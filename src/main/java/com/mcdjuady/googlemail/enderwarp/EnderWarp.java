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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author McDjuady
 */
public class EnderWarp extends JavaPlugin {

    @Override
    public void onEnable() {
        ShapelessAdvancedRecipe recipe = new ShapelessAdvancedRecipe(new ItemStack(Material.EYE_OF_ENDER), new CloneEyeValidator());
        Bukkit.getPluginManager().registerEvents(new ItemUseListener(), this);
        recipe.addIngredient(2, Material.EYE_OF_ENDER);
        CraftUtils.getRecipeManager().addRecipe(recipe);
    }

}
