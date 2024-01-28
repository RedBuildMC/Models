package fr.redbuild.models.spigot.commands.defaultcmd.region.subcmd;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultarguments.RegionArgument;
import fr.redbuild.models.spigot.region.Region;
import fr.redbuild.models.spigot.region.RegionController;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class DeleteRegion extends SubCmd {
    @Autowired
    private MiniMessage mm;
    @Autowired
    private RegionController regionController;
    public DeleteRegion(Boolean optional) {
        super("delete", optional);
        rc(1,new RegionArgument("region",false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(argument.getOptional(1, Region.class).isEmpty()) {
            sender.sendMessage(mm.deserialize("<red>Cette region n'existe pas"));
            return;
        }
        regionController.deleteRegion(argument.get(1, Region.class));
        sender.sendMessage(mm.deserialize("<green>Region supprime"));
    }
}
