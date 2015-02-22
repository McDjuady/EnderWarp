/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp;

import com.googlemail.mcdjuady.craftutils.validators.CloneValidator;
import com.mcdjuady.googlemail.enderwarp.misc.Util;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class CloneEyeValidator extends CloneValidator{
    
    @Override
    public boolean validate(List<ItemStack> ingredients) {
        if (ingredients.size() != 2) {
            return false;
        }
        boolean valid = false;
        for (ItemStack item : ingredients) {
            valid = valid ^ Util.isWarpEye(item);
        }
        return valid;
    }
    
}
