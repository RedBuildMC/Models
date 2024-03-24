package fr.elitgaimix.models.spigot.commands.defaultcmd.region.subcmd;

import fr.elitgaimix.models.spigot.Autowired.Autowired;
import fr.elitgaimix.models.spigot.commands.SubCmd.SubCmd;
import fr.elitgaimix.models.spigot.commands.arg.Argument;
import fr.elitgaimix.models.spigot.region.RegionController;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pos2 extends SubCmd {
    @Autowired
    private MiniMessage mm;
    @Autowired
    private RegionController regionController;
    public Pos2() {
        super("pos2", false);
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player) {
            regionController.setPos2(player);
            player.sendMessage(mm.deserialize("<green>Position 2 définie"));
        }
    }
}
