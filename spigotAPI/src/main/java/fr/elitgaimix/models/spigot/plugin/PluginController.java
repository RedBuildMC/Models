package fr.elitgaimix.models.spigot.plugin;

import fr.elitgaimix.models.spigot.region.RegionController;
import fr.elitgaimix.models.spigot.user.UserManager;
import fr.elitgaimix.models.spigot.utils.injector.Injector;
import fr.elitgaimix.models.spigot.utils.mongo.CodecController;
import fr.elitgaimix.models.spigot.world.WorldManager;
import fr.elitgaimix.models.spigot.Kyori.MiniUtils;
import fr.elitgaimix.models.spigot.logger.CtMsg;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class PluginController extends JavaPlugin {
    @Setter
    @Getter
    private String pluginPackage = "fr.redbuild";

    public static CodecController codecController = new CodecController();
    public static PluginController INSTANCE;
    private List<ServerInfo> classInfo = new ArrayList<>();

    public abstract void pluginStart();

    public abstract void pluginStop();
    public RegionController regionController;

    public void load(){
    }

    public abstract String dbName();

    public void registerClassInfo(ServerInfo info){
        classInfo.add(info);
    }

    @Override
    public void onEnable(){
        INSTANCE = this;
        saveDefaultConfig();
        regionController = new RegionController();
        Injector.registerInjectedInstances(INSTANCE);
        RegionController regionController = new RegionController();
        Injector.registerInstance(MiniMessage.class.getSimpleName(), MiniUtils.getMiniMessage());
        Injector.registerInstance(codecController);
        codecController.initialize();
        Injector.inject(regionController);
        CtMsg.init();
        //pas de code avant
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
        classInfo.forEach(ServerInfo::serverStart);
        this.pluginStart();
    }

    @Override
    public void onDisable(){
        this.pluginStop();
        Injector.getInstance(UserManager.class).deInit();
        if(regionController != null)
            regionController.stopRegionChecker();
        classInfo.forEach(ServerInfo::serverStop);
    }

    @Override
    public void onLoad(){
        this.load();
    }


}
