package fr.redbuild.models.paper.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import fr.redbuild.models.paper.user.User;
import lombok.Getter;


public class UserInitEvent extends Event implements org.bukkit.event.Cancellable{

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancellable = false;
    
    @Getter
    private final User user;

    public UserInitEvent(User user){
        this.user = user;
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
