package fr.redbuild.models.spigot.packet;

import fr.redbuild.models.spigot.event.PlayerInteractAtNpcEvent;
import fr.redbuild.models.spigot.event.SignUpdateEvent;
import fr.redbuild.models.spigot.npc.NPCController;
import fr.redbuild.models.spigot.plugin.PluginController;
import fr.redbuild.models.spigot.utils.injector.Injector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import io.netty.channel.ChannelDuplexHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PacketUtils {
    private static final List<Player> injected = new ArrayList<>();
    private static NPCController npcController;
    public static void injectPlayer(Player player){
        if(npcController == null)
            npcController = Injector.getInstance(NPCController.class);
        if(!injected.contains(player)) {
            ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

                @Override
                public void channelRead(@NotNull ChannelHandlerContext channelHandlerContext, @NotNull Object msg) throws Exception {
                    if (msg instanceof Packet) {
                        Packet<?> p = (Packet<?>) msg;
                        if (p instanceof ServerboundInteractPacket packet) {
                            Bukkit.getScheduler()
                                    .runTask(PluginController.INSTANCE, () -> {
                                        // Executer dans le thread principal
                                        if(npcController.getNPC(packet.getEntityId()) != null) {
                                            var event = new PlayerInteractAtNpcEvent(player, packet.getEntityId(), player.isSneaking(), packet.getActionType(), packet);
                                            event.callEvent();
                                        }
                                    });
                        } else if (p instanceof ServerboundSignUpdatePacket packet) {
                            Bukkit.getScheduler()
                                    .runTask(PluginController.INSTANCE, () -> {
                                        // Executer dans le thread principal
                                        var event = new SignUpdateEvent(packet.getLines(), player, packet.getPos());
                                        event.callEvent();
                                    });
                        }

                    }
                    super.channelRead(channelHandlerContext, msg);
                }
            };

            ChannelPipeline pipeline = channel(player).pipeline();
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
            injected.add(player);
        }
    }

    public static byte toRawYaw(float yaw) {
        return (byte) (yaw * 256F / 360F);
    }

    public static void sendPacket(Player player,Packet<?> packet){
        getConnection(player).send(packet);
    }

    public static void sendPacket(List<Player> player,Packet<?> packet){
        player.forEach(p -> sendPacket(p, packet));
    }

    public static void sendPackets(Player player,List<Packet<?>> packets){
        packets.forEach(p -> sendPacket(player, p));
    }

    public static void sendPackets(List<Player> player,List<Packet<?>> packets){
        player.forEach(p -> sendPackets(p, packets));
    }

    public static void sendPacketToAll(Packet<?> packet){
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            sendPacket(player,packet);
        }
    }

    public static Channel channel(Player player){
        return getConnection(player).channel;
    }

    public static Connection getConnection(Player player){
        return packetConnection(player).connection;
    }
    public static ServerGamePacketListenerImpl packetConnection(Player player){
        return ((CraftPlayer) player).getHandle().connection;
    }
}
