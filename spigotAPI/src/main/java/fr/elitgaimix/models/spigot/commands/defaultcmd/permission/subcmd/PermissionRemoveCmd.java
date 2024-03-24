package fr.elitgaimix.models.spigot.commands.defaultcmd.permission.subcmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.elitgaimix.models.spigot.Autowired.Autowired;
import fr.elitgaimix.models.spigot.commands.SubCmd.SubCmd;
import fr.elitgaimix.models.spigot.commands.arg.Argument;
import fr.elitgaimix.models.spigot.commands.defaultarguments.PlayerArgument;
import fr.elitgaimix.models.spigot.commands.defaultcmd.permission.argument.PermissionArgument;
import fr.elitgaimix.models.spigot.utils.permission.PermissionManager;

public class PermissionRemoveCmd extends SubCmd{
    @Autowired
    private PermissionManager permissionManager;

    public PermissionRemoveCmd(Boolean optional) {
        super("remove", optional);
        rc(1, new PermissionArgument("permission", false));
        rc(2, new PlayerArgument("player", false));
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
            Player player = argument.getOptional(2, Player.class).get();
            String permission = argument.get(1, String.class);
            permissionManager.removePermission(permission, player);
            sender.sendMessage("Vous avez retiré la permission " + permission + " à " + player.getName());

    }
    
}
