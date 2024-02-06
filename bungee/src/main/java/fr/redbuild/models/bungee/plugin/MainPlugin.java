package fr.redbuild.models.bungee.plugin;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class MainPlugin extends Plugin{

    public static MainPlugin INSTANCE;

    public abstract void pluginStart();

    public abstract void pluginStop();

    @Override
    public void onEnable(){
        pluginStart();

    }

    @Override
    public void onDisable(){
        pluginStop();

    }
}
