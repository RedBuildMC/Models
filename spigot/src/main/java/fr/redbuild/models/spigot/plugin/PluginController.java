package fr.redbuild.models.spigot.plugin;

import fr.redbuild.models.spigot.npc.NPCController;
import fr.redbuild.models.spigot.region.RegionController;
import fr.redbuild.models.spigot.user.UserManager;
import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.utils.mongo.CodecController;
import fr.redbuild.models.spigot.world.WorldManager;
import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.logger.CtMsg;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public abstract class PluginController extends JavaPlugin {
    @Setter
    @Getter
    private String pluginPackage = "fr.redbuild";

    public static CodecController codecController = new CodecController();
    public static PluginController INSTANCE;

    public abstract void pluginStart();

    public abstract void pluginStop();
    public RegionController regionController;

    public void load(){
    }

    public abstract String dbName();
    @Override
    public void onEnable(){
        INSTANCE = this;
        saveDefaultConfig();
        regionController = new RegionController();
        Injector.registerInjectedInstances(INSTANCE);
        NPCController npcController = new NPCController();
        RegionController regionController = new RegionController();
        Injector.registerInstance(MiniMessage.class.getSimpleName(), MiniUtils.getMiniMessage());
        Injector.registerInstance(codecController);
        codecController.initialize();
        Injector.registerInstance(npcController);
        Injector.inject(regionController);
        CtMsg.init();
        //pas de code avant
        npcController.initialize();
        regionController.initialize();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        try {
            Injector.registerPlugin(pluginPackage);
            Injector.registerPlugin("fr.redbuild.models");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        WorldManager worldManager = new WorldManager();
        worldManager.init();
        Injector.registerInstance(worldManager);
        UserManager userManager = new UserManager();
        Injector.registerInstance(userManager);
        userManager.init();
        this.pluginStart();
    }

    @Override
    public void onDisable(){
        this.pluginStop();
        Injector.getInstance(UserManager.class).deInit();
        if(regionController != null)
            regionController.stopRegionChecker();
    }

    @Override
    public void onLoad(){
        this.load();
    }


}
