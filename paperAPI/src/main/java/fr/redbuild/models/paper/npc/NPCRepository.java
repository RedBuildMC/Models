package fr.redbuild.models.paper.npc;

import fr.redbuild.models.paper.mongo.repository.MongoDBRepository;
import fr.redbuild.models.paper.plugin.PluginController;

import java.util.UUID;

public class NPCRepository extends MongoDBRepository<NPC, UUID> {
    public NPCRepository() {
        super(NPC.class, "npc", PluginController.INSTANCE.dbName());
    }
}
