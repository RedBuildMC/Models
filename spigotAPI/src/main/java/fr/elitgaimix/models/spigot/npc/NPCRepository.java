package fr.elitgaimix.models.spigot.npc;

import fr.elitgaimix.models.spigot.mongo.repository.MongoDBRepository;
import fr.elitgaimix.models.spigot.plugin.PluginController;

import java.util.UUID;

public class NPCRepository extends MongoDBRepository<NPC, UUID> {
    public NPCRepository() {
        super(NPC.class, "npc", PluginController.INSTANCE.dbName());
    }
}
