package com.swiftpenguin.AutoSellChest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.meta.BlockStateMeta;

import javax.xml.crypto.Data;

public class SignCreation implements Listener {

    private AutoSellChest plugin;

    public SignCreation(AutoSellChest plugin) {
        this.plugin = plugin;

    }

 @EventHandler
    public void onPlace(SignChangeEvent e){
        if (!e.getPlayer().hasPermission("acs.use")) {
            e.getPlayer().sendMessage(ChatColor.RED + "You lack the permissions to create a AutoSell chest...");

        } else {
            
        if (e.getBlock().getState() instanceof Sign) {
            String mainCallout = plugin.getConfig().getString("Language.mainCallout");
            if (e.getLine(0).equalsIgnoreCase(mainCallout)) {
                if (e.getBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.CHEST) || e.getBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.TRAPPED_CHEST)){
                    e.setLine(0, ChatColor.LIGHT_PURPLE + mainCallout);
                    e.setLine(1, e.getPlayer().getName());
                    e.setLine(2, ChatColor.DARK_GREEN + "{WORKING}");
                    e.setLine(3, "0");

                    String creation = plugin.getConfig().getString("Language.sellerCreate").replace("&", "§");
                    e.getPlayer().sendMessage(creation);

                    BlockState state = e.getBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getState();
                    if(state instanceof Chest){
                        Chest chest = (Chest) state;
                        chest.setCustomName("AutoSeller");
                        chest.update();
                    }
                    if (plugin.getConfig().getBoolean("Settings.SoundOnCreation")){
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 8, 1);
                    }
                } else {
                    String failed = plugin.getConfig().getString("Language.createFailedSign").replace("&", "§");
                    e.getPlayer().sendMessage(failed);
                }
            }
        }
    }
 }
}



