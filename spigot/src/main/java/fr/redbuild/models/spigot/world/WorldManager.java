package fr.redbuild.models.spigot.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.logger.CtMsg;
import fr.redbuild.models.spigot.utils.injector.Injector;

public class WorldManager {
    @Autowired
    private WorldRepository worldRepository;

    private List<SWorld> allWorld = new ArrayList<>();

    public void init(){
        if(worldRepository == null)
            worldRepository = new WorldRepository();
        Injector.inject(worldRepository);
        worldRepository.findAll().forEach(world -> {
            Bukkit.createWorld(new WorldCreator(world.getName()));
            allWorld.add(world);
            CtMsg.log("§5Le monde §6" + world.getName() + "§5 a été chargé !");
        });
    }

    public SWorld getLoadedWorld(String name){
        return allWorld.stream().filter(world -> world.getName().equals(name)).findAny().orElse(null);
    }

    public boolean isLoaded(String name){
        return allWorld.stream().anyMatch(world -> world.getName().equals(name));
    }

    public SWorld getWorld(String name){
        return worldRepository.findAll().stream().filter(world -> world.getName().equals(name)).findAny().orElse(null);
    }

    public SWorld getWorldByID(String uuid){
        return worldRepository.findAll().stream().filter(world -> world.getUuid().toString().equals(uuid)).findAny().orElse(null);
    }

    public void addWorld(String name,String displayname,String description,String permission){
        if(getWorld(name) != null)
            return;
        SWorld world = new SWorld(java.util.UUID.randomUUID(), name, displayname, description, permission);
        Bukkit.createWorld(new WorldCreator(name));
        worldRepository.save(world);
        allWorld.add(world);
    }

    public void removeWorld(String name){
        SWorld world = getWorld(name);
        if(world != null){
            worldRepository.delete(world);
            allWorld.remove(world);
            Bukkit.getWorld(name).getPlayers().forEach(p -> CtMsg.sendMiniMessage("<red>Fermeture du monde !", p));
            if(isLoaded(name))
                Bukkit.unloadWorld(Bukkit.getWorld(name), true);
        }
    }

    public void removeWorld(SWorld world){
        if(world != null){
            worldRepository.delete(world);
            allWorld.remove(world);
            Bukkit.getWorld(world.getName()).getPlayers().forEach(p -> CtMsg.sendMiniMessage("<red>Fermeture du monde !", p));
            if(isLoaded(world.getName()))
                Bukkit.unloadWorld(Bukkit.getWorld(world.getName()), true);
        }
    }

    public void teleport(Player player, String worldName){
        if(getWorld(worldName) != null){
            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
        }
    }
}
