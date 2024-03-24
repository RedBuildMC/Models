package fr.redbuild.models.paper.commands.defaultarguments;

import fr.redbuild.models.paper.commands.arg.CmdArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PlayerArgument extends CmdArgument<Player> {
    public PlayerArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<Player> getValue(String textValue, CommandSender sender) {
        return Optional.ofNullable(Bukkit.getPlayer(textValue));
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender) {
        return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
