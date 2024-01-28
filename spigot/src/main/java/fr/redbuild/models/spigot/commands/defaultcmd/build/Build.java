package fr.redbuild.models.spigot.commands.defaultcmd.build;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.Cmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultarguments.PlayerArgument;
import fr.redbuild.models.spigot.mode.BuildMode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build extends Cmd {
    @Autowired
    private BuildMode buildMode;
    @Autowired
    private MiniMessage mm;
    public Build() {
        super("build", "Build mode");
        rc(1,new PlayerArgument("player",true));
        setPermission("redbuild.models.build");
    }

    @Override
    public void execute(CommandSender sender, String command, Argument args) {
        if(args.size() != 0 ){
            if(args.getOptional(1,Player.class).isPresent()) {
                Player player = args.get(1,Player.class);
                if (buildMode.contains(player)) {
                    buildMode.deRegister(player);
                    player.sendMessage(mm.deserialize("<gold>" + sender.getName() + "<red> Vous a désactivé le mode build !"));
                    sender.sendMessage(mm.deserialize("<red>Mode build désactivé pour <gold>" + player.getName()));
                } else {
                    buildMode.register(player);
                    player.sendMessage(mm.deserialize("<gold>" + sender.getName() + "<green> Vous a activé le mode build !"));
                    sender.sendMessage(mm.deserialize("<green>Mode build activé pour <gold>" + player.getName()));
                }
                return;
            }
        }
        if(sender instanceof Player player)
            if(buildMode.contains(player)){
                buildMode.deRegister(player);
                player.sendMessage(mm.deserialize("<red>Mode build désactivé !"));
            }else{
                buildMode.register(player);
                player.sendMessage(mm.deserialize("<green>Mode build activé !"));
            }

    }
}
