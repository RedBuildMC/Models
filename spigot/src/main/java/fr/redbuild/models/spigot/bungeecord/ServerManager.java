package fr.redbuild.models.spigot.bungeecord;

import fr.redbuild.models.spigot.plugin.PluginController;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * The ServerManager class is responsible for managing server connections for players.
 */
public class ServerManager {
    
    /**
     * Connects a player to the specified server.
     * 
     * @param server the name of the server to connect to
     * @param player the player to connect
     */
    public void connectPlayer(String server, Player player){
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(PluginController.INSTANCE, "BungeeCord", out.toByteArray());
    }
}
