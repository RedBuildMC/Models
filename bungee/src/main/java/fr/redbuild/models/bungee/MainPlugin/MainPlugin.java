package fr.redbuild.models.bungee.MainPlugin;

import fr.redbuild.models.bungee.Logs.LogManager;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class MainPlugin extends Plugin{

    public static MainPlugin INSTANCE;

    public abstract void pluginStart();

    public abstract void pluginStop();

    @Override
    public void onEnable(){
        pluginStart();
        LogManager.test(this);

    }

    @Override
    public void onDisable(){
        pluginStop();
        LogManager.saveLogs();
    }
}
