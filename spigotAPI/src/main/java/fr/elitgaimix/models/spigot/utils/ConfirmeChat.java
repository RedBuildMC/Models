package fr.elitgaimix.models.spigot.utils;

import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ConfirmeChat {

    public Map<Player,Boolean> click = new HashMap<>();

    public void sendConfirmMsg(String message,String acceptMsg,String denyMsg,ClickCallback<Audience> accept,ClickCallback<Audience> deny,Player player) {
		TextComponent msg = Component.text(message)
                    .append(Component.text(acceptMsg).clickEvent(ClickEvent.callback(accept)))
                    .append(Component.text(denyMsg).clickEvent(ClickEvent.callback(deny)));
        player.sendMessage(msg);
	}

    public void sendConfirmMsg(String message,String acceptMsg,String denyMsg,Runnable accept,Runnable deny,Player player) {
            if(!click.containsKey(player))
                click.put(player,false);
            if(click.get(player) == true)
                click.replace(player, false);
		    TextComponent msg = Component.text(message)
                        .append(Component.text(acceptMsg).clickEvent(ClickEvent.callback(audience ->{
                            if(click.get(player) == false) {
                                click.replace(player, true);
                                accept.run();
                            }
                        })))
                        .append(Component.text(denyMsg).clickEvent(ClickEvent.callback(audience -> {
                            if(click.get(player) == false){
                                click.replace(player, true);
                                deny.run();
                            }
                        })));
        player.sendMessage(msg);
	}
}
