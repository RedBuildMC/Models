package fr.redbuild.models.spigot.commands.defaultcmd.permission.argument;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.arg.CmdArgument;
import fr.redbuild.models.spigot.utils.permission.PermissionManager;

public class PermissionArgument extends CmdArgument<String>{
    @Autowired
    private PermissionManager permissionManager;
    
    public PermissionArgument(String name, Boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<String> getValue(String textValue, CommandSender sender) {
        return Optional.ofNullable(textValue);
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender) {
        return permissionManager.getAllPermission();
    }
}
