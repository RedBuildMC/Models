package fr.redbuild.models.paper.commands.defaultcmd.region.subcmd;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.commands.SubCmd.SubCmd;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.commands.defaultarguments.RegionArgument;
import fr.redbuild.models.paper.region.Region;
import fr.redbuild.models.paper.region.RegionController;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditRegion extends SubCmd {
    @Autowired
    private MiniMessage mm;
    @Autowired
    private RegionController regionController;
    public EditRegion(Boolean optional) {
        super("edit", optional);
        rc(1,new RegionArgument("region",false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player) {
            if (argument.getOptional(1, Region.class).isEmpty()) {
                player.sendMessage(mm.deserialize("<red>Cette region n'existe pas"));
                return;
            }
            regionController.openEditor(player, argument.get(1, Region.class));
            }
        }
}
