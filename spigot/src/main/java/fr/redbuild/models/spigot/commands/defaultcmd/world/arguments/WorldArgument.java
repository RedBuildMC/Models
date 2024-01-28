package fr.redbuild.models.spigot.commands.defaultcmd.world.arguments;

import java.util.List;
import java.util.Optional;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import fr.redbuild.models.spigot.commands.arg.CmdArgumentWP;

public class WorldArgument extends CmdArgumentWP<World>{

    public WorldArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<World> getValue(List<String> textValue, CommandSender sender) {
        return Optional.ofNullable(sender.getServer().getWorld(textValue.get(0)));
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender, int pos) {
        return sender.getServer().getWorlds().stream().map(World::getName).toList();
    }
    
}
