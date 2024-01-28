package fr.redbuild.models.spigot.event;

import lombok.Getter;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
@Getter
public class PlayerInteractAtNpcEvent extends Event implements Cancellable {
    private final Player player;
    private final int EntityID;
    private boolean sneak;
    private boolean cancellable = false;
    private ServerboundInteractPacket.ActionType actionType;
    private static final HandlerList HANDLERS = new HandlerList();
    private final ServerboundInteractPacket packet;

    public PlayerInteractAtNpcEvent(Player player, int EntityID,boolean sneak,ServerboundInteractPacket.ActionType actionType,ServerboundInteractPacket packet){
        this.actionType = actionType;
        this.sneak = sneak;
        this.player = player;
        this.EntityID = EntityID;
        this.packet = packet;
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
