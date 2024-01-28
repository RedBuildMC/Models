package fr.redbuild.models.bungee.Logs;

public enum LogType {
    ERROR,
    WARNING,
    TEST,
    LOG;
    public String toString(LogType logType){
        if(logType == ERROR){
            return "[ERROR]";
        }else if(logType == WARNING){
            return "[WARNING]";
        }else if(logType == LOG){
            return "[LOG]";
        }else if(logType == TEST){
            return "";
        }
        return "[NULL]";
    }
}
