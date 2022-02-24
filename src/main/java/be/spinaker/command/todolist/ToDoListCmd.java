package be.spinaker.command.todolist;

import be.spinaker.command.Command;
import be.spinaker.configs.Config;
import be.spinaker.configs.ConfigType;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;

public class ToDoListCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;
    private DiscordCommands bot;
    private FileConfiguration todo;
    private static final String check = "check";
    private static final String visiblity = "visibility";

    public ToDoListCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
        this.bot = plugin.getDiscordCommands();
        this.todo = ConfigType.TODO.getConfig().getFileConfiguration();
    }

    @Override
    public boolean execute(Player sender, String[] args)
    {
        String arg = "";
        for(String elem : args) {
            arg += elem + " ";
        }
        if(args[0].equalsIgnoreCase("add")) {
            String addObj = arg.replace("add ", "");
            System.out.println(addObj);
            if(todo.getConfigurationSection(addObj) == null || todo.getInt(addObj + "." + check, 0) == 1) {
                todo.set(addObj + "." + visiblity, 0);
                todo.set(addObj + "." + check, 0);
                ConfigType.TODO.save();
                Bukkit.broadcastMessage("§6" + sender.getDisplayName() + " has added the objective '" + addObj + "' to the todo list.");
                bot.sendMessage(sender, "has added the objective '" + addObj + "' to the todo list.", true, new Color(255, 215, 0));
            } else {
                sender.sendMessage("This objective is not yet finished...");
            }
        } else if(args[0].equalsIgnoreCase("check")) {
            String checkObj = arg.replace("check ", "");
            if(todo.getConfigurationSection(checkObj) != null && todo.getInt(checkObj + "." + check) == 0) {
                todo.set(checkObj + "." + check, 1);
                ConfigType.TODO.save();
                Bukkit.broadcastMessage(sender.getDisplayName() + " has completed the objective '" + checkObj + "'");
                bot.sendMessage(sender, " has completed the objective '" + checkObj + "'", true, Color.GREEN);
            } else {
                sender.sendMessage("This objective either does not exist or is already checked...");
                return false;
            }
            //if last objective --> ptit message sympa + feu d'artifice
        } else if(args[0].equalsIgnoreCase("removeAll")) {
            for(String elem : todo.getKeys(false)) {
                todo.set(elem + "." + visiblity, 1);
                todo.set(elem + "." + check, 1);
            }
            ConfigType.TODO.save();
            Bukkit.broadcastMessage(sender.getDisplayName() + "has archived all the objectives");
            bot.sendMessage(sender, "has archived all the objectives", true, new Color(255, 203, 96));
        } else if(args[0].equalsIgnoreCase("clean")) {
            for(String elem : todo.getKeys(false)) {
                if(todo.getInt(elem + "." + check) == 1) {
                    todo.set(elem + "." + visiblity, 1);
                    todo.set(elem + "." + check, 1);
                }
            }
            ConfigType.TODO.save();
            Bukkit.broadcastMessage(sender.getDisplayName() + "has cleaned the objectives");
        } else if(args[0].equalsIgnoreCase("recap")) {
            boolean isThereObjectiveOnGoing = false;
            sender.sendMessage("=======To do List======");
            for(String elem : todo.getKeys(false)) {
                if(todo.getInt(elem + "." + visiblity) == 0) {
                    isThereObjectiveOnGoing = true;
                    if(todo.getInt(elem + "." + check) == 0) sender.sendMessage("§7[§cX§7] §6" + elem);
                    else if(todo.getInt(elem + "." + check) == 1) sender.sendMessage("§7[§aV§7] §2" + elem);
                }
            }
            if(!isThereObjectiveOnGoing) sender.sendMessage("There is no objectives in the todo list right now");
            sender.sendMessage("=====================");
        }
        else {
            sender.sendMessage("Please use one of the following args with this commands :");
            sender.sendMessage("add, check, recap, removeALl");
        }


        return false;
    }
}