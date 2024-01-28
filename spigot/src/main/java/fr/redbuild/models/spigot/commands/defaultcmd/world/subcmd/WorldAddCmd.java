package fr.redbuild.models.spigot.commands.defaultcmd.world.subcmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultarguments.StringArgument;
import fr.redbuild.models.spigot.logger.CtMsg;
import fr.redbuild.models.spigot.world.WorldManager;

public class WorldAddCmd extends SubCmd{

    @Autowired
    private WorldManager worldManager;

    public WorldAddCmd(Boolean optional) {
        super("add", optional);
        rc(1, new StringArgument("name", false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        worldManager.addWorld(argument.get(1,String.class),argument.get(1,String.class),argument.get(1,String.class),"");
        CtMsg.sendMiniMessage("<green>Le monde <gold>"+argument.get(1,String.class)+"<green> a été ajouté", sender);
        if(sender instanceof Player player){
            worldManager.teleport(player, argument.get(1, String.class));
            CtMsg.sendMiniMessage("<green>Vous avez été téléporté dans le monde <gold>"+argument.get(1, String.class), sender);
        }
    }
    
}
