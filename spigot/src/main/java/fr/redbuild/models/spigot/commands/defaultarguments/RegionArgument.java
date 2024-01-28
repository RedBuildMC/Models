package fr.redbuild.models.spigot.commands.defaultarguments;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.commands.arg.CmdArgument;
import fr.redbuild.models.spigot.region.Region;
import fr.redbuild.models.spigot.region.RegionController;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegionArgument extends CmdArgument<Region> {

    @Autowired
    public RegionController regionController;
    
    public RegionArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<Region> getValue(String textValue, CommandSender sender) {
        return regionController.getRegion(textValue);
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender) {
        List<String> result = new ArrayList<>();
        regionController.getRegions().forEach(reg -> result.add(reg.getName()));
        return result;
    }
}
