package fr.redbuild.models.paper.commands.defaultcmd.world.subcmd;

import org.bukkit.command.CommandSender;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.commands.SubCmd.SubCmd;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.commands.defaultcmd.world.arguments.SWorldArgument;
import fr.redbuild.models.paper.logger.CtMsg;
import fr.redbuild.models.paper.world.SWorld;
import fr.redbuild.models.paper.world.WorldManager;

public class WorldRemoveCmd extends SubCmd{
    @Autowired
    protected WorldManager worldManager;

    public WorldRemoveCmd(Boolean optional) {
        super("remove", optional);
        rc(1, new SWorldArgument("world", false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(argument.getOptional(1, SWorld.class).isPresent()){
            worldManager.removeWorld(argument.get(1, SWorld.class));
            CtMsg.sendMiniMessage("<green>Le monde <gold>"+argument.get(1, SWorld.class).getName()+"<green> a été supprimé", sender);
        }else{
            CtMsg.sendMiniMessage("<red>Ce monde n'existe pas", sender);
        }

    }
    
}
