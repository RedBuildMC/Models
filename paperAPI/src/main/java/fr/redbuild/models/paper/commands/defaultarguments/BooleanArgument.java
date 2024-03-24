package fr.redbuild.models.paper.commands.defaultarguments;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import fr.redbuild.models.paper.commands.arg.CmdArgument;

public class BooleanArgument extends CmdArgument<Boolean>{

    public BooleanArgument(boolean optional) {
        super("", optional);
    }

    @Override
    public @NotNull Optional<Boolean> getValue(String textValue, CommandSender sender) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValues'");
    }
    
}
