package com.swiftpenguin.AutoSellChest;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LegacySellChest {

    private AutoSellChest plugin;

    public LegacySellChest(AutoSellChest plugin) {
        this.plugin = plugin;

    }

}
   /* private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        plugin.econ = rsp.getProvider();
        return plugin.econ != null;
    }


    @EventHandler
    public void transfer(InventoryMoveItemEvent e) {

        if (e.getInitiator().getType().equals(InventoryType.HOPPER) && e.getDestination().getType().equals(InventoryType.CHEST)) { //Hopper -> Chest Check

            String transfer = e.getItem().getType().toString().toUpperCase();

            if (getConfig().getConfigurationSection("ItemPrices").getKeys(false).contains(e.getItem().getType().toString().toUpperCase())){ // Item Config Check

                org.bukkit.block.Block above = e.getDestination().getLocation().getBlock().getRelative(BlockFace.UP); // Sign Grab

                if (above.getState() instanceof Sign) { //Sign Check
                    Sign sign = (Sign) above.getLocation().getBlock().getState();
                    String mainCallout = getConfig().getString("Language.mainCallout");

                    //if (sign.getLine(0).length() > 0 && sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "<AutoSell>")) { //Checking main Callout
                    if (sign.getLine(0).length() > 0 && sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + mainCallout)) { //Checking main Callout

                        if (sign.getLine(1).length() > 0) {
                            String pName = sign.getLine(1);
                            Player p = Bukkit.getServer().getPlayer(pName);

                            OfflinePlayer player = Bukkit.getOfflinePlayer(sign.getLine(1)); //Grabbing offlinePlayer
                            if (player == null) {
                                return;
                            }

                            //Bukkit.getServer().broadcastMessage("Type: " + e.getItem().getType());

                            double charge = plugin.getConfig().getDouble("ItemPrices." + transfer + ".price"); // Grabbing itemPrice
                            double multiply = charge * e.getItem().getAmount();

                            EconomyResponse r = plugin.econ.depositPlayer(player, charge); //Payment
                            if (r.transactionSuccess()) {

                                if(plugin.getConfig().getInt("Settings.mode") == 1){

                                    int mode1 = Integer.parseInt(sign.getLine(3));
                                    int newTotal = mode1 + 1;
                                    sign.setLine(3, Integer.toString(newTotal));
                                    sign.update();

                                } else {

                                    double mode2 = Double.parseDouble(sign.getLine(3));
                                    double newTotal = mode2 + multiply;
                                    //int parsed = (int) multiply;
                                    sign.setLine(3, Double.toString(newTotal));
                                    sign.update();
                                }

                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    public void run() {

                                        e.getDestination().clear();

                                    }
                                }, 1);
                            }
                        }
                    }
                }
            }
        }
    }
}*/