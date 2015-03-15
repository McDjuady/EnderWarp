package com.mcdjuady.googlemail.enderwarp.listener;

import com.mcdjuady.googlemail.enderwarp.EnderWarp;
import com.mcdjuady.googlemail.enderwarp.misc.Util;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

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
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BEACON && !itemInHand.hasItemMeta()) {
                Block clickedBlock = e.getClickedBlock();
                //e.getPlayer().sendMessage("Create warp Eye");
                int level = Util.getBeaconLevel(clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
                if (level < EnderWarp.getBeaconLevel()) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED + "Can't link to a level " + level + " Beacon! The required level is " + EnderWarp.getBeaconLevel());
                    e.setCancelled(true);
                    return;
                }
                ItemStack warpEye = Util.createWarpEye(clickedBlock);

                int newAmmount = itemInHand.getAmount() - 1;
                if (newAmmount > 0) {
                    //e.getPlayer().sendMessage("decrease and add");
                    if (e.getPlayer().getInventory().addItem(warpEye).isEmpty()) {
                        itemInHand.setAmount(newAmmount);
                    }
                } else {
                    //e.getPlayer().sendMessage("remove");
                    e.getPlayer().getInventory().setItemInHand(null);
                    e.getPlayer().getInventory().addItem(warpEye);
                }
                e.getPlayer().updateInventory();
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
                Block beaconBlock = world.getBlockAt(beacon);
                final int beaconX = beacon.getBlockX();
                final int beaconY = beacon.getBlockY();
                final int beaconZ = beacon.getBlockZ();
                int level = Util.getBeaconLevel(world, beaconX, beaconY, beaconZ);

                if (level < EnderWarp.getBeaconLevel()) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED + "The beacon you tried to warp to wasn't a high enough level! Please upgrade your beacon");
                    e.setCancelled(true);
                    return;
                }

                if (beaconBlock.getType() != Material.BEACON || !world.getBlockAt(beacon.add(0, 1, 0)).isEmpty() || !world.getBlockAt(beacon.add(0, 1, 0)).isEmpty()) {
                    e.getPlayer().sendMessage(ChatColor.DARK_RED + "The linked beacon was missing or obstructed");
                    e.setCancelled(true);
                    return;
                }

                //update inv before tp
                if (e.getPlayer().getGameMode() != GameMode.CREATIVE && !itemInHand.containsEnchantment(Enchantment.DURABILITY) && EnderWarp.consumeOnThrow()) {
                    int newAmmount = itemInHand.getAmount() - 1;
                    if (newAmmount > 0) {
                        //e.getPlayer().sendMessage("decrease");
                        itemInHand.setAmount(newAmmount);
                    } else {
                        //e.getPlayer().sendMessage("remove");
                        e.getPlayer().getInventory().setItemInHand(null);
                    }
                }

                Random random = new Random();

                e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation(), Effect.PORTAL, null);
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1, random.nextFloat() + 0.5F);

                //e.getPlayer().sendMessage("TP");
                Location target = new Location(world, Integer.valueOf(coords[0]) + 0.5, Integer.valueOf(coords[1]), Integer.valueOf(coords[2]) + 0.5);
                e.getPlayer().teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);

                target.add(0, 1, 0);
                world.playEffect(target, Effect.PORTAL, null);
                world.playSound(target, Sound.ENDERMAN_TELEPORT, 1, random.nextFloat() + 0.5F);

                e.setCancelled(true);
            }
        }
        //Bukkit.getLogger().info("IsCanceldEnd: " + e.isCancelled());
    }
}
