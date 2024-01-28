package fr.redbuild.models.spigot.event;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SignUpdateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancellable = false;
    @Getter
    private String[] lines;
    @Getter
    private Player player;
    @Getter
    private BlockPos pos;

    public SignUpdateEvent(String[] lines, Player player, BlockPos pos) {
        this.lines = lines;
        this.player = player;
        this.pos = pos;
    }

    @Override
    public boolean isCancelled() {
        return cancellable;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancellable = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
