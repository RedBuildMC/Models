package fr.elitgaimix.models.spigot.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.elitgaimix.models.spigot.handlers.InteractAtNpcHandler;
import fr.elitgaimix.models.spigot.listeners.handlers.HandlerListener;
import fr.elitgaimix.models.spigot.packet.PacketUtils;
import fr.elitgaimix.models.spigot.player.PlayerManager;
import fr.elitgaimix.models.spigot.utils.injector.Injector;
import fr.elitgaimix.models.spigot.utils.mongo.Id;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
@Getter
@Setter
public class NPC {
    
    @BsonIgnore
    private final NPCController npcController;
    @BsonIgnore
    private NPCRepository npcRepository;
    @BsonIgnore
    private final PlayerManager playerManager;
    @Id
    @BsonId
    private UUID npcUUID = UUID.randomUUID();
    @BsonProperty
    private String name;
    @BsonProperty
    private Skin skin;
    @BsonProperty
    private int entityID;
    @BsonProperty
    private Location npcLocation;
    @BsonProperty
    private boolean visibleName = false;
    @BsonProperty
    private Map<String, Object> attributes = new HashMap<>();

    @BsonIgnore
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
        npcRepository.save(this);
    }
    @BsonIgnore
    public void removeAttribute(String key) {
        attributes.remove(key);
        npcRepository.save(this);
    }
    @BsonIgnore
    public void setAttribute(String key, Object value) {
        attributes.replace(key, value);
        npcRepository.save(this);
    }
    @BsonIgnore
    public boolean hasAttribute(String key) {
        npcRepository.save(this);
        return attributes.containsKey(key);
    }
    @BsonIgnore
    public Object getAttribute(String key) {
        npcRepository.save(this);
        return attributes.get(key);
    }

    public void init(){
        if(hasAttribute("teleportation")){
            Location loc = (Location) documentToLocation((Document) getAttribute("teleportation"));
            click((player, event, uuid) -> player.teleport(loc));
        }
    }

    public Document locationToDocument(Location location) {
        Document document = new Document();
        document.put("x", location.getX());
        document.put("y", location.getY());
        document.put("z", location.getZ());
        document.put("world", location.getWorld().getName());
        return document;
    }

    public Location documentToLocation(Document document) {
        double x = document.getDouble("x");
        double y = document.getDouble("y");
        double z = document.getDouble("z");
        World world = Bukkit.getWorld(document.getString("world"));
        return new Location(world, x, y, z);
    }


    /**
     * Constructeur de la classe NPC.
     *
     * @param uuid        L'UUID du NPC.
     * @param name        Le nom du NPC.
     * @param skin        La skin du NPC.
     * @param entityID    L'ID de l'entité du NPC.
     * @param npcLocation La position du NPC.
     */
    public NPC(@NotNull UUID uuid, @NotNull String name, @NotNull Skin skin, int entityID, @NotNull Location npcLocation) {
        this.name = name;
        this.skin = skin;
        this.entityID = entityID;
        npcUUID = uuid;
        this.npcLocation = npcLocation;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        npcRepository.save(this);
    }

    /**
     * Constructeur de la classe NPC.
     *
     * @param name         Le nom du NPC.
     * @param textureValue La valeur de la texture du NPC.
     * @param textureSignature La signature de la texture du NPC.
     * @param entityID     L'ID de l'entité du NPC.
     * @param npcLocation  La position du NPC.
     */
    public NPC(@NotNull String name, @NotNull String textureValue, @NotNull String textureSignature, int entityID, @NotNull Location npcLocation) {
        this.name = name;
        skin = new Skin(textureValue, textureSignature);
        this.entityID = entityID;
        npcUUID = UUID.randomUUID();
        this.npcLocation = npcLocation;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        npcRepository.save(this);
    }

    /**
     * Constructeur de la classe NPC.
     *
     * @param name        Le nom du NPC.
     * @param player      Le joueur dont la skin sera utilisée pour le NPC.
     * @param entityID    L'ID de l'entité du NPC.
     * @param npcLocation La position du NPC.
     */
    public NPC(@NotNull String name, @NotNull Player player, @NotNull int entityID, @NotNull Location npcLocation) {
        this.name = name;
        this.entityID = entityID;
        npcUUID = UUID.randomUUID();
        this.npcLocation = npcLocation;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        textureByPlayer(player);
        npcRepository.save(this);
    }

    public NPC(){
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
    }

    /**
     * Constructeur de la classe NPC.
     *
     * @param player      Le joueur dont la skin sera utilisée pour le NPC.
     * @param entityID    L'ID de l'entité du NPC.
     * @param npcLocation La position du NPC.
     */
    public NPC(@NotNull Player player, @NotNull int entityID, @NotNull Location npcLocation) {
        this.name = player.getName();
        this.entityID = entityID;
        npcUUID = UUID.randomUUID();
        this.npcLocation = npcLocation;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        textureByPlayer(player);
        npcRepository.save(this);
    }

    public NPC(@NotNull Player player,@NotNull Location location){
        this.name = player.getName();
        this.npcLocation = location;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        this.entityID = playerManager.getFreeId();
        textureByPlayer(player);
        npcRepository.save(this);
    }


    /**
     * Définit la texture du NPC en utilisant la skin du joueur spécifié.
     *
     * @param player Le joueur dont la skin sera utilisée pour le NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC textureByPlayer(Player player) {
        Property property = ((CraftPlayer) player).getHandle().getGameProfile().getProperties().get("textures").iterator().next();
        skin = new Skin(property.getValue(), property.getSignature());
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit l'action à effectuer lors du clic sur le NPC.
     *
     * @param handler Le gestionnaire d'interaction avec le NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC click(InteractAtNpcHandler handler) {
        HandlerListener.registerInteractAtNpcHandler(npcUUID, handler);
        return this;
    }

    /**
     * Définit l'action à effectuer lors du clic sur le NPC.
     *
     * @param handler L'action à exécuter lors du clic sur le NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC click(Runnable handler) {
        HandlerListener.registerInteractAtNpcHandler(npcUUID, (player, event, uuid) -> handler.run());
        return this;
    }

    /**
     * Fait apparaître le NPC pour tous les joueurs.
     */
    @BsonIgnore
    public void spawn() {
        ServerPlayer npc = toServerPlayer();
        PacketUtils.sendPacketToAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        PacketUtils.sendPacketToAll(new ClientboundAddPlayerPacket(npc));
        PacketUtils.sendPacketToAll(new ClientboundTeleportEntityPacket(npc));
        PacketUtils.sendPacketToAll(new ClientboundMoveEntityPacket.Rot(npc.getId(), PacketUtils.toRawYaw(npcLocation.getYaw()), PacketUtils.toRawYaw(npcLocation.getPitch()), true));
        PacketUtils.sendPacketToAll(new ClientboundRotateHeadPacket(npc, PacketUtils.toRawYaw(npcLocation.getYaw())));
        PacketUtils.sendPacketToAll(new ClientboundAnimatePacket(npc, ClientboundAnimatePacket.SWING_MAIN_HAND));
        npcController.registerNPC(this);
    }

    /**
     * Fait apparaître le NPC pour un joueur spécifique.
     *
     * @param player Le joueur pour lequel le NPC doit apparaître.
     */
    @BsonIgnore
    public void spawn(Player player) {
        ServerPlayer npc = toServerPlayer();
        PacketUtils.sendPacket(player, new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        PacketUtils.sendPacket(player, new ClientboundAddPlayerPacket(npc));
        PacketUtils.sendPacket(player, new ClientboundTeleportEntityPacket(npc));
        PacketUtils.sendPacket(player, new ClientboundMoveEntityPacket.Rot(npc.getId(), PacketUtils.toRawYaw(npcLocation.getYaw()), PacketUtils.toRawYaw(npcLocation.getPitch()), true));
        PacketUtils.sendPacket(player, new ClientboundRotateHeadPacket(npc, PacketUtils.toRawYaw(npcLocation.getYaw())));
        PacketUtils.sendPacket(player, new ClientboundAnimatePacket(npc, ClientboundAnimatePacket.SWING_MAIN_HAND));

        npcController.registerNPC(this);
    }

    /**
     * Fait disparaître le NPC pour un joueur spécifique.
     *
     * @param player Le joueur pour lequel le NPC doit disparaître.
     */
    @BsonIgnore
    public void deSpawn(Player player) {
        PacketUtils.sendPacket(player, new ClientboundPlayerInfoRemovePacket(List.of(this.npcUUID)));
        PacketUtils.sendPacket(player, new ClientboundRemoveEntitiesPacket(this.entityID));
        npcController.deRegisterNPC(this);
    }

    /**
     * Fait disparaître le NPC pour tous les joueurs.
     */
    @BsonIgnore
    public void deSpawn() {
        playerManager.removeNPC(this.entityID);
        HandlerListener.deRegisterInteractAtNpcHandler(this);
        PacketUtils.sendPacketToAll(new ClientboundPlayerInfoRemovePacket(List.of(this.npcUUID)));
        PacketUtils.sendPacketToAll(new ClientboundRemoveEntitiesPacket(this.entityID));
        npcController.deRegisterNPC(this);
    }

    /**
     * Met à jour l'apparence du NPC pour un joueur spécifique.
     *
     * @param player Le joueur pour lequel le NPC doit être mis à jour.
     */
    @BsonIgnore
    public void update(Player player) {
        deSpawn(player);
        spawn(player);
    }

    /**
     * Met à jour l'apparence du NPC pour tous les joueurs.
     */
    @BsonIgnore
    public void update() {
        deSpawn();
        spawn();
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
    }

    /**
     * Supprime le NPC.
     */
    @BsonIgnore
    public void delete() {
        deSpawn();
        if (HandlerListener.isRegister(this))
            HandlerListener.deRegisterInteractAtNpcHandler(this);
        if (!playerManager.isRegister(this.entityID))
            playerManager.registerNPC(this);
            if(npcRepository == null)
                npcRepository = Injector.getInstance(NPCRepository.class);
        npcController.deRegisterNPC(this);
        npcRepository.delete(this);
    }

    @BsonIgnore
    @NotNull
    private ServerPlayer toServerPlayer() {
        if (playerManager.isRegister(this.entityID))
            playerManager.registerNPC(this);
        GameProfile gameProfile = new GameProfile(npcUUID, name);
        ServerLevel nmsWorld = ((CraftWorld) npcLocation.getWorld()).getHandle();
        ServerPlayer npc = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), nmsWorld, gameProfile);
        npc.setPos(npcLocation.getX(), npcLocation.getY(), npcLocation.getZ());
        npc.setXRot(npcLocation.getPitch());
        npc.setYRot(npcLocation.getYaw());
        npc.setYBodyRot(npcLocation.getYaw());
        npc.setYHeadRot(npcLocation.getYaw());
        npc.setId(this.entityID);
        npc.listName = Component.empty();
        npc.gameProfile.getProperties().removeAll("textures");
        npc.gameProfile.getProperties().put("textures", new Property("textures", skin.getTextureValue(), skin.getTextureSignature()));
        return npc;
    }

    public String toString(){
        return "NPC{" +
                "npcUUID=" + npcUUID +
                ", name='" + name + '\'' +
                ", skin=" + skin +
                ", entityID=" + entityID +
                ", npcLocation=" + npcLocation +
                ", visibleName=" + visibleName +
                ", attributes=" + attributes +
                '}';
    }
}
