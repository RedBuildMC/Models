package fr.redbuild.models.spigot.commands.defaultcmd.permission.subcmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.defaultarguments.PlayerArgument;
import fr.redbuild.models.spigot.commands.defaultcmd.permission.argument.PermissionArgument;
import fr.redbuild.models.spigot.utils.permission.PermissionManager;

public class PermissionAddCmd extends SubCmd {
    @Autowired
    private PermissionManager permissionManager;

    public PermissionAddCmd(Boolean optional) {
        super("add", optional);
        rc(1, new PermissionArgument("permission", false));
        rc(2, new PlayerArgument("player", false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
            Player player = argument.get(2, Player.class);
            String permission = argument.get(1, String.class);
            permissionManager.addPermission(permission, player);
            sender.sendMessage("Vous avez ajouté la permission " + permission + " à " + player.getName());
    }
}
