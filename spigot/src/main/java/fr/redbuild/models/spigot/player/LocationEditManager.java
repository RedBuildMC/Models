package fr.redbuild.models.spigot.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.handlers.LocationEditMode;
import fr.redbuild.models.spigot.listeners.handlers.HandlerListener;
import net.md_5.bungee.api.ChatColor;

public class LocationEditManager {
    public void addEditor(Player player, LocationEditMode locationEditMode){
        player.sendMessage(MiniUtils.getMiniMessage().deserialize("<gold>You are in Edit mode ! <red> Right click to cancel <gold> and <green>Left click to edit"));
        BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "You are in Edit mode ! " + ChatColor.RED + " Right click to cancel " + ChatColor.GOLD + " and " + ChatColor.GREEN + "Left click to edit", BarColor.GREEN, BarStyle.SOLID);
        bar.setVisible(true);
        bar.addPlayer(player);
        HandlerListener.registerLocationEditHandler(player, new LocationEditMode() {
            @Override
            public void execute(Player player, Location location) {
                bar.setVisible(false);
                bar.removePlayer(player);
                locationEditMode.execute(player, location);
            }

            @Override
            public void cancel(Player player) {
                bar.setVisible(false);
                bar.removePlayer(player);
                locationEditMode.cancel(player);
            }
        });
    }

    public void addEditor(Player player, Runnable execute,Runnable cancel){
        player.closeInventory();
        player.sendMessage(MiniUtils.getMiniMessage().deserialize("<gold>You are in Edit mode ! <red> Right click to cancel <gold> and <green>Left click to edit"));
        BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "You are in Edit mode ! " + ChatColor.RED + " Right click to cancel " + ChatColor.GOLD + " and " + ChatColor.GREEN + "Left click to edit", BarColor.GREEN, BarStyle.SOLID);
        bar.setVisible(true);
        bar.addPlayer(player);
        HandlerListener.registerLocationEditHandler(player, new LocationEditMode() {
            @Override
            public void execute(Player player, Location location) {
                bar.setVisible(false);
                bar.removePlayer(player);
                execute.run();
            }

            @Override
            public void cancel(Player player) {
                bar.setVisible(false);
                bar.removePlayer(player);
                cancel.run();
            }
        });
    }
}
