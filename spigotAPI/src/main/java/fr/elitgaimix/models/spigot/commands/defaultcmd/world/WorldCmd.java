package fr.elitgaimix.models.spigot.commands.defaultcmd.world;

import org.bukkit.command.CommandSender;

import fr.elitgaimix.models.spigot.commands.Cmd;
import fr.elitgaimix.models.spigot.commands.arg.Argument;
import fr.elitgaimix.models.spigot.commands.defaultcmd.world.subcmd.WorldAddCmd;
import fr.elitgaimix.models.spigot.commands.defaultcmd.world.subcmd.WorldRemoveCmd;
import fr.elitgaimix.models.spigot.commands.defaultcmd.world.subcmd.WorldTpCmd;

public class WorldCmd extends Cmd{

    public WorldCmd() {
        super("world", "World manager command");
        setPermission("redbuild.models.world");
        rc(1, new WorldAddCmd(false));
        rc(1, new WorldRemoveCmd(false));
        rc(1, new WorldTpCmd(false));
    }

    @Override
    public void execute(CommandSender sender, String command, Argument args) {
    }
    
}
