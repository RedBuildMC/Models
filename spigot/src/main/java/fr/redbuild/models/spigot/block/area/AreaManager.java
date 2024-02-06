package fr.redbuild.models.spigot.block.area;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.block.Area;

public class AreaManager {
    @Autowired
    private AreaRepository areaRepository;

    private List<Area> areas = new ArrayList<>();

    private Map<Player,Location> pos1 = new HashMap<>();

    private Map<Player,Location> pos2 = new HashMap<>();

    public void createArea(Location start,Location end,String name){
        Area area = new Area(start,end);
        area.addAttribute("name", name);
        areas.add(area);
    }

    public void createArea(Player player,String name){
        if(pos1.containsKey(player) && pos2.containsKey(player)){
            createArea(pos1.get(player),pos2.get(player),name);
        }
    }
    
    public void createArea(Player player){
        createArea(player,"area.of."+player.getName());
    }

    public void setPos1(Player player,Location location){
        pos1.put(player,location);
    }

    public void setPos2(Player player,Location location){
        pos2.put(player,location);
    }
    
}
