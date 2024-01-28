package fr.redbuild.models.spigot.commands;


import java.util.*;

import fr.redbuild.models.spigot.Kyori.MiniUtils;
import fr.redbuild.models.spigot.commands.arg.Argument;
import fr.redbuild.models.spigot.commands.arg.CmdArgument;
import fr.redbuild.models.spigot.commands.arg.CmdArgumentWP;
import fr.redbuild.models.spigot.utils.injector.Injector;
import fr.redbuild.models.spigot.commands.SubCmd.SubCmd;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.s;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public abstract class Cmd extends Command{
    private final MiniMessage mm = MiniUtils.getMiniMessage();
    private final List<SubCmd> subCommand = new ArrayList<>();
    private final List<CmdArgument> cmdArguments = new ArrayList<>();
    private List<CmdArgumentWP> cmdArgumentWPs = new ArrayList<>();
    private boolean a = false;
    public Component usageMessage;

    public Cmd( String name,  String description,  String usageMessage,
            List<String> aliases) {
        super(name, description, usageMessage, aliases);
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

    public Cmd(String name, String desc) {
        super(name, desc, "", List.of());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        boolean error = false;
        if(!a){
            setMainCommand("/" + getName() + " ");
            for(SubCmd cmd : subCommand){
                cmd.setMainCommand("/" + getName() + " ");
            }
            a = true;
        }
        Argument finalArgs = new Argument();
        if (!cmdArguments.isEmpty() || !cmdArgumentWPs.isEmpty()) {
            if (args.length == 0 && cmdArguments.stream().anyMatch(a -> a.isOptional() && a.pos == args.length) && cmdArgumentWPs.stream().anyMatch(a -> a.isOptional() && a.pos == args.length)) {
                this.execute(sender, commandLabel, finalArgs);
                System.out.println("ok");
                return true;
            }
            for (CmdArgumentWP arg : cmdArgumentWPs) {
                if (arg.pos <= args.length) {
                    List<String> fs = new ArrayList<>();
                    for (int n = arg.pos - 1; n < args.length; n++) {
                        fs.add(args[n] + " ");
                    }
                    finalArgs.addArgument(arg.pos, arg.getValue(fs, sender));
                }else if(!arg.isOptional() && arg.pos <= args.length){
                error = true;
                }
            }
            for (CmdArgument arg : cmdArguments) {
                if (arg.pos <= args.length) {
                    finalArgs.addArgument(arg.pos, arg.getValue(args[arg.pos - 1], sender));
                }else if(!arg.isOptional() && arg.pos <= args.length){
                    sender.sendMessage(usageMessage);
                    error = true;
                }
            }
        }
        if(!subCommand.isEmpty()) {
            for (SubCmd rc : subCommand) {
                if(args.length >= rc.getPosition())
                    if (rc.getName().equalsIgnoreCase(args[rc.getPosition() - 1])) {
                        rc.execute(sender, commandLabel, args, rc.getPosition() + 1);
                        return true;
                    }else if(!rc.getOptional() && rc.getPosition() <= args.length){
                        error = true;
                    }
            }
        }
        if (error && finalArgs.size() == 0) {
            sender.sendMessage(usageMessage);
            return true;
        }
        this.execute(sender, commandLabel,finalArgs);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location){
        List<String> tab = new ArrayList<>();
        for (CmdArgumentWP arg : cmdArgumentWPs){
            if (arg.pos <= args.length ) {
                for(Object argString : arg.getValues(sender,args.length)){
                    if(argString instanceof String s)
                        if(s.startsWith(args[args.length - 1]))
                            tab.add(s);
                }
            }
        }
        for(CmdArgument arg : cmdArguments){
            if(arg.pos == args.length){
                for(Object argString : arg.getValues(sender)){
                    if(argString instanceof String s)
                        if(s.startsWith(args[args.length   - 1]))
                            tab.add(s);
                }
            }
        }
        if(!subCommand.isEmpty()) {
            for (SubCmd rc : subCommand) {
                if(args.length >= rc.getPosition())
                    if(rc.getName().startsWith(args[rc.getPosition() - 1])){
                        tab.addAll(rc.tabComplete(sender,alias,args,location,rc.getPosition() + 1));
                    }
            }}

        return tab;
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

    public abstract void execute(CommandSender sender, String command, Argument args);
    
}
