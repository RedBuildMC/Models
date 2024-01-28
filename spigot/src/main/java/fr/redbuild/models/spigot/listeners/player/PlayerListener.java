package fr.redbuild.models.spigot.listeners.player;
import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @Autowired
    private PlayerManager playerManager;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        playerManager.registerPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        playerManager.deRegisterPlayer(event.getPlayer());
    }
}
