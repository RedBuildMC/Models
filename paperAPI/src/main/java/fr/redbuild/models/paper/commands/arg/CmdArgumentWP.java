package fr.redbuild.models.paper.commands.arg;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
@Getter
public abstract class CmdArgumentWP<T> {
    private final String name;
    private final boolean optional;
    public int pos;

    public CmdArgumentWP<T> setPos(int pos){
        this.pos = pos;
        return this;
    }
    public CmdArgumentWP(String name, boolean optional){
        this.name = name;
        this.optional = optional;
    }

    public abstract @NotNull Optional<T> getValue(List<String> textValue, CommandSender sender);
    public abstract @NotNull List<String> getValues(CommandSender sender, int pos);
}
