package com.swiftpenguin.AutoSellChest;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBreak implements Listener {

    private AutoSellChest plugin;

    public onBreak(AutoSellChest plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    public void chestBreak(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST)){

            org.bukkit.block.Block above = e.getBlock().getLocation().getBlock().getRelative(BlockFace.UP);

            if (above.getState() instanceof Sign) {
                Sign sign = (Sign) above.getLocation().getBlock().getState();
                String mainCallout = plugin.getConfig().getString("Language.mainCallout");

                if (sign.getLine(0).length() > 0 && sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + mainCallout)) {

                    String signName = sign.getLine(1);
                    String pName = e.getPlayer().getName();

                    if (signName.equalsIgnoreCase(pName) || e.getPlayer().hasPermission("acs.break")){

                        //String onBreak = plugin.getConfig().getString("Language.sellerDestroy");

                        BlockState state = e.getBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getState();
                        if(state instanceof Chest){
                            Chest chest = (Chest) state;
                            chest.setCustomName("Chest");
                            chest.update();
                        }

                        String onBreak = plugin.getConfig().getString("Language.sellerDestroy").replace("&", "§");
                        e.getPlayer().sendMessage(onBreak);
                        return;
                    }

                    e.setCancelled(true);
                    String denybreak = plugin.getConfig().getString("Language.denyBreak").replace("%p%", signName);
                    e.getPlayer().sendMessage(ChatColor.RED + denybreak);

                }
            }
        }
    }

    @EventHandler
    public void signBreak(BlockBreakEvent e){
        if (e.getBlock().getState() instanceof Sign){

            Sign sign = (Sign) e.getBlock().getState();
            String mainCallout = plugin.getConfig().getString("Language.mainCallout");

            //if (sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "<AutoSell>")) {
            if (sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + mainCallout)) {
                String signName = sign.getLine(1);
                String pName = e.getPlayer().getName();

                if (signName.equalsIgnoreCase(pName) || e.getPlayer().hasPermission("acs.break")){

                    //String onBreak = plugin.getConfig().getString("Language.sellerDestroy");

                    BlockState state = e.getBlock().getLocation().getBlock().getRelative(BlockFace.DOWN).getState();
                    if(state instanceof Chest){
                        Chest chest = (Chest) state;
                        chest.setCustomName("Chest");
                        chest.update();
                    }

                    String onBreak = plugin.getConfig().getString("Language.sellerDestroy").replace("&", "§");
                    e.getPlayer().sendMessage(onBreak);
                    return;
                }

                e.setCancelled(true);
                String denybreak = plugin.getConfig().getString("Language.denyBreak").replace("%p%", signName);
                e.getPlayer().sendMessage(denybreak);

            }
        }
    }
}
