package fr.redbuild.models.paper.mode;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.packet.PacketUtils;
import fr.redbuild.models.paper.plugin.PluginController;
import fr.redbuild.models.paper.region.Region;
import fr.redbuild.models.paper.region.RegionController;
import fr.redbuild.models.paper.user.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BuildMode {
    @Autowired
    private RegionController regionController;
    @Autowired
    private UserManager userManager;
    private List<Player> players = new ArrayList<>();

    private BukkitTask checker = null;

    public boolean contains(Player player){
        return players.contains(player);
    }

    public void register(Player player){
        players.add(player);
        regionController.getRegions().forEach(region -> {
            if(region.getStart().getWorld() == player.getWorld() && region.visibility && (!userManager.getUser(player).hasAttribute("build_particles") || userManager.getUser(player).getAttribute("build_particles").equals("true")))
                region.getParticles().forEach(packet -> PacketUtils.sendPacket(player,packet));
            });
        if(checker == null)
            startTimer();
    }

    public void deRegister(Player player){
        players.remove(player);
    }

    public void startTimer(){
        checker = PluginController.INSTANCE.getServer().getScheduler()
        .runTaskTimer(PluginController.INSTANCE, this::step, 0, 10L);
    }

    public void step(){
        regionController.getRegions().forEach(r -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                 if(r.getStart().getWorld() == player.getWorld() && r.visibility && (!userManager.getUser(player).hasAttribute("build_particles") || userManager.getUser(player).getAttribute("build_particles").equals("true")))
                    PacketUtils.sendPackets(players, r.getParticles());
            });
        });
    }

    public List<String> getSafeZone(){
        return List.of(
                        "Spawn","default","lobby");
    }

    public List<Region> getSafeRegions(){
        List<Region> finalList = new ArrayList<>();
        getSafeZone().forEach(name -> regionController.getRegion(name).ifPresent(reg -> finalList.add(reg)));
        return finalList;
    }

    public boolean isSafeRegion(Region reg){
        if(getSafeRegions().contains(reg))
            return true;
        if(getSafeZone().contains(reg.getName()))
            return true;
        return false;
    }
}
