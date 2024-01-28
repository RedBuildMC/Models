package fr.redbuild.models.spigot.commands.defaultcmd.world.arguments;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.arg.CmdArgumentWP;
import fr.redbuild.models.spigot.world.SWorld;
import fr.redbuild.models.spigot.world.WorldManager;
import fr.redbuild.models.spigot.world.WorldRepository;

public class SWorldArgument extends CmdArgumentWP<SWorld> {
    @Autowired
    private WorldManager worldManager;

    @Autowired
    private WorldRepository worldRepository;
    public SWorldArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<SWorld> getValue(List<String> textValue, CommandSender sender) {
        return Optional.ofNullable(worldManager.getWorld(textValue.get(0)));
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender, int pos) {
        return worldRepository.findAll().stream().map(SWorld::getName).toList();
    }

    
}