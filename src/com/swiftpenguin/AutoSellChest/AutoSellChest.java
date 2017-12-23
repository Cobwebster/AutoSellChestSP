package com.swiftpenguin.AutoSellChest;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSellChest extends JavaPlugin implements Listener {

    public static Economy econ = null;

    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new SignCreation(this), this);
        getServer().getPluginManager().registerEvents(new onBreak(this), this);
        getServer().getPluginManager().registerEvents(new AntiDupe(this), this);
        registerConfig();

        if (getConfig().getInt("Version") == 1) {

            int ver = getConfig().getInt("Version") + 1;
            getConfig().addDefault("Language.mainCallout", "<AutoSell>");
            getConfig().options().copyDefaults(true);
            getConfig().set("Version", ver);
            saveConfig();
        }

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void registerConfig() {
        saveDefaultConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

            return true;

        } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("acs.reload")) {

            reloadConfig();
            sender.sendMessage(ChatColor.YELLOW + "[AutoSell] Version: " +getDescription().getVersion()+ " Reloaded...");

            if (getServer().getVersion().contains("1.8")) {
            } else {
                ((Player) sender).playSound(((Player) sender).getLocation(), Sound.BLOCK_ANVIL_USE, 10, 1);
                return true;
            }
        }
        return true;
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

                if (sign.getLine(0).length() > 0 && sign.getLine(0).equalsIgnoreCase(ChatColor.LIGHT_PURPLE + mainCallout)) { //Checking main Callout

                    if (sign.getLine(1).length() > 0) {
                        String pName = sign.getLine(1);
                        Player p = Bukkit.getServer().getPlayer(pName);

                        OfflinePlayer player = Bukkit.getOfflinePlayer(sign.getLine(1)); //Grabbing offlinePlayer
                        if (player == null) {
                            return;
                        }
                        //Bukkit.getServer().broadcastMessage("Type: " + e.getItem().getType());

                        double charge = getConfig().getDouble("ItemPrices." + transfer + ".price"); // Grabbing itemPrice
                        double multiply = charge * e.getItem().getAmount();

                        EconomyResponse r = econ.depositPlayer(player, charge); //Payment
                            if (r.transactionSuccess()) {

                                        if(getConfig().getInt("Settings.mode") == 1){
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

                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
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
    }
/*

                 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                                    public void run() {

                                        Bukkit.getServer().broadcastMessage("paid");

                                    }
                                }, 10);

 */