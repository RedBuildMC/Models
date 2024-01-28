package fr.redbuild.models.spigot.commands.arg;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
@Getter
public abstract class CmdArgument<T> {
    private final String name;
    private final boolean optional;

    public int pos;

    public CmdArgument<T> setPos(int pos){
        this.pos = pos;
        return this;
    }

    public CmdArgument(String name, boolean optional){
        this.name = name;
        this.optional = optional;
    }

    public abstract @NotNull Optional<T> getValue(String textValue, CommandSender sender);
    public abstract @NotNull List<String> getValues(CommandSender sender);
}
