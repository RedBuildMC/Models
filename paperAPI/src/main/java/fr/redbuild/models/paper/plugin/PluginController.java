package fr.redbuild.models.paper.plugin;

import fr.redbuild.models.paper.region.RegionController;
import fr.redbuild.models.paper.user.UserManager;
import fr.redbuild.models.paper.utils.injector.Injector;
import fr.redbuild.models.paper.utils.mongo.CodecController;
import fr.redbuild.models.paper.world.WorldManager;
import fr.redbuild.models.paper.Kyori.MiniUtils;
import fr.redbuild.models.paper.logger.CtMsg;
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

    public abstract String getServerName();
    
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
