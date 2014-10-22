/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdjuady.googlemail.enderwarp.listener;

import com.mcdjuady.googlemail.enderwarp.EnderWarp;
import com.mcdjuady.googlemail.enderwarp.misc.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author McDjuady
 */
public class ItemUseListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //Bukkit.getLogger().info("IsCanceldStart: " + e.isCancelled());
        ItemStack itemInHand = e.getPlayer().getItemInHand();
        if (itemInHand != null && itemInHand.getType() == Material.EYE_OF_ENDER) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BEACON) {
                Block clickedBlock = e.getClickedBlock();
                //e.getPlayer().sendMessage("Create warp Eye");
                ItemStack warpEye = Util.createWarpEye(clickedBlock);

                int newAmmount = itemInHand.getAmount() - 1;
                if (newAmmount > 0) {
                    //e.getPlayer().sendMessage("decrease and add");
                    if (e.getPlayer().getInventory().addItem(warpEye).isEmpty()) {
                        itemInHand.setAmount(newAmmount);
                        e.getPlayer().updateInventory(); //TODO temp!
                    }
                } else {
                    //e.getPlayer().sendMessage("remove");
                    e.getPlayer().getInventory().setItemInHand(null);
                    e.getPlayer().updateInventory(); //TODO temp!
                    e.getPlayer().getInventory().addItem(warpEye);
                    e.getPlayer().updateInventory(); //TODO temp!
                }
                e.setCancelled(true);
            } else if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasLore()) {
                //e.getPlayer().sendMessage("Right click Air");
                if (!Util.isWarpEye(itemInHand)) { //TODO ignore?
                    e.getPlayer().sendMessage(ChatColor.DARK_RED.toString() + "The eye you used was not a warp eye! Please contact your server admin");
                    e.setCancelled(true);
                    return;
                }
                String hiddenLore = Util.unhideString(itemInHand.getItemMeta().getLore().get(1)).replace("[WarpEye]", "");
                String[] coords = hiddenLore.split(":");
                //e.getPlayer().sendMessage("Coords: " + Arrays.toString(coords));
                if (coords.length != 4) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED.toString() + "The eye you used contaided corrupted coordinates. Please contact your server admin.");
                    e.setCancelled(true);
                    return;
                }
                String worldName = coords[3];
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED.toString() + "The world your warp eye is linked to does not exist! Please contact your server admin.");
                    e.setCancelled(true);
                    return;
                }

                Location beacon = new Location(world, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]) - 1, Integer.valueOf(coords[2]));
                //TODO widen checks?
                if (world.getBlockAt(beacon).getType() != Material.BEACON || !world.getBlockAt(beacon.add(0, 1, 0)).isEmpty() || !world.getBlockAt(beacon.add(0, 1, 0)).isEmpty()) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED + "The linked beacon was missing or obstructed");
                    e.setCancelled(true);
                    return;
                }

                //update inv before tp
                if (e.getPlayer().getGameMode() != GameMode.CREATIVE && !itemInHand.containsEnchantment(Enchantment.DURABILITY)) {
                    int newAmmount = itemInHand.getAmount() - 1;
                    if (newAmmount > 0) {
                        //e.getPlayer().sendMessage("decrease");
                        itemInHand.setAmount(newAmmount);
                    } else {
                        //e.getPlayer().sendMessage("remove");
                        e.getPlayer().getInventory().setItemInHand(null);
                    }
                }

                //e.getPlayer().sendMessage("TP");
                e.getPlayer().teleport(new Location(world, Integer.valueOf(coords[0]) + 0.5, Integer.valueOf(coords[1]), Integer.valueOf(coords[2]) + 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
                //TODO add effects

                e.setCancelled(true);
            }
        }
        //Bukkit.getLogger().info("IsCanceldEnd: " + e.isCancelled());
    }

    @EventHandler
    public void onCraftigPrepare(PrepareItemCraftEvent e) {
        //some kind of stupid check because recipes aren't equal for whatever reason
        //Bukkit.getLogger().info("PrepareCraft: " + items.length + "\n" + Arrays.deepToString(items));
        if (Util.isCloningRecipe(e.getInventory(), Material.EYE_OF_ENDER)) {
            //find all 3 eyes
            int i = 0;
            ItemStack[] items = new ItemStack[3];
            for (ItemStack item : e.getInventory()) {
                if (item != null && item.getType() == Material.EYE_OF_ENDER) {
                    items[i++] = item;
                }
            }
            if (Util.isWarpEye(items[1]) ^ Util.isWarpEye(items[2])) { //only do this if it's one warp eye
                ItemStack warpEye = Util.isWarpEye(items[1]) ? items[1] : items[2];
                ItemStack resultEye = warpEye.clone();
                resultEye.setAmount(1);
                e.getInventory().setResult(resultEye);
            } else {
                e.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (Util.isCloningRecipe(e.getInventory(), Material.EYE_OF_ENDER)) {
            e.setResult(Event.Result.DENY); //cancel the crafting and do our manual version
            e.setCancelled(true);
            //find all 3 eyes
            int i = 0;
            ItemStack[] items = new ItemStack[3];
            for (ItemStack item : e.getInventory()) {
                if (item != null && item.getType() == Material.EYE_OF_ENDER) {
                    items[i++] = item;
                }
            }
            ItemStack warpEye = Util.isWarpEye(items[1]) ? items[1] : items[2];
            ItemStack normalEye = Util.isWarpEye(items[1]) ? items[2] : items[1];
            ItemStack resultEye = warpEye.clone();
            resultEye.setAmount(1);
            //since there is no option in CB to leave something behind in the crafting grid we have to do the crafting manualy
            //special handling for shift-clicking
            if (e.isShiftClick()) {
                resultEye.setAmount(normalEye.getAmount());

                HashMap<Integer, ItemStack> returnedItems = e.getView().getPlayer().getInventory().addItem(resultEye);
                //deal with leftovers
                if (!returnedItems.isEmpty()) {
                    ItemStack leftover = returnedItems.get(0);
                    Bukkit.getLogger().info(leftover.getAmount() + "");
                    normalEye.setAmount(leftover.getAmount());
                } else {
                    e.getInventory().remove(normalEye);
                }
            } else if (e.getCursor().getType() == Material.AIR || (e.getCursor().isSimilar(warpEye) && e.getCursor().getAmount() < e.getCursor().getMaxStackSize())) {
                int ammount = normalEye.getAmount();
                if (e.getCursor().getType() == Material.AIR) {
                    e.setCursor(resultEye); //TODO temp?
                    ammount--;
                    //warpEye.setAmount(warpEye.getAmount()+1);
                    //e.setResult(Event.Result.ALLOW);
                } else {
                    e.getCursor().setAmount(e.getCursor().getAmount() + 1);
                    ammount--;
                }

                if (ammount <= 0) {
                    e.getInventory().remove(normalEye);
                } else {
                    normalEye.setAmount(ammount);
                }
                //TODO updateInv?
            }
        }
    }
}
