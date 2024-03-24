package fr.redbuild.models.paper.npc;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.plugin.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCController extends ServerInfo{
    @Autowired
    private NPCRepository npcRepository;
    public List<NPC> allNPC = new ArrayList<>();
    public void initialize(){
        npcRepository.findAll().forEach(npc -> {
            registerNPC(npc);
            npc.init();
            npc.spawn();
        });
    }

    public NPC getNPC(UUID uuid){
        return allNPC.stream().filter(npc -> npc.getNpcUUID() == uuid).findAny().orElse(null);
    }

    public NPC getNPC(int entityID){
        return allNPC.stream().filter(npc -> npc.getEntityID() == entityID).findAny().orElse(null);
    }

    public void registerNPC(NPC npc){
        allNPC.add(npc);
    }

    public void deRegisterNPC(NPC npc){
        allNPC.remove(npc);
    }

    @Override
    public void serverStart() {
        initialize();
    }

    @Override
    public void serverStop() {
        allNPC.forEach(npc -> npcRepository.save(npc));
    }
}
