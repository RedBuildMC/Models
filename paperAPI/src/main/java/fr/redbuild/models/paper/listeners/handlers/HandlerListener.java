package fr.redbuild.models.paper.listeners.handlers;

import fr.redbuild.models.paper.Gui.GuiBuilder;
import fr.redbuild.models.paper.Gui.ItemBuilder;
import fr.redbuild.models.paper.Kyori.MiniUtils;
import fr.redbuild.models.paper.event.PlayerInteractAtNpcEvent;
import fr.redbuild.models.paper.event.SignUpdateEvent;
import fr.redbuild.models.paper.handlers.*;
import fr.redbuild.models.paper.mode.BuildMode;
import fr.redbuild.models.paper.mode.EditNpcLocationMode;
import fr.redbuild.models.paper.npc.NPC;
import fr.redbuild.models.paper.npc.NPCController;
import fr.redbuild.models.paper.packet.SignUtils;
import fr.redbuild.models.paper.player.LocationEditManager;
import fr.redbuild.models.paper.utils.ConfirmeChat;
import fr.redbuild.models.paper.utils.injector.Injector;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
public class HandlerListener implements Listener {
    private ConfirmeChat confirmeChat;

    private final Map<Player, NPC> npcDelRequest = new HashMap<>();
    //Handlers var
    private static final Map<ItemStack, OnInteractHandler> interactHandler = new HashMap<>();
    private static final Map<ItemStack, OnClickHandler> clickHandler = new HashMap<>();
    private static final Map<GuiBuilder, OnGuiClickHandler> guiClickHandler = new HashMap<>();

    private static final Map<ItemStack, OnDropHandler> dropHandler = new HashMap<>();

    private static final Map<BlockPos, SignHandler> signHandler = new HashMap<>();
    private static final Map<UUID, InteractAtNpcHandler> interactAtNpcHandler = new HashMap<>();
    private static final List<EditNpcLocationMode> editNpcLocationMode = new ArrayList<>();
    private static final Map<Player,LocationEditMode> locationEditMode = new HashMap<>();

    //All EventHandler
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
            if(interactHandler.get(event.getItem()) != null && action(event.getAction())){
                ItemStack item = event.getItem();
                interactHandler.get(item).execute(event.getPlayer(), event.getAction(),event);
            }
            if(!editNpcLocationMode.isEmpty()){
                for(EditNpcLocationMode mode : editNpcLocationMode){
                    if(event.getPlayer() == mode.getPlayer()){
                        if(!action(event.getAction()))
                            return;
                        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                            //Cancel
                            mode.cancel();

                        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                            //change NPC Location
                            mode.execute();
                        editNpcLocationMode.remove(mode);
                        return;
                    }
                }
            }
            if(locationEditMode.containsKey(event.getPlayer())){
                Location loc = event.getPlayer().getLocation();
                if(event.getClickedBlock() != null)
                 loc = event.getClickedBlock().getLocation();
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    locationEditMode.get(event.getPlayer()).cancel(event.getPlayer());
                if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                    locationEditMode.get(event.getPlayer()).execute(event.getPlayer(),loc);
                locationEditMode.remove(event.getPlayer());
                event.setCancelled(true);
            }
            

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = Bukkit.getServer().getPlayer(event.getWhoClicked().getUniqueId());
        if(clickHandler.containsKey(event.getCurrentItem()))
            clickHandler.get(event.getCurrentItem()).execute(event,player);
        guiClickHandler.keySet().stream().filter(gui -> event.getView().title().equals(gui.name())).findAny().ifPresent(gui -> guiClickHandler.get(gui).execute(event,player,gui.getItem(event.getSlot())));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(dropHandler.containsKey(event.getItemDrop().getItemStack())){
            event.getPlayer().closeInventory();
            dropHandler.get(event.getItemDrop().getItemStack()).execute(event.getPlayer(),event.getItemDrop().getItemStack(),event);
        }
    }

    @EventHandler
    public void signUpdate(SignUpdateEvent event){
        if(signHandler.containsKey(event.getPos())){
            signHandler.get(event.getPos()).execute(event,event.getPlayer(), Arrays.stream(event.getLines()).toList());
        }
    }

    @EventHandler
    public void InteractAtNpc(PlayerInteractAtNpcEvent event) {
        if(confirmeChat == null)
            confirmeChat = Injector.getInstance(ConfirmeChat.class);
        BuildMode buildMode = Injector.getInstance(BuildMode.class);
        NPCController npcController = Injector.getInstance(NPCController.class);
        NPC npc = npcController.getNPC(event.getEntityID());
        if(npc != null) {
            Player player = event.getPlayer();
            if (!buildMode.contains(player)) {
                interactAtNpcHandler.keySet().stream().filter(uuid -> uuid == npcController.getNPC(event.getEntityID()).getNpcUUID()).findAny().ifPresent(npc1 -> interactAtNpcHandler.get(npc1).execute(event.getPlayer(), event, npc1));
            } else if (event.getActionType() == ServerboundInteractPacket.ActionType.INTERACT && player.hasPermission("epicraft.npc.edit")){
                GuiBuilder gui = new GuiBuilder("Edit: <gold>" + npc.getName()).rows(1);
                gui.fillSides(Material.ORANGE_STAINED_GLASS_PANE);
                gui.item(new ItemBuilder(Material.WRITABLE_BOOK, "Edit name").setPos(3).desc("Click to edit the name of npc").onClick(((event1, player1) -> {
                    event1.setCancelled(true);
                    SignUtils.openSign(player1, "New name :", npc.getName(), ((event2, player2, text) -> {
                        player2.sendMessage(MiniUtils.getMiniMessage().deserialize("The npc's name is now :" + text.get(1)));
                        npc.setName(text.get(1));
                        npc.update();
                    }));
                })));
                gui.item(new ItemBuilder(Material.ENDER_PEARL, "Add teleportation").setPos(7).desc("Click to add teleportation").onClick(((event1, player1) -> {
                    event1.setCancelled(true);
                    Injector.getInstance(LocationEditManager.class).addEditor(player1, () -> {
                        if(npc.hasAttribute("teleportation"))
                            npc.setAttribute("teleportation", npc.locationToDocument(player.getLocation()));
                        else
                            npc.addAttribute("teleportation", npc.locationToDocument(player.getLocation()));
                        npc.init();
                        player1.sendMessage(MiniUtils.getMiniMessage().deserialize("<green>Teleportation added !"));
                    }, () -> {
                        player1.sendMessage(MiniUtils.getMiniMessage().deserialize("<red>Cancel !"));
                    });
                })));
                gui.item(new ItemBuilder(Material.ENDER_PEARL, "Edit position").setPos(1).desc("Click to edit the position of npc").onClick(((event1, player1) -> {
                    player1.closeInventory();
                    event1.setCancelled(true);
                    player1.sendMessage(MiniUtils.getMiniMessage().deserialize("<gold>You are in Edit mode ! <red> Right click to cancel <gold> and <green>Left click to edit"));
                    BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "You are in Edit mode ! " + ChatColor.RED + " Right click to cancel " + ChatColor.GOLD + " and " + ChatColor.GREEN + "Left click to edit", BarColor.GREEN, BarStyle.SOLID);
                    bar.setVisible(true);
                    bar.addPlayer(player1);
                    this.registerEditMode(new EditNpcLocationMode(bar, player1, npc));
                })));
                gui.item(new ItemBuilder(Material.BARRIER, "Delete npc").setPos(5).desc("Click to delete the npc").onClick(((event1, player1) -> {
                    // SignUtils.openSign(player1, "Are you sure ?", "yes(y) or no(n)", ((event2, player2, text) -> {
                    //     if (Objects.equals(text.get(2), "y") || Objects.equals(text.get(2), "yes")) {
                    //         npc.delete();
                    //         player.sendMessage(MiniUtils.getMiniMessage().deserialize("<green>Npc <gold>" + npc.name + "<green> was deleted !"));
                    //     }
                    // }));
                    npcDelRequest.put(player, npc);
                    player1.closeInventory();
                    confirmeChat.sendConfirmMsg(ChatColor.GOLD + "Are you sure to remove the NPC ? : ", ChatColor.GREEN + "[Delete] ", ChatColor.RED + " [Cancel]", () -> {
                        if (npcDelRequest.containsKey(player)) {
                            npc.delete();
                            player.sendMessage(MiniUtils.getMiniMessage().deserialize("<green>Npc <gold>" + npc.getName() + "<green> was deleted !"));
                            npcDelRequest.remove(player);
                        }
                    }, () -> {
                        if (npcDelRequest.containsKey(player)) {
                            player.sendMessage(MiniUtils.getMiniMessage().deserialize("<red>Cancel !"));
                            npcDelRequest.remove(player);
                        }
                    }, player);
                    event1.setCancelled(true);
                })));
                gui.open(player);
            }
        }
    }

    //Register all handler
    public static void registerClickHandler(ItemStack item,OnClickHandler handler){
        clickHandler.put(item,handler);
    }

    public static void registerLocationEditHandler(Player player,LocationEditMode mode){
        locationEditMode.put(player, mode);
    }

    public static void registerInteractHandler(ItemStack item,OnInteractHandler handler){
        interactHandler.put(item,handler);
    }

    public static void registerGuiClickHandler(GuiBuilder gui,OnGuiClickHandler handler){
        guiClickHandler.put(gui,handler);
    }

    public void registerEditMode(EditNpcLocationMode mode){
        editNpcLocationMode.add(mode);
    }

    public  static void registerDropHandler(ItemStack item,OnDropHandler handler){
        dropHandler.put(item,handler);
    }
    public  static void registerSignHandler(BlockPos pos,SignHandler handler){
        signHandler.put(pos,handler);
    }

    public  static void registerInteractAtNpcHandler(UUID uuid,InteractAtNpcHandler handler){
        interactAtNpcHandler.put(uuid,handler);
    }
    public static void deRegisterInteractAtNpcHandler(NPC npc){
        interactAtNpcHandler.remove(npc.getNpcUUID());
    }

    public static boolean isRegister(NPC npc) {
        return interactAtNpcHandler.containsKey(npc.getNpcUUID());
    }

    //Other function
    public boolean action(Action action){
        return switch (action) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK, RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> true;
            default -> false;
        };
    }
}
