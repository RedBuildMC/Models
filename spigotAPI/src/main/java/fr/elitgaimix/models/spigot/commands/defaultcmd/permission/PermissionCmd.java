package fr.elitgaimix.models.spigot.commands.defaultcmd.permission;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.elitgaimix.models.spigot.Autowired.Autowired;
import fr.elitgaimix.models.spigot.commands.Cmd;
import fr.elitgaimix.models.spigot.commands.arg.Argument;
import fr.elitgaimix.models.spigot.commands.defaultarguments.PlayerArgument;
import fr.elitgaimix.models.spigot.commands.defaultcmd.permission.subcmd.PermissionAddCmd;
import fr.elitgaimix.models.spigot.commands.defaultcmd.permission.subcmd.PermissionRemoveCmd;
import fr.elitgaimix.models.spigot.logger.CtMsg;
import fr.elitgaimix.models.spigot.utils.permission.PermissionManager;

public class PermissionCmd extends Cmd{
    @Autowired
    private PermissionManager permissionManager;

    public PermissionCmd() {
        super("permission", "manage permission of players");
        rc(1, new PermissionAddCmd(false));
        rc(1, new PermissionRemoveCmd(false));
        rc(1, new PlayerArgument("player", false));
        setPermission("redbuild.models.permission");
    }

    @Override
    public void execute(CommandSender sender, String command, Argument args) {
        if(args.getOptional(1,Player.class).isPresent()){
            List<String> perms = permissionManager.getPermission(args.getOptional(1,Player.class).get());
            CtMsg.sendMiniMessage("<gold>Permission of players <green>" + args.get(1, Player.class).getName() + " <gold>" + perms.toString(), sender);
        }
    }
    
}
