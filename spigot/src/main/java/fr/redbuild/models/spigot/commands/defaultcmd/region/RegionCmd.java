package fr.redbuild.models.spigot.commands.defaultcmd.region;

import fr.redbuild.models.spigot.commands.Cmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultcmd.region.subcmd.*;
import org.bukkit.command.CommandSender;

public class RegionCmd extends Cmd {
    public RegionCmd() {
        super("region", "Manage region");
        rc(1,new CreateRegion());
        rc(1,new EditRegion(false));
        rc(1,new DeleteRegion(false));
        rc(1,new Pos1());
        rc(1,new Pos2());
        setPermission("redbuild.models.region");
    }

    @Override
    public void execute(CommandSender sender, String command, Argument args) {
    }
}
