package fr.elitgaimix.models.spigot.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.elitgaimix.models.spigot.Autowired.Autowired;

public class UserListener implements Listener{
    @Autowired
    private UserManager userManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        userManager.playerJoin(event.getPlayer());
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        userManager.playerQuit(event.getPlayer());
    }
}
