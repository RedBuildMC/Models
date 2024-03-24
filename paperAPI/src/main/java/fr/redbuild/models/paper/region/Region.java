package fr.redbuild.models.paper.region;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R3.CraftParticle;
import org.bukkit.entity.Player;

import fr.redbuild.models.paper.plugin.PluginController;
import fr.redbuild.models.paper.utils.mongo.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Region {
        @Id
        @BsonId
        private UUID uuid = UUID.randomUUID();
        @BsonProperty
        private String name = "default";
        @BsonProperty
        private Location start;
        @BsonProperty
        private Location end;
            @BsonProperty
    private String serverName = PluginController.INSTANCE.getServerName();
        @BsonProperty
        private Region sousRegion = null;
        @BsonProperty("visibility")
        public boolean visibility;
        @BsonProperty
        private Map<String, String> attributes = new HashMap<>();
        @BsonIgnore
        private transient List<Packet<?>> particles = new ArrayList<>();

        @BsonIgnore
        public boolean isInRegion(Player player) {
                return contains(player.getLocation());
        }

        public boolean isInRegion(Location location) {
                return contains(location);
        }


        public Region() {
                start = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
                end = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
        }

        public Region(Boolean ignored) {
                start = null;
                end = null;
        }

        public Region(Location start, Location end) {
                this.start = start;
                this.end = end;
        }

        public Region(Location start, Location end, Region sousRegion,String name,UUID uuid,boolean visibilite) {
                this.start = start;
                this.end = end;
                this.sousRegion = sousRegion;
                this.name = name;
                this.uuid = uuid;
                this.visibility = visibilite;
        }

        @BsonIgnore
        public void addAttribute(String key, String value) {
            attributes.put(key, value);
        }
        @BsonIgnore
        public void removeAttribute(String key) {
            attributes.remove(key);
        }
        @BsonIgnore
        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }
        @BsonIgnore
        public boolean hasAttribute(String key) {
            return attributes.containsKey(key);
        }
        @BsonIgnore
        public String getAttribute(String key) {
            return attributes.get(key);
        }

        public boolean hasSousRegion() {
                return sousRegion != null;
        }
        public Region setVisibility(boolean visibility) {
                this.visibility = visibility;
                return this;
        }

        @BsonIgnore
        public boolean contains(Location location) {
                var minX = Math.min(start.getBlockX(), end.getBlockX());
                var minY = Math.min(start.getBlockY(), end.getBlockY());
                var minZ = Math.min(start.getBlockZ(), end.getBlockZ());

                var maxX = Math.max(start.getBlockX(), end.getBlockX());
                var maxY = Math.max(start.getBlockY(), end.getBlockY());
                var maxZ = Math.max(start.getBlockZ(), end.getBlockZ());
                if(location.getWorld() == null || end.getWorld() == null)
                        return false;
                return location.getWorld().getName().equals(end.getWorld().getName()) && location.getX() >= minX
                                && location.getY() >= minY && location.getZ() >= minZ
                                && location.getX() <= maxX && location.getY() <= maxY
                                && location.getZ() <= maxZ;
        }

        public String toString() {
                // StringBuilder builder = new StringBuilder();
                // builder.append("Region Start: ").append(start.toString()).append("\n");
                // builder.append("Region End: ").append(end.toString()).append("\n");
                // builder.append("Region SousRegion: ").append(sousRegion).append("\n");
                // builder.append("Region Name: ").append(name).append("\n");
                // builder.append("Region UUID: ").append(uuid).append("\n");
                // builder.append("Region Visibility: ").append(visibility).append("\n");
                // return builder.toString();
                return "region(" + start.toString() + "," + end.toString() + "," + sousRegion + "," + name + "," + uuid + "," + visibility + ")";
        }       

        @BsonIgnore
        public void createParticle() {
                if(!visibility)
                        return;
                var minX = Math.min(start.getBlockX(), end.getBlockX());
                var minZ = Math.min(start.getBlockZ(), end.getBlockZ());
                var minY = Math.min(start.getBlockY(), end.getBlockY());

                var maxX = Math.max(start.getBlockX(), end.getBlockX());
                var maxZ = Math.max(start.getBlockZ(), end.getBlockZ());
                var maxY = Math.max(start.getBlockY(), end.getBlockY());

                if (this.particles == null) {
                        this.particles = new ArrayList<>();
                } else {
                        this.particles.clear();
                }
                ParticleOptions param = CraftParticle.toNMS(
                                Particle.REDSTONE, new Particle.DustOptions(Color.RED, 4f));
                for (int x = minX; x < (int) maxX; x += 8) {
                        // Min Y, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, minY, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max Y, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, maxY, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Min Y, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, minY, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max Y, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, maxY, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                }

                for (int z = minZ; z < (int) maxZ; z += 8) {
                        // Min X, Min Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, minY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Min Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, minY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Min X, Max Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, maxY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Max Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, maxY, z, 0f,
                                        0f, 0f, 0f, 1));
                }

                for (int y = minY; y < maxY; y += 8) {
                        // Min X, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, y, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, y, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Min X, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, y, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, y, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                }

                param = CraftParticle.toNMS(
                                Particle.REDSTONE, new Particle.DustOptions(Color.GRAY, 2f));
                for (int x = minX; x < maxX; x += 4) {
                        // Min Y, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, minY, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max Y, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, maxY, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Min Y, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, minY, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max Y, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, x, maxY, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                }

                for (int z = minZ; z < maxZ; z += 4) {
                        // Min X, Min Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, minY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Min Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, minY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Min X, Max Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, maxY, z, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Max Y
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, maxY, z, 0f,
                                        0f, 0f, 0f, 1));
                }

                for (int y = minY; y < maxY; y += 4) {
                        // Min X, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, y, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Min Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, y, minZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Min X, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, minX, y, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                        // Max X, Max Z
                        this.particles.add(new ClientboundLevelParticlesPacket(
                                        param, true, maxX, y, maxZ, 0f,
                                        0f, 0f, 0f, 1));
                }

        }

}
