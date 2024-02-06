package fr.redbuild.models.spigot.Gui;

import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.handlers.OnGuiClickHandler;
import fr.redbuild.models.spigot.listeners.handlers.HandlerListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe représente un constructeur de GUI (Interface Utilisateur Graphique).
 * Elle permet de créer et personnaliser une interface utilisateur pour les joueurs.
 */
public class GuiBuilder {

    private MiniMessage mm = MiniUtils.getMiniMessage();
    private List<ItemBuilder> items = new ArrayList<>();

    private Component name;

    private int rows = 1;

    private OnGuiClickHandler clickHandler = ((event,player, item) -> {event.setCancelled(true);});

    /**
     * Constructeur de la classe GuiBuilder.
     *
     * @param name Le nom de la GUI.
     */
    public GuiBuilder(@NotNull String name){
        this.name = mm.deserialize(name);
    }

    /**
     * Renvoie le nombre de lignes de la GUI.
     *
     * @return Le nombre de lignes de la GUI.
     */
    public int rows(){
        return rows;
    }

    /**
     * Définit le nombre de lignes de la GUI.
     *
     * @param rows Le nombre de lignes de la GUI.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder rows(int rows){
        if(!(rows > 6))
            this.rows = rows;
        return this;
    }

    /**
     * Renvoie le nom de la GUI.
     *
     * @return Le nom de la GUI.
     */
    public Component name(){
        return name;
    }

    /**
     * Définit le nom de la GUI.
     *
     * @param name Le nom de la GUI.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder name(String name){
        this.name = mm.deserialize(name);
        return this;
    }

    /**
     * Ajoute un item à la position spécifiée dans la GUI.
     *
     * @param mat Le matériau de l'item.
     * @param pos La position de l'item.
     */
    public GuiBuilder item(Material mat,int pos){
        ItemBuilder item = new ItemBuilder(mat,"").desc("").setPos(pos);
        item(item);
        return this;
    }

    public GuiBuilder item(Material mat,int pos,String name){
        ItemBuilder item = new ItemBuilder(mat,name).desc("").setPos(pos);
        item(item);
        return this;
    }

    /**
     * Remplit les côtés de la GUI avec un matériau spécifié.
     *
     * @param mat Le matériau de remplissage.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder fillSides(Material mat){
        fillLeft(mat);
        fillRight(mat);
        fillTop(mat);
        fillBot(mat);
        return this;
    }

    /**
     * Remplit le côté gauche de la GUI avec un matériau spécifié.
     *
     * @param mat Le matériau de remplissage.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder fillLeft(Material mat){
        item(mat,0);
        item(mat,9);
        item(mat,18);
        item(mat,27);
        item(mat,36);
        item(mat,45);
        return this;
    }

    /**
     * Remplit le côté droit de la GUI avec un matériau spécifié.
     *
     * @param mat Le matériau de remplissage.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder fillRight(Material mat){
        item(mat,8);
        item(mat,17);
        item(mat,26);
        item(mat,35);
        item(mat,44);
        item(mat,53);
        return this;
    }

    /**
     * Remplit le haut de la GUI avec un matériau spécifié.
     *
     * @param mat Le matériau de remplissage.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder fillTop(Material mat){
        item(mat,0);
        item(mat,1);
        item(mat,2);
        item(mat,3);
        item(mat,4);
        item(mat,5);
        item(mat,6);
        item(mat,7);
        item(mat,8);
        return this;
    }

    /**
     * Remplit le bas de la GUI avec un matériau spécifié.
     *
     * @param mat Le matériau de remplissage.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder fillBot(Material mat){
        int a = (rows*9) - 1;
        item(mat,a);
        item(mat,a - 1);
        item(mat,a - 2);
        item(mat,a - 3);
        item(mat,a - 4);
        item(mat,a - 5);
        item(mat,a - 6);
        item(mat,a - 7);
        item(mat,a - 8);
        return this;
    }

    /**
     * Ajoute un item à la GUI.
     *
     * @param item L'item à ajouter.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder item(ItemBuilder item){
        items.stream().filter(it -> it.getPos() == item.getPos()).findAny().ifPresent(guiItemBuilder -> items.remove(guiItemBuilder));
        items.add(item);
        return this;
    }

    /**
     * Définit le gestionnaire d'événements de clic sur la GUI.
     *
     * @param handler Le gestionnaire d'événements de clic.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder onClick(Runnable handler){
        this.clickHandler = (event,player,item) -> {
            handler.run();
        };
        return this;
    }

    /**
     * Définit le gestionnaire d'événements de clic sur la GUI.
     *
     * @param handler Le gestionnaire d'événements de clic.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder onClick(OnGuiClickHandler handler){
        this.clickHandler = handler;
        return this;
    }

    /**
     * Construit l'inventaire de la GUI.
     *
     * @return L'inventaire de la GUI.
     */
    public Inventory build(){
        Inventory inv = Bukkit.createInventory(null,rows*9,name);
        for(ItemBuilder item : items){
            if(item.getPos() < rows*9){
                inv.setItem(item.getPos(),item.build());
            }
        }
        HandlerListener.registerGuiClickHandler(this,clickHandler);
        return inv;
    }

    /**
     * Récupère l'item à la position spécifiée dans la GUI.
     *
     * @param slot La position de l'item.
     * @return L'item à la position spécifiée.
     */
    public Optional<ItemBuilder> getItem(int slot){
        return items.stream().filter(item -> item.getPos() == slot).findAny();
    }

    /**
     * Ouvre la GUI pour un joueur spécifié.
     *
     * @param player Le joueur.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder open(Player player){
        player.openInventory(build());
        return this;
    }

    /**
     * Ouvre la GUI pour une liste de joueurs spécifiée.
     *
     * @param players La liste de joueurs.
     * @return L'instance actuelle de GuiBuilder.
     */
    public GuiBuilder open(List<Player> players){
        Inventory inv = build();
        for(Player player : players){
            player.openInventory(inv);
        }
        return  this;
    }
}
