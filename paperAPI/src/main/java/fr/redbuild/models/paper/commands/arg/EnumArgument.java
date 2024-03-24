package fr.redbuild.models.paper.commands.arg;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EnumArgument<E extends Enum<E>> extends CmdArgument<E> {

    private Class<? extends E> enumClass;

    public EnumArgument(Class<? extends E> enumClass, boolean optional) {
        super(enumClass.getSimpleName(), optional);
        this.enumClass = enumClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Optional<E> getValue(String textValue, CommandSender sender) {
        Enum<E>[] constants = enumClass.getEnumConstants();
        for (Enum<E> constant : constants) {
            if (constant.toString().equalsIgnoreCase(textValue)) {
                return Optional.of((E) constant);
            }
        }
        return Optional.empty();
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender) {
        return Stream.of(enumClass.getEnumConstants()).map(Enum::toString).toList();
    }

}
