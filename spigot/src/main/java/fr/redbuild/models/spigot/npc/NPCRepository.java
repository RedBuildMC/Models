package fr.redbuild.models.spigot.npc;

import fr.redbuild.models.spigot.mongo.repository.MongoDBRepository;
import fr.redbuild.models.spigot.plugin.PluginController;

import java.util.UUID;

public class NPCRepository extends MongoDBRepository<NPC, UUID> {
    public NPCRepository() {
        super(NPC.class, "npc", PluginController.INSTANCE.dbName());
    }
}
