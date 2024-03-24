package fr.elitgaimix.models.spigot.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import fr.elitgaimix.models.spigot.user.User;

public class PlayerFirstJoinEvent extends Event implements org.bukkit.event.Cancellable {
        @Getter
        private final Player player;
        @Getter
        private final User user;
        private boolean Cancellable = false;
        private static final HandlerList HANDLERS = new HandlerList();

        public PlayerFirstJoinEvent(Player player, User user) {
                this.player = player;
                this.user = user;
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

        public static HandlerList getHandlerList() {
                return HANDLERS;
        }
}
