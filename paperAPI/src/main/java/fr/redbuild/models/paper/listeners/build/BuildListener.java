package fr.redbuild.models.paper.listeners.build;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.mode.BuildMode;
import fr.redbuild.models.paper.region.RegionController;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BuildListener implements Listener {
    @Autowired
    private BuildMode buildMode;

    @Autowired
    private RegionController regionController;

    @EventHandler
    public void onJoin(PlayerInteractEvent event) {
        Location location = event.getPlayer().getLocation();
        if (event.getClickedBlock() != null)
            location = event.getClickedBlock().getLocation();
        if (buildMode.contains(event.getPlayer()))
            return;
        if (buildMode.isSafeRegion(regionController.getRegion(location)))
            event.setCancelled(true);
    }
}
