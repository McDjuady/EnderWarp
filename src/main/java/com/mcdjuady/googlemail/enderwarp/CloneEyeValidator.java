/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp;

import com.googlemail.mcdjuady.craftutils.CloneValidator;
import com.mcdjuady.googlemail.enderwarp.misc.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class CloneEyeValidator extends CloneValidator{
    
    public boolean validate(ItemStack[] matrix) {
        if (matrix == null) {
            return false;
        }
        boolean valid = false;
        for (ItemStack item : matrix) {
            if (item != null && item.getType() != Material.AIR) {
                valid = valid ^ Util.isWarpEye(item);
            }
        }
        return valid;
    }
    
}
