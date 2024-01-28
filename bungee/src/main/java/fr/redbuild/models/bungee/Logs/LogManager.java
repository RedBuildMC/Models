package fr.redbuild.models.bungee.Logs;

import fr.redbuild.models.bungee.MainPlugin.MainPlugin;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogManager {
    @Getter
    private static List<Log> logs = new ArrayList<>();
    private static MainPlugin plugin;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public LogManager(MainPlugin main){
        plugin = main;
    }
    public static void log(String message){
        System.out.println("[§4LOG§r][§9INFO§r]" + message);
        logs.add(new Log(message,sdf.format(new Date()),LogType.LOG,LoggerType.PLUGINS,"Bungee"));
    }

    public static void logWithOut(String message){
        System.out.println("[§4LOG§r]" + message);
        logs.add(new Log(message,sdf.format(new Date()),LogType.TEST,LoggerType.PLUGINS,"Bungee"));
    }

    public static void logError(String message){
        System.out.println("[§4LOG§r][§cERROR§r]" + message);
        logs.add(new Log(message,sdf.format(new Date()),LogType.LOG,LoggerType.PLUGINS,"Bungee"));
    }

    public static void logWarning(String message){
        System.out.println("[§4LOG§r][§eWARNING§r]" + message);
        logs.add(new Log(message,sdf.format(new Date()),LogType.LOG,LoggerType.PLUGINS,"Bungee"));
    }
    public static void log(String message,String date,LogType type,LoggerType logger,String loggerName){
        System.out.println("[§4LOG§r]" + getLogTypeMessage(type) + message);
        logs.add(new Log(message,date,type,logger,loggerName));
    }

    public static  void resetLogs(){
        logs = new ArrayList<>();
    }

    public static void test(MainPlugin plugin){
        logWithOut("[§3EPI§aCRAFT§r] Starting log test !");
        log("Log INFO");
        logWarning("Log WARNING");
        logError("Log ERROR");
        logWithOut("[§3EPI§aCRAFT§r] Log test finish !");
        new LogManager(plugin);
    }
    public static String getLogTypeMessage(LogType type){
        switch(type){
            case LOG -> {
                return  "[§9INFO§r]";
            }
            case ERROR -> {
                return "[§cERROR§r]";
            }
            case WARNING -> {
                return "[§eWARNING§r]";
            }
            default -> {
                return null;
            }
        }
    }


    public static String logsToString() {
        String logs = "";
        for(Log log : LogManager.logs){
            logs = logs + log.format() + "\n";
        }
        return  logs;
    }
    public static void saveLogs(){
        if(!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        log("save logfile in " + plugin.getDataFolder() + "\\Logs-" +  sdf.format(new Date()).replace(" ","-").replace(":","-") +".txt");
        try (FileWriter wr = new FileWriter(new File(plugin.getDataFolder(),"/Logs-" +  sdf.format(new Date()).replace(" ","-").replace(":","-") +".txt"))) {
            wr.write(logsToString());
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }
}
