/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp;

import com.googlemail.mcdjuady.craftutils.validators.ShapelessValidator;
import com.mcdjuady.googlemail.enderwarp.misc.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class DeWarpEyeValidator implements ShapelessValidator{

    @Override
    public ItemStack getResult(List<ItemStack> ingredients) {
        return new ItemStack(Material.EYE_OF_ENDER);
    }

    @Override
    public boolean validate(List<ItemStack> ingredients) {
        if (ingredients.size() != 1) {
            return false;
        }
        return Util.isWarpEye(ingredients.get(0));
    }

    @Override
    public Map<ItemStack, Integer> costMatrix(List<ItemStack> ingredients) {
        Map<ItemStack, Integer> costMatrix = new HashMap<>();
        costMatrix.put(ingredients.get(0), 1);
        return costMatrix;
    }
    
}
