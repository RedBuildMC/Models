package fr.redbuild.models.bungee.Logs;

public enum LoggerType {
    PLAYER,
    CONSOLE,
    PLUGINS,
    GAME;

    public String toString(LoggerType loggerType){
        if(loggerType == PLAYER){
            return "[PLAYER]";
        }else if(loggerType == CONSOLE){
            return "[CONSOLE]";
        }else if(loggerType == PLUGINS){
            return "[PLUGINS]";
        }else if(loggerType == GAME){
            return "[GAME]";
        }
        return "[NULL]";
    }
}
