package fr.elitgaimix.models.spigot.logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.elitgaimix.models.spigot.utils.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CtMsg {
    @Getter
    @Setter
    private static String minecraftPrefix = "§4§lRed§6§lBuild §7» §r";
    @Getter
    @Setter
    private static String miniMessagePrefix = "<dark_red>Red<gold>Build <gray>» ";

    private static MiniMessage mm;

    public static void log(String message){
        Bukkit.getLogger().info(minecraftPrefix + message);
    }

    public static void waring(String message){
        Bukkit.getLogger().warning(message);
    }

    public static void error(String message){
        Bukkit.getLogger().severe(message);
    }

    public static void sendMessage(String message,Player player){
        player.sendMessage(minecraftPrefix + message);
    }

    public static void sendMiniMessage(String message,Player player){
        player.sendMessage(mm.deserialize(miniMessagePrefix + message));
    }

    public static void sendMessage(String message,CommandSender player){
        player.sendMessage(minecraftPrefix + message);
    }

    public static void sendMiniMessage(String message,CommandSender player){
        player.sendMessage(mm.deserialize(miniMessagePrefix + message));
    }

    @Deprecated
    public static void broadcast(String message){
        Bukkit.getServer().broadcastMessage(minecraftPrefix + message);
    }

    public static void broadcastMiniMessage(String message){
        Bukkit.getServer().broadcast(mm.deserialize(miniMessagePrefix + message));
    }

    public static void init(){
        mm = Injector.getInstance(MiniMessage.class);
        log("§aLogger init");
        log("LogMessage");
        waring("WaringMessage");
        error("ErrorMessage");
        log("§aLogger init success");
    }
}
