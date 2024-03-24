package fr.elitgaimix.models.spigot.packet;


import fr.elitgaimix.models.spigot.listeners.handlers.HandlerListener;
import fr.elitgaimix.models.spigot.handlers.SignHandler;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftSign;
import org.bukkit.entity.Player;


import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import org.bukkit.event.Listener;

import java.util.List;

public class SignUtils implements Listener {
    public static void openSign(Player player, String title, SignHandler handler){
        sendSignData(player, List.of(title, "", "", "").toArray(new String[0]),handler);
    }

    public static void openSign(Player player,String title, Runnable handler){
        openSign(player,title,((event, player1, text) -> handler.run()));
    }

    public static void openSign(Player player, List<String> text,SignHandler handler){
        sendSignData(player,text.toArray(new String[0]),handler);
    }

    public static void openSign(Player player,List<String> title, Runnable handler){
        openSign(player,title,((event, player1, text) -> handler.run()));
    }

    public static void openSign(Player player,String title,String preValue,SignHandler handler){
        sendSignData(player, List.of(title, preValue, "", "").toArray(new String[0]),handler);
    }
    public static void openSign(Player player,String title,String preValue,Runnable handler){
        sendSignData(player, List.of(title, preValue, "", "").toArray(new String[0]),(event,player1,text) -> handler.run());
    }

    public static void sendSignData(Player player, String[] lines,SignHandler handler) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("String line must be of length 4");
        }

        final BlockPos blockPosition = new BlockPos(
            player.getLocation().getBlockX(), 1,
            player.getLocation().getBlockZ());
        HandlerListener.registerSignHandler(blockPosition,handler);

        ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(
            blockPosition,
            Blocks.OAK_WALL_SIGN.defaultBlockState());
        PacketUtils.sendPacket(player,packet);

        SignBlockEntity sign = new SignBlockEntity(blockPosition,
            Blocks.OAK_WALL_SIGN.defaultBlockState());
        CraftSign<SignBlockEntity> craftSign = new CraftSign<>(player.getWorld(), sign);
        craftSign.setLine(0, lines[0]);
        craftSign.setLine(1, lines[1]);
        craftSign.setLine(2, lines[2]);
        craftSign.setLine(3, lines[3]);

        craftSign.applyTo(sign);

        PacketUtils.sendPacket(player,sign.getUpdatePacket());

        ClientboundOpenSignEditorPacket openSignEditor = new ClientboundOpenSignEditorPacket(
                blockPosition);
        PacketUtils.sendPacket(player,openSignEditor);
    }
}
