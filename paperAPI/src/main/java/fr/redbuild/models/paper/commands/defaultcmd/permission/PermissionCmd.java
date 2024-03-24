package fr.redbuild.models.paper.commands.defaultcmd.permission;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.commands.Cmd;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.commands.defaultarguments.PlayerArgument;
import fr.redbuild.models.paper.commands.defaultcmd.permission.subcmd.PermissionAddCmd;
import fr.redbuild.models.paper.commands.defaultcmd.permission.subcmd.PermissionRemoveCmd;
import fr.redbuild.models.paper.logger.CtMsg;
import fr.redbuild.models.paper.utils.permission.PermissionManager;

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
