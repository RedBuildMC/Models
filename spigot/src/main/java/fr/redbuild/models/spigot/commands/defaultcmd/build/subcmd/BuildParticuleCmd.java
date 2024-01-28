package fr.redbuild.models.spigot.commands.defaultcmd.build.subcmd;

import org.bukkit.command.CommandSender;

import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultarguments.BooleanArgument;

public class BuildParticuleCmd extends SubCmd{

    public BuildParticuleCmd(Boolean optional) {
        super("particule", optional);
        rc(1, new BooleanArgument(false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
    }
    
}
