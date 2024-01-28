package fr.redbuild.models.bungee.Logs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Log {
    private final String message;
    private final String date;
    private final LogType type;
    private final LoggerType logger;
    private final String loggerName;
    public Log(String message,String date,LogType type,LoggerType loger,String loggerName){
        this.message = message;
        this.date = date;
        this.type = type;
        this.logger = loger;
        this.loggerName = loggerName;
    }

    public String format(){
        return "[" + date + "]"  + logger.toString(logger) + "[" + loggerName + "]" + type.toString(type) + " " + message;
    }
}
