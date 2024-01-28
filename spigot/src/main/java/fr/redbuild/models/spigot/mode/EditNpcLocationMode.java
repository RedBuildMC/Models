package fr.redbuild.models.spigot.mode;

import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.npc.NPC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
@Getter
@Setter
public class EditNpcLocationMode {
    private BossBar bar;
    private Player player;
    private NPC npc;

    public EditNpcLocationMode(BossBar bar, Player player, NPC npc) {
        this.bar = bar;
        this.player = player;
        this.npc = npc;
    }

    public void cancel(){
        bar.setVisible(false);
        player.sendMessage(MiniUtils.getMiniMessage().deserialize("<red>Edit cancel !"));
    }

    public void execute(){
        bar.setVisible(false);
        player.sendMessage(MiniUtils.getMiniMessage().deserialize("<green>Location of : <gold>"+ npc.name + "<green> Changed !"));
        npc.npcLocation = player.getLocation();
        npc.update();
    }
}
