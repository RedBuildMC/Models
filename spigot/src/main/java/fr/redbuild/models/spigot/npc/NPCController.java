package fr.redbuild.models.spigot.npc;

import fr.redbuild.models.spigot.Autowired.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCController {
    @Autowired
    private NPCRepository npcRepository;
    public List<NPC> allNPC = new ArrayList<>();
    public void initialize(){
        npcRepository.findAll().forEach(npc -> {
            registerNPC(npc);
            npc.spawn();
        });
    }

    public NPC getNPC(UUID uuid){
        return allNPC.stream().filter(npc -> npc.npcUUID == uuid).findAny().orElse(null);
    }

    public NPC getNPC(int entityID){
        return allNPC.stream().filter(npc -> npc.entityID == entityID).findAny().orElse(null);
    }

    public void registerNPC(NPC npc){
        allNPC.add(npc);
    }

    public void deRegisterNPC(NPC npc){
        allNPC.remove(npc);
    }
}
