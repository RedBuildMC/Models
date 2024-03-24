package fr.redbuild.models.paper.mode;

import fr.redbuild.models.paper.Kyori.MiniUtils;
import fr.redbuild.models.paper.npc.NPC;
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
        player.sendMessage(MiniUtils.getMiniMessage().deserialize("<green>Location of : <gold>"+ npc.getName() + "<green> Changed !"));
        npc.setNpcLocation(player.getLocation());
        npc.update();
    }
}
