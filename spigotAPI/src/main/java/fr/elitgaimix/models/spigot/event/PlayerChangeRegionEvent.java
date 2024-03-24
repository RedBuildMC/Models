package fr.elitgaimix.models.spigot.event;

import fr.elitgaimix.models.spigot.region.Region;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeRegionEvent extends Event implements org.bukkit.event.Cancellable {
    @Getter
    private final Player player;
    @Getter
    private final Region oldRegion;
    @Getter
    private final Region newRegion;
    private boolean Cancellable = false;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerChangeRegionEvent(Player player, Region oldRegion, Region newRegion){
        this.player = player;
        this.newRegion = newRegion;
        this.oldRegion = oldRegion;
    }

    @Override
    public boolean isCancelled() {
        return Cancellable;
    }

    @Override
    public void setCancelled(boolean cancel) {
        Cancellable = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
