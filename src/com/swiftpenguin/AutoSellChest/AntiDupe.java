package com.swiftpenguin.AutoSellChest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class AntiDupe implements Listener {

    private AutoSellChest plugin;
    public AntiDupe(AutoSellChest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void blocker (InventoryMoveItemEvent e){

        if (e.getDestination().getType().equals(InventoryType.HOPPER) && e.getInitiator().getType().equals(InventoryType.HOPPER)) {
            if (e.getDestination().getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.CHEST)){

                BlockState state = e.getDestination().getLocation().getBlock().getRelative(BlockFace.UP).getState();
                Chest chest = (Chest) state;

                if (chest.getInventory().getTitle().equals("AutoSeller")){
                    e.setCancelled(true);
                    Location loc = e.getDestination().getLocation();
                    loc.getBlock().setType(Material.AIR);

                }
            }
        }
    }

}
