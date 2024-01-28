package fr.redbuild.models.spigot.bungeecord;

import fr.redbuild.models.spigot.plugin.PluginController;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerManager {
    public static void connectPlayer(String server,Player player){
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(PluginController.INSTANCE, "BungeeCord", out.toByteArray());
    }
}
