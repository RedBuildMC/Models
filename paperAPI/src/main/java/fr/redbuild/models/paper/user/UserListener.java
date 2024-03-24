package fr.redbuild.models.paper.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.redbuild.models.paper.Autowired.Autowired;

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
