package fr.redbuild.models.spigot.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.redbuild.models.spigot.handlers.InteractAtNpcHandler;
import fr.redbuild.models.spigot.listeners.handlers.HandlerListener;
import fr.redbuild.models.spigot.packet.PacketUtils;
import fr.redbuild.models.spigot.player.PlayerManager;
import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.utils.mongo.Id;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.UUID;

public class NPC {
    
    @BsonIgnore
    private final NPCController npcController;
    @BsonIgnore
    private NPCRepository npcRepository;
    @BsonIgnore
    private final PlayerManager playerManager;
    @Id
    @BsonId
    public UUID npcUUID = UUID.randomUUID();
    @BsonProperty
    public String name;
    @BsonProperty
    public Skin skin;
    @BsonProperty
    public int entityID;
    @BsonProperty
    public Location npcLocation;
    @BsonProperty
    public boolean visibleName = false;


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
        setTexture(player);
        npcRepository.save(this);
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
                setTexture(player);
        npcRepository.save(this);
    }

    public NPC(@NotNull Player player,@NotNull Location location){
        this.name = player.getName();
        this.npcLocation = location;
        npcController = Injector.getInstance(NPCController.class);
        npcRepository = Injector.getInstance(NPCRepository.class);
        playerManager = Injector.getInstance(PlayerManager.class);
        this.entityID = playerManager.getFreeId();
        setTexture(player);
        npcRepository.save(this);
    }

    /**
     * Définit le nom du NPC.
     *
     * @param name Le nom du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setName(String name) {
        this.name = name;
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit l'ID de l'entité du NPC.
     *
     * @param id L'ID de l'entité du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setEntityId(int id) {
        entityID = id;
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit si le nom du NPC est visible.
     *
     * @param visibleName Indique si le nom du NPC est visible.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setVisibleName(boolean visibleName) {
        this.visibleName = visibleName;
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la texture du NPC en utilisant la skin du joueur spécifié.
     *
     * @param player Le joueur dont la skin sera utilisée pour le NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setTexture(Player player) {
        setTexture(((CraftPlayer) player).getHandle().getGameProfile().getProperties().get("textures").iterator().next());
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la texture du NPC en utilisant la propriété spécifiée.
     *
     * @param prop La propriété contenant la valeur et la signature de la texture.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setTexture(@NotNull Property prop) {
        skin = new Skin(prop.getValue(), prop.getSignature());
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la texture du NPC.
     *
     * @param skin La skin du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setTexture(Skin skin) {
        this.skin = skin;
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit le monde du NPC.
     *
     * @param world Le monde du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setWorld(@NotNull World world) {
        npcLocation.setWorld(world);
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la coordonnée X du NPC.
     *
     * @param x La coordonnée X du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setX(double x) {
        npcLocation.setX(x);
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la coordonnée Y du NPC.
     *
     * @param y La coordonnée Y du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setY(double y) {
        npcLocation.setY(y);
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit la coordonnée Z du NPC.
     *
     * @param z La coordonnée Z du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setZ(double z) {
        npcLocation.setZ(z);
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit l'angle de rotation en hauteur du NPC.
     *
     * @param pitch L'angle de rotation en hauteur du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setPitch(float pitch) {
        npcLocation.setPitch(pitch);
        if(npcRepository == null)
            npcRepository = Injector.getInstance(NPCRepository.class);
        npcRepository.save(this);
        return this;
    }

    /**
     * Définit l'angle de rotation en direction du NPC.
     *
     * @param yaw L'angle de rotation en direction du NPC.
     * @return L'instance du NPC.
     */
    @BsonIgnore
    public NPC setYaw(float yaw) {
        npcLocation.setYaw(yaw);
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
        ServerPlayer npc = getServerPlayer();
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
        ServerPlayer npc = getServerPlayer();
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
        npcRepository.delete(this);
    }

    @BsonIgnore
    @NotNull
    private ServerPlayer getServerPlayer() {
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
}
