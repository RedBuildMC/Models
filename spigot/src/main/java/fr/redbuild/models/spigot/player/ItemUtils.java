package fr.redbuild.models.spigot.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;

public class ItemUtils {
    /**
	 * give l'item au joueur
     * 
     * @param player le joueur
     * @param message le message si l'inventaire du joueur et plein
	 */
    public static void safeGive(Player player, Component message, ItemStack item){
        int n = 0;
        boolean set = false;
        while(n != 36){
            if(player.getInventory().contains(item,n)){
                player.getInventory().setItem(n, item);
                set = true;
                break;
            }
            n++;
        }
        if(!set){
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            player.sendMessage(message);
        }
    }

    public static void forceSafeGive(int pos, Player player, Component message, ItemStack item, Boolean b){
            if(b && player.getInventory().contains(item,pos))
                return;
            if(player.getInventory().getItem(pos) == item){
                player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getItem(pos));
                player.sendMessage(message);
            }
            player.getInventory().setItem(pos, item);

    }
}
