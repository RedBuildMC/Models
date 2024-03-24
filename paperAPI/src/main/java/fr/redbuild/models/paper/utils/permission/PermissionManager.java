package fr.redbuild.models.paper.utils.permission;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.redbuild.models.paper.plugin.PluginController;

public class PermissionManager {
    
    public void addPermission(String permissionName,Player player){
        player.addAttachment(PluginController.INSTANCE, permissionName, true);
    }

    public List<String> getAllPermission(){
        return Bukkit.getServer().getPluginManager().getPermissions().stream().map(perm -> perm.getName()).toList();
    }

    public List<String> getPermission(Player player){
        return player.getEffectivePermissions().stream().map(perm -> perm.getPermission()).toList();
    }

    public void addPermission(List<String> permissionName,Player player){
        permissionName.forEach(perm -> addPermission(perm, player));
    }

    public void removePermission(String permissionName,Player player){
        player.addAttachment(PluginController.INSTANCE, permissionName, false);
    }

    public void removePermissionPermission(List<String> permissionName,Player player){
        permissionName.forEach(perm -> removePermission(perm, player));
    }
}
