package fr.redbuild.models.spigot.region;


import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.Gui.GuiBuilder;
import fr.redbuild.models.spigot.Gui.ItemBuilder;
import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.event.PlayerChangeRegionEvent;
import fr.redbuild.models.spigot.logger.CtMsg;
import fr.redbuild.models.spigot.mode.BuildMode;
import fr.redbuild.models.spigot.packet.SignUtils;
import fr.redbuild.models.spigot.plugin.PluginController;
import fr.redbuild.models.spigot.utils.injector.Injector;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * The RegionController class is responsible for managing regions in the application.
 * It provides methods for retrieving, creating, editing, and deleting regions.
 */
public class RegionController {
    @Autowired
    private BuildMode buildMode;
    private final MiniMessage mm = MiniUtils.getMiniMessage();

    private final RegionRepository regionRepository = Injector.registerInstance(new RegionRepository());
    private List<Region> allRegion = new ArrayList<>();
    private final Map<Player,Region> oldRegions = new HashMap<>();
    private BukkitTask checker = null;

    public final Region DEFAULT_REGION = new Region(true);

    private final Map<Player,Location> pos1 = new HashMap<>();

    private final Map<Player,Location> pos2 = new HashMap<>();
    public RegionController(){
    }
    public Region getRegion(Player player){
        for(Region r : allRegion){
            if(r.isInRegion(player))
                return isInSousRegion(r, player);
        }
        return DEFAULT_REGION;
    }

    public Region getRegion(Location location){
        for(Region r : allRegion){
            if(r.isInRegion(location))
                return isInSousRegion(r, location);
        }
        return DEFAULT_REGION;
    }

    private Region isInSousRegion(Region region,Location location){
        if(region.hasSousRegion() && region.getSousRegion().isInRegion(location)){
            return isInSousRegion(region, location);
        }else{
            return region;
        }
    }

    private Region isInSousRegion(Region region,Player player){
        if(region.hasSousRegion() && region.getSousRegion().isInRegion(player)){
            return isInSousRegion(region, player);
        }else{
            return region;
        }
    }

    public void initialize(){
        regionRepository.findAll().forEach(this::registerRegion);
        startRegionChecker();
    }

    public List<Region> getRegions(){
        return allRegion;
    }

    public void deleteRegion(Region region){
        deRegisterRegion(region);
        regionRepository.delete(region);
    }

    public void openEditor(Player player,Region region){
        new GuiBuilder("Edit region:" + region.getName()).rows(1).fillTop(Material.ORANGE_STAINED_GLASS_PANE)
                .item(new ItemBuilder(Material.WRITABLE_BOOK,"Edit name").setPos(2).onClick(() -> {
                    SignUtils.openSign(player,"New name :",region.getName(),((event, player1, text) -> {
                        region.setName(text.get(1));
                        saveRegion(region);
                        player1.sendMessage(mm.deserialize("<green>The region's name is now : <gold>" + text.get(1)));
                    }));
                }))
                .item(new ItemBuilder(Material.ENDER_PEARL,"Edit corner 1").setPos(4).onClick(() -> {
                    region.setStart(player.getLocation());
                    saveRegion(region);
                    player.closeInventory();
                    player.sendMessage(mm.deserialize("<green>Location changed !"));
                }))
                .item(new ItemBuilder(Material.ENDER_PEARL,"Edit corner 2").setPos(6).onClick(() -> {
                    region.setEnd(player.getLocation());
                    saveRegion(region);
                    player.closeInventory();
                    player.sendMessage(mm.deserialize("<green>Location changed !"));
                }))
                .open(player);
    }


    public Optional<Region> getRegion(String name){
        for(Region r : allRegion){
            if(r.getName().equalsIgnoreCase(name))
                return Optional.of(r);
        }
        return Optional.empty();
    }

    public void checkRegion(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            if(oldRegions.containsKey(player)) {
                if (!((oldRegions.get(player) == null && getRegion(player) == null) || oldRegions.get(player) == getRegion(player))){
                    Region newRegion = getRegion(player);
                    Region oldRegion = oldRegions.get(player);
                    if(oldRegion == null)
                        oldRegion = DEFAULT_REGION;
                    if(newRegion == null)
                        newRegion = DEFAULT_REGION;
                    if(buildMode.contains(player)){
                        // player.sendMessage(mm.deserialize("<gold>Changement de region de <red>" + oldRegion.getName() + "<gold> vers <green>" + newRegion.getName()));
                        CtMsg.sendMiniMessage("<gold>Changement de region de <red>" + oldRegion.getName() + "<gold> vers <green>" + newRegion.getName(), player);
                    }
                    new PlayerChangeRegionEvent(player,oldRegion,newRegion).callEvent();
                    oldRegions.remove(player);
                    oldRegions.put(player,newRegion);
                }

            }
        }
    }

    public void createRegion(String name,Player player){
        Region region = new Region();
        region.visibility = true;
        region.setName(name);
        if(pos1.containsKey(player) && pos2.containsKey(player)){
            region.setStart(pos1.get(player));
            region.setEnd(pos2.get(player));
        }
        registerRegion(region);
        regionRepository.save(region);
        // player.sendMessage(mm.deserialize("<green>La Region : <gold>" + name + " <green>a ete creer"));
        CtMsg.sendMiniMessage("<green>La Region : <gold>" + name + " <green>a ete creer", player);
    }

    public boolean isStart(){
        return checker != null;
    }

    public void setPos1(Player player){
        if(pos1.containsKey(player)) {
            pos1.replace(player, player.getLocation());
            return;
        }
        pos1.put(player,player.getLocation());
    }

    public void setPos2(Player player){
        if(pos2.containsKey(player)) {
            pos2.replace(player, player.getLocation());
            return;
        }
        pos2.put(player,player.getLocation());
    }

    public void registerPlayer(Player player){
        if(!oldRegions.containsKey(player))
            oldRegions.put(player,getRegion(player));
    }

    public void deRegisterPlayer(Player player){
        oldRegions.remove(player);
        pos1.remove(player);
        pos2.remove(player);
    }

    public void deRegisterRegion(Region region){
        allRegion.remove(region);
    }

    public void startRegionChecker(){
        // System.out.println(ChatColor.GREEN + "Start region checker !");
        CtMsg.log("§aStart region checker !");
        if(checker == null)
            checker = PluginController.INSTANCE.getServer()
                .getScheduler()
                .runTaskTimer(PluginController.INSTANCE, this::checkRegion, 0, 5L);
    }

    public void stopRegionChecker(){
        // System.out.println(ChatColor.RED + "Stop region checker !");
        CtMsg.log("§cStop region checker !");
        if(checker == null)
            return;
        checker.cancel();
    }

    public void saveRegion(Region region){
         allRegion.stream().filter(r -> r.getUuid() == region.getUuid()).findAny().ifPresent(oldRegion -> {
             deRegisterRegion(oldRegion);
             allRegion.remove(oldRegion);
             regionRepository.save(region);
             allRegion.add(region);
             registerRegion(region);
         });
    }
    public void registerRegion(Region region) {
        region.createParticle();
        allRegion.add(region);
    }
}
