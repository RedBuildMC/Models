package fr.elitgaimix.models.spigot.commands.defaultarguments;

import fr.elitgaimix.models.spigot.commands.arg.CmdArgumentWP;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class StringArgument extends CmdArgumentWP<String> {
    public StringArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<String> getValue(List<String> textValue, CommandSender sender) {
        StringBuilder f = new StringBuilder();
        for (String s : textValue) {
            f.append(s);
        }
        return Optional.of(f.toString());
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender, int pos) {
        return List.of("");
    }
}
