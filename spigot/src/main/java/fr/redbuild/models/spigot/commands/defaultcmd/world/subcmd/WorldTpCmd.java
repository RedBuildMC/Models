package fr.redbuild.models.spigot.commands.defaultcmd.world.subcmd;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultcmd.world.arguments.WorldArgument;
import fr.redbuild.models.spigot.logger.CtMsg;

public class WorldTpCmd extends SubCmd{

    public WorldTpCmd(Boolean optional) {
        super("tp", optional);
        rc(1, new WorldArgument("world", false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player)
            if(argument.getOptional(1, World.class).isPresent()){
                player.teleport(argument.get(1, World.class).getSpawnLocation());
                CtMsg.sendMiniMessage("<green>Vous avez été téléporté dans le monde <gold>"+argument.get(1, World.class).getName(), sender);
            }else{
                CtMsg.sendMiniMessage("<red>Ce monde n'existe pas", sender);
            }
            
    }
    
}
