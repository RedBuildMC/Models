package fr.redbuild.models.spigot.commands.defaultarguments;

import fr.redbuild.models.spigot.commands.arg.CmdArgumentWP;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationArgument extends CmdArgumentWP<Location> {
    public LocationArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public @NotNull Optional<Location> getValue(List<String> textValue, CommandSender sender) {
        if(textValue.size() >= 3 && sender instanceof Player player)
            return Optional.of(new Location(player.getWorld(),Double.parseDouble(textValue.get(0)),Double.parseDouble(textValue.get(1)),Double.parseDouble(textValue.get(2))));
        return Optional.empty();
    }

    @Override
    public @NotNull List<String> getValues(CommandSender sender, int pos) {
        if(sender instanceof Player player) {
            List<String> finalList = new ArrayList<>();
            DecimalFormat format = new DecimalFormat("0.000");
            format.setMaximumFractionDigits(3);
            format.setRoundingMode(RoundingMode.FLOOR);
            if (pos == 1) {
                finalList.add(format.format(player.getLocation().getX()).replace(",","."));
            } else if (pos == 2) {
                finalList.add(format.format(player.getLocation().getY()).replace(",","."));
            } else if (pos == 3) {
                finalList.add(format.format(player.getLocation().getZ()).replace(",","."));
            }
            return finalList;
        }
        return List.of();
    }
}
