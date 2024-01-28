package fr.redbuild.models.spigot.commands.defaultarguments;

import fr.redbuild.models.spigot.commands.arg.EnumArgument;
import org.bukkit.Material;

public class MaterialArgument extends EnumArgument<Material> {
    public MaterialArgument(String name, boolean optional) {
        super(Material.class, optional);
    }
}
