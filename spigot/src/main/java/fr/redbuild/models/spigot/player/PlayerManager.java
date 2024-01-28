package fr.redbuild.models.spigot.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.npc.NPC;
import fr.redbuild.models.spigot.packet.PacketUtils;
import fr.redbuild.models.spigot.region.RegionController;
import fr.redbuild.models.spigot.scoreboard.ScoreBoardManager;
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
            npc.spawn(player);
        }
        PacketUtils.injectPlayer(player);
        scoreBoardManager.join(player);
    }

    public int getFreeId(){
        return onlineNPC.stream().mapToInt(npc -> npc.entityID).max().orElse(0) + 1;
    }

    public void deRegisterPlayer(Player player){
        regionController.deRegisterPlayer(player);
    }

    public NPC getNPC(UUID uuid){
        return onlineNPC.stream().filter(npc -> npc.npcUUID == uuid).findAny().orElse(null);
    }

    public void registerNPC(NPC npc){
        onlineNPC.add(npc);
    }

    public void removeNPC(NPC npc){
        onlineNPC.remove(npc);
    }

    public void removeNPC(int npc){
        onlineNPC.stream().filter(npc1 -> npc1.entityID == npc).findAny().ifPresent(onlineNPC::remove);
    }

    public boolean isRegister(int npc){
        return onlineNPC.stream().noneMatch(npc1 -> npc1.entityID == npc);
    }
}
