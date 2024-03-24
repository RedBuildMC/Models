package fr.redbuild.models.paper.commands.SubCmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.redbuild.models.paper.Kyori.MiniUtils;
import fr.redbuild.models.paper.commands.arg.Argument;
import fr.redbuild.models.paper.commands.arg.CmdArgument;
import fr.redbuild.models.paper.commands.arg.CmdArgumentWP;
import fr.redbuild.models.paper.utils.injector.Injector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import lombok.Getter;

/**
 * This abstract class represents a sub-command in a command hierarchy.
 * It provides methods for registering sub-commands, command arguments, and usage messages.
 * Sub-commands can be executed by providing the necessary arguments.
 */
@Getter
@SuppressWarnings("all")
public abstract class SubCmd {
    private final MiniMessage mm = MiniUtils.getMiniMessage();
    private String name;
    private Boolean optional;
    private List<SubCmd> subCommand = new ArrayList<>();
    private String permission = null;
    private int position;
    private final List<CmdArgument> cmdArguments = new ArrayList<>();
    private final List<CmdArgumentWP> cmdArgumentWPs = new ArrayList<>();
    public Component usageMessage;

    public SubCmd(String name,Boolean optional){
        this.name = name;
        this.optional = optional;
    }

    public void rc(int pos,SubCmd cmd){
        subCommand.add(cmd.setPos(pos));
        Injector.registerInstance(cmd);
    }
    public void rc(int pos,CmdArgument arg){
        cmdArguments.add(arg.setPos(pos));
        Injector.registerInstance(arg);
    }

    public void rc(int pos,CmdArgumentWP arg){
        cmdArgumentWPs.add(arg.setPos(pos));
        Injector.registerInstance(arg);
    }

    public String prefix(String name){
        return "[" + name + "] ";
    }

    public void setPermission(String name){
        permission = name;
    }
    public void setMainCommand(String main){
        for(SubCmd cmd : subCommand){
            cmd.setMainCommand(main);
        }
        usageMessage = mm.deserialize("<red>Commande incorrect, Faite " + main + "help");
    }
    public void setUsageMessage(Component message){
        usageMessage = message;
    }

    public List<String> tabComplete(CommandSender sender,String alias,String[] args,Location location,int position){
        List<String> tab = new ArrayList<>();
        for (CmdArgumentWP arg : cmdArgumentWPs){
            if (arg.pos + position <= args.length + 1) {
                for(Object argString : arg.getValues(sender,args.length - position)){
                    if(argString instanceof String s)
                        if(s.startsWith(args[arg.pos + position - 2]))
                            tab.add(s);
                }
            }
        }
        for(CmdArgument arg : cmdArguments){
            if(arg.pos + position == args.length + 1){
                for(Object argString : arg.getValues(sender)){
                    if(argString instanceof String s)
                        if(s.startsWith(args[arg.pos + position - 2]))
                            tab.add(s);
                }
            }
        }
        for(SubCmd subcmd : subCommand){
            if(args.length == position){
                if((subcmd.getPermission() == null || sender.hasPermission(subcmd.getPermission())) && subcmd.getName().startsWith(args[position - 1])){
                    tab.add(subcmd.getName());
                }
            }else{
                if(args.length != 0)
                    if(tab.isEmpty() && args.length >= position && args[position - 1].equalsIgnoreCase(subcmd.getName())){
                        int pos = position;
                        pos++;
                        tab = subcmd.tabComplete(sender, alias, args, location,pos);
                    }
            }
        }
        if(tab.isEmpty() && args.length == position - 1 && (permission == null || sender.hasPermission(permission)))
            tab.add(name);
        return tab;
    }

    public void execute(CommandSender sender,  String commandLabel,  String[] args,int position){
        Argument finalArgs = new Argument();
        if (!cmdArguments.isEmpty() || !subCommand.isEmpty() || !cmdArgumentWPs.isEmpty()) {
            for (CmdArgumentWP arg : cmdArgumentWPs){
                if (arg.pos + position <= args.length + 1) {
                    List<String> fs = new ArrayList<>(Arrays.asList(args).subList(arg.pos, args.length));
                    finalArgs.addArgument(arg.pos, arg.getValue(fs,sender));
                }
            }
            for (CmdArgument arg : cmdArguments) {
                if (arg.pos + position <= args.length + 1) {
                    finalArgs.addArgument(arg.pos, arg.getValue(args[position - 1], sender));
                }
            }
            for (SubCmd rc : subCommand) {
                if(args.length != 0)
                    if (args.length >= position && rc.getName().equalsIgnoreCase(args[position - 1])) {
                        int pos = position;
                        pos++;
                        rc.execute(sender, commandLabel, args, pos);
                        return;
                    }
            }
            if ((cmdArguments.stream().anyMatch(a -> !a.isOptional() && a.pos + position - 1 == args.length) || cmdArguments.stream().anyMatch(a -> !a.isOptional() && a.pos + position - 1 == args.length)) && finalArgs.size() == 0) {
                sender.sendMessage(usageMessage);
                return;
            }
        }
        if(permission == null || sender.hasPermission(permission)) {
            this.execute(sender, commandLabel, finalArgs);
            return;
        }
        sender.sendMessage(usageMessage);
    }

    public SubCmd setPos(int pos){
        position = pos;
        return this;
    }

    public abstract void execute(CommandSender sender,String args,Argument argument);
}
