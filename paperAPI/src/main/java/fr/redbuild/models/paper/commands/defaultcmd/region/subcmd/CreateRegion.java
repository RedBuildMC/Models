package fr.redbuild.models.paper.commands.defaultcmd.region.subcmd;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.commands.SubCmd.SubCmd;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.commands.defaultarguments.StringArgument;
import fr.redbuild.models.paper.region.RegionController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRegion extends SubCmd {
    @Autowired
    private RegionController regionController;
    public CreateRegion() {
        super("create", false);
        rc(1,new StringArgument("name",false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player)
            regionController.createRegion(argument.get(1,String.class),player);
    }
}
