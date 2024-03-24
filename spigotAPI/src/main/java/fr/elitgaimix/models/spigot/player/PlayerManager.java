package fr.elitgaimix.models.spigot.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import fr.elitgaimix.models.spigot.Autowired.Autowired;
import fr.elitgaimix.models.spigot.npc.NPC;
import fr.elitgaimix.models.spigot.packet.PacketUtils;
import fr.elitgaimix.models.spigot.region.RegionController;
import fr.elitgaimix.models.spigot.scoreboard.ScoreBoardManager;
import org.bukkit.entity.Player;

public class PlayerManager {

    @Autowired
    private RegionController regionController;

    @Autowired
    private ScoreBoardManager scoreBoardManager;

    private final List<NPC> onlineNPC = new ArrayList<>();

    public void registerPlayer(Player player){
        regionController.registerPlayer(player);
        
        for(NPC npc : onlineNPC){
            if(npc.getNpcLocation().getWorld() == player.getWorld())
                npc.spawn(player);
        }
        PacketUtils.injectPlayer(player);
        scoreBoardManager.join(player);
    }

    public void worldChange(Player player){
        for(NPC npc : onlineNPC){
            if(npc.getNpcLocation().getWorld() == player.getWorld())
                npc.spawn(player);
            else
                npc.deSpawn(player);
        }
    }

    public int getFreeId(){
        return new Random().nextInt((5000 - 1000) + 1) + 1000;
    }

    public void deRegisterPlayer(Player player){
        regionController.deRegisterPlayer(player);
    }

    public NPC getNPC(UUID uuid){
        return onlineNPC.stream().filter(npc -> npc.getNpcUUID() == uuid).findAny().orElse(null);
    }

    public void registerNPC(NPC npc){
        onlineNPC.add(npc);
    }

    public void removeNPC(NPC npc){
        onlineNPC.remove(npc);
    }

    public void removeNPC(int npc){
        onlineNPC.stream().filter(npc1 -> npc1.getEntityID() == npc).findAny().ifPresent(onlineNPC::remove);
    }

    public boolean isRegister(int npc){
        return onlineNPC.stream().noneMatch(npc1 -> npc1.getEntityID() == npc);
    }
}
