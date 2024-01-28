package fr.redbuild.models.spigot.commands.defaultcmd.region.subcmd;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.region.RegionController;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pos1 extends SubCmd {
    @Autowired
    private RegionController regionController;
    @Autowired
    private MiniMessage mm;
    public Pos1() {
        super("pos1", false);
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player) {
            regionController.setPos1(player);
            player.sendMessage(mm.deserialize("<green>Position 1 définie"));
        }
    }
}
