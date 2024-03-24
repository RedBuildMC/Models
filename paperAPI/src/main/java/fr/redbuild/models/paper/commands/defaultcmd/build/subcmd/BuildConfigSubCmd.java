package fr.redbuild.models.paper.commands.defaultcmd.build.subcmd;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.redbuild.models.paper.Autowired.Autowired;
import fr.redbuild.models.paper.Gui.GuiBuilder;
import fr.redbuild.models.paper.Gui.ItemBuilder;
import fr.redbuild.models.paper.commands.SubCmd.SubCmd;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.user.User;
import fr.redbuild.models.paper.user.UserManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class BuildConfigSubCmd extends SubCmd{
    @Autowired
    private UserManager userManager;

    @Autowired
    private MiniMessage mm;

    public BuildConfigSubCmd(Boolean optional) {
        super("config", optional);
    }

    @Override
    public void execute(CommandSender sender, String args, Argument argument) {
        if(sender instanceof Player player){
            openEditor(player);
        }else{
            sender.sendMessage("Vous devez être un joueur pour executer cette commande");
        }
    }

    public void openEditor(Player player){
        User user = userManager.getUser(player);
            ItemBuilder item1 = new ItemBuilder().setPos(3);
            ItemBuilder item2 = new ItemBuilder().setPos(5);
            if (!user.hasAttribute("build_particles") || user.getAttribute("build_particles").equals("true")) {
                item1.icon(Material.GREEN_CONCRETE).name("<gold>Particules : <green>Activer").desc("<red>Cliquez pour désactiver les particules")
                        .onClick(() -> {
                            if (user.hasAttribute("build_particles")) {
                                user.setAttribute("build_particles", "false");
                                player.sendMessage(mm.deserialize("<red><bold>Vous avez désactivé les particules"));
                                openEditor(player);
                            } else {
                                user.addAttribute("build_particles", "false");
                                player.sendMessage(mm.deserialize("<red><bold>Vous avez désactivé les particules"));
                                openEditor(player);
                            }
                        });
            } else {
                item1.icon(Material.RED_CONCRETE).name("<gold>Particules : <red>Désactiver").desc("<green>Cliquez pour activer les particules")
                        .onClick(() -> {
                            if (user.hasAttribute("build_particles")) {
                                user.setAttribute("build_particles", "true");
                                player.sendMessage(mm.deserialize("<green><bold>Vous avez activé les particules"));
                                openEditor(player);
                            } else {
                                user.addAttribute("build_particles", "true");
                                player.sendMessage(mm.deserialize("<green><bold>Vous avez activé les particules"));
                                openEditor(player);
                            }
                        });
            }
            if (!user.hasAttribute("build_messages") || user.getAttribute("build_messages").equals("true")) {
                item2.icon(Material.GREEN_CONCRETE).name("<gold>Messages : <green>Activer").desc("<red>Cliquez pour désactiver les messages")
                        .onClick(() -> {
                            if (user.hasAttribute("build_messages")) {
                                user.setAttribute("build_messages", "false");
                                player.sendMessage(mm.deserialize("<red><bold>Vous avez désactivé les messages"));
                                openEditor(player);
                            } else {
                                user.addAttribute("build_messages", "false");
                                player.sendMessage(mm.deserialize("<red><bold>Vous avez désactivé les messages"));
                                openEditor(player);
                            }
                        });
            } else {
                item2.icon(Material.RED_CONCRETE).name("<gold>Messages : <red>Désactiver").desc("<green>Cliquez pour activer les messages")
                        .onClick(() -> {
                            if (user.hasAttribute("build_messages")) {
                                user.setAttribute("build_messages", "true");
                                player.sendMessage(mm.deserialize("<green><bold>Vous avez activé les messages"));
                                openEditor(player);
                            } else {
                                user.addAttribute("build_messages", "true");
                                player.sendMessage(mm.deserialize("<green><bold>Vous avez activé les messages"));
                            }
                        });
            }

            new GuiBuilder("Build Config")
                    .rows(1)
                    .fillSides(Material.ORANGE_STAINED_GLASS_PANE)
                    .item(item1)
                    .item(item2)
                    .open(player);
    }
    
}
