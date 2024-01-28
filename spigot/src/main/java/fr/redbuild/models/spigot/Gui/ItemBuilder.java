package fr.redbuild.models.spigot.Gui;

import fr.redbuild.models.spigot.handlers.OnDropHandler;
import fr.redbuild.models.spigot.listeners.handlers.HandlerListener;
import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.handlers.OnInteractHandler;
import fr.redbuild.models.spigot.player.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.redbuild.models.spigot.handlers.OnClickHandler;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;


public class ItemBuilder {

    private MiniMessage mm = MiniUtils.getMiniMessage();

    @Getter
    @Setter
    private ItemStack item;

    @Getter
    private int pos;

    private OnClickHandler clickHandler;

    private OnInteractHandler interactHandler;

    private OnDropHandler dropHandler;

    /**
     * Constructs an ItemBuilder with the specified material and name.
     *
     * @param mat  the material of the item
     * @param name the display name of the item
     */
    public ItemBuilder(Material mat, String name) {
        item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize(name));
        item.setItemMeta(meta);
    }

    /**
     * Constructs an ItemBuilder with the specified material.
     *
     * @param mat the material of the item
     */
    public ItemBuilder(Material mat) {
        item = new ItemStack(mat, 1);
    }

    /**
     * Constructs an ItemBuilder with the specified material, name, and amount.
     *
     * @param mat    the material of the item
     * @param name   the display name of the item
     * @param amount the amount of the item
     */
    public ItemBuilder(Material mat, String name, int amount) {
        item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize(name));
        item.setItemMeta(meta);
    }

    /**
     * Constructs an ItemBuilder with the default material (STONE).
     */
    public ItemBuilder() {
        item = new ItemStack(Material.STONE);
    }

    /**
     * Constructs an ItemBuilder with the default material (STONE) and the specified name.
     *
     * @param name the display name of the item
     */
    public ItemBuilder(String name) {
        item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize(name));
        item.setItemMeta(meta);
    }

    /**
     * Constructs an ItemBuilder with the specified ItemStack.
     *
     * @param itemStack the ItemStack to use
     */
    public ItemBuilder(ItemStack itemStack) {
        item = itemStack;
    }

    /**
     * Sets the icon of the item.
     *
     * @param material the material of the icon
     * @return the ItemBuilder instance
     */
    public ItemBuilder icon(Material material) {
        item.setType(material);
        return this;
    }

    /**
     * Adds a description to the item.
     *
     * @param desc the description to add
     * @return the ItemBuilder instance
     */
    public ItemBuilder desc(String desc) {
        item.lore(List.of(mm.deserialize(desc)));
        return this;
    }

    /**
     * Adds a description to the item.
     *
     * @param desc the description to add
     * @return the ItemBuilder instance
     */
    public ItemBuilder desc(Component desc) {
        item.lore(List.of(desc));
        return this;
    }

    /**
     * Sets the position of the item.
     *
     * @param pos the position of the item
     * @return the ItemBuilder instance
     */
    public ItemBuilder setPos(int pos) {
        this.pos = pos;
        return this;
    }

    /**
     * Adds multiple descriptions to the item.
     *
     * @param desc the descriptions to add
     * @return the ItemBuilder instance
     */
    public ItemBuilder desc(List<Component> desc) {
        item.lore(desc);
        return this;
    }

    /**
     * Sets the icon of the item.
     *
     * @param itemstack the ItemStack to use as the icon
     * @return the ItemBuilder instance
     */
    public ItemBuilder icon(ItemStack itemstack) {
        item.setType(itemstack.getType());
        return this;
    }

    /**
	 * Set le nom
     * 
	 * @param name le nom
	 * @return GuiItemBuilder
	 */
    public ItemBuilder name(String name){
        item.getItemMeta().displayName(mm.deserialize(name));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int power){
        item.addEnchantment(enchantment, power);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment){
        item.addEnchantment(enchantment,1);
        return this;
    }

    /**
	 * Set le nom
     * 
	 * @param name le nom
	 * @return GuiItemBuilder
	 */
    public ItemBuilder name(Component name){
        item.getItemMeta().displayName(name);
        return this;
    }

    /**
	 * Build l'item
     * 
	 * @return GuiItem
	 */
    public ItemStack build(){
        if(interactHandler != null)
            HandlerListener.registerInteractHandler(item,interactHandler);
        if(clickHandler != null)
            HandlerListener.registerClickHandler(item,clickHandler);
        if(dropHandler != null)
            HandlerListener.registerDropHandler(item,dropHandler);
        return item;
    }

    /**
	 * Give l'item au joueur
     * 
     * @param player le joueur
	 */
    public void safeGive(Player player){
        if(interactHandler != null)
            HandlerListener.registerInteractHandler(item,interactHandler);
        if(clickHandler != null)
            HandlerListener.registerClickHandler(item,clickHandler);
        if(dropHandler != null)
            HandlerListener.registerDropHandler(item,dropHandler);
        ItemUtils.safeGive(player,mm.deserialize("<red>Votre inventaire est plein !"),item);
    }

    public void forceSafeGive(int pos,Player player,Boolean b){
        if(interactHandler != null)
            HandlerListener.registerInteractHandler(item,interactHandler);
        if(clickHandler != null)
            HandlerListener.registerClickHandler(item,clickHandler);
        if(dropHandler != null)
            HandlerListener.registerDropHandler(item,dropHandler);
        ItemUtils.forceSafeGive(pos,player,mm.deserialize("<red>Votre inventaire est plein !"),item,b);
    }

    public void forceSafeGive(int pos,Player player,Component message,Boolean b){
        if(interactHandler != null)
            HandlerListener.registerInteractHandler(item,interactHandler);
        if(clickHandler != null)
            HandlerListener.registerClickHandler(item,clickHandler);
        if(dropHandler != null)
            HandlerListener.registerDropHandler(item,dropHandler);
        ItemUtils.forceSafeGive(pos,player,message,item,b);
    }

    /**
	 * Give l'item au joueur
     * 
     * @param player le joueur
     * @param message le message si l'inventaire du joueur et plein
	 */
    public void safeGive(Player player,Component message){
        if(interactHandler != null)
            HandlerListener.registerInteractHandler(item,interactHandler);
        if(clickHandler != null)
            HandlerListener.registerClickHandler(item,clickHandler);
        if(dropHandler != null)
            HandlerListener.registerDropHandler(item,dropHandler);
        ItemUtils.safeGive(player,message,item);
    }

    public ItemBuilder onClick(Runnable handler){
        this.clickHandler = (event,player) -> {
            handler.run();
         };
         return this;
    }

    public ItemBuilder onDrop(Runnable handler){
        dropHandler = ((player, item, event) -> {
            handler.run();
        });
        return this;
    }

    public ItemBuilder onInteract(Runnable handler){
        this.interactHandler = (player, click,event) -> {
            handler.run();
         };
        return this;
    }

    public ItemBuilder onClick(OnClickHandler handler){
        this.clickHandler = handler;
         return this;
    }

    public ItemBuilder onDrop(OnDropHandler handler){
        dropHandler = handler;
        return this;
    }

    public ItemBuilder onInteract(OnInteractHandler handler){
        this.interactHandler = handler;
        return this;
    }

}
