package be.spinaker.minecraft;

import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class MinecraftCommands implements CommandExecutor {

    MainDiscordBotMinecraftMaven main;
    DiscordCommands bot;

    public MinecraftCommands(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        bot = main.getDiscordCommands();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command c, @NotNull String cmd, @NotNull String[] args) {
        if(cmd.equalsIgnoreCase("stop") && !(main.isStopping())) {
            if(sender instanceof ConsoleCommandSender) Bukkit.shutdown();
            if(Bukkit.getOnlinePlayers().size() == 1) {
                bot.sendRespondToCommand(sender.getName() + " is shuting down the server");
                Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " is shuting down the server");
                main.getCooldown().runTaskTimer(main, 0, 20);
            }
            else if(Bukkit.getOnlinePlayers().size() > 1) {
                Bukkit.broadcastMessage(ChatColor.GOLD + " You need to be alone on the server for you to shut it down.");
            }
        }
        else if(cmd.equalsIgnoreCase("cancel") && main.isStopping()) {
            main.getCooldown().stop();
            main.resetCooldown();
            Bukkit.broadcastMessage(ChatColor.GREEN + "The shuting down of the server has been canceled !");
            bot.sendRespondToCommand("The shuting down of the server has been canceled !");
        }
        else if(cmd.equalsIgnoreCase("backup")) {
            main.getBackup().make();
        }
        else if(cmd.equalsIgnoreCase("getMaxBackups")) {
            sender.sendMessage(ChatColor.AQUA + "The maximum numbers of backups stored is : " + main.getConfig().getInt("max_backups"));
        }
        else if(cmd.equalsIgnoreCase("setMaxBackups")) {
            if(args.length >= 1 && isNumeric(args[0])){
                int backupNumber = Integer.parseInt(args[0]);
                main.getConfig().set("max_backups", backupNumber);
                main.saveConfig();
                sender.sendMessage(ChatColor.AQUA + "There will now be " + backupNumber + " backup stored");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Usage : /setMaxBackup <Amount of Backups> ");
            }
        }
        else if(cmd.equalsIgnoreCase("getBackupsInterval")) {
            sender.sendMessage(ChatColor.AQUA + "The Interval between two backups is : " + main.getConfig().getInt("default_timer") + "s");
        }
        else if(cmd.equalsIgnoreCase("setBackupsInterval")) {
            if(args.length >= 1  && isNumeric(args[0])){
                int backupInterval = Integer.parseInt(args[0]);
                main.getConfig().set("default_timer", backupInterval);
                main.saveConfig();
                sender.sendMessage(ChatColor.AQUA + "There will now be " + backupInterval + " backup stored");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Usage : /setBackupInterval <Amount of time in seconds> ");
            }
        }
        else if(cmd.equalsIgnoreCase("help")) {
            displayHelp(sender);
        }
    return false;
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage("/stop to stop the server after a countdown of 30s");
        sender.sendMessage("/cancel will cancel '/stop' if done on time.");
        sender.sendMessage("/backup will make a backup of the server world");
        sender.sendMessage("/getMaxBackups gives you the max amount of backups that is being stored");
        sender.sendMessage("/setMaxBackups will set the max amount of backups that will be stored");
        sender.sendMessage("Be careful not to overload the server storage system");
        sender.sendMessage("/getBackupsInterval gives you the amount of time in seconds between two backups");
        sender.sendMessage("/setBackupsInterval will set the amount of time in seconds between two backups");
        sender.sendMessage(ChatColor.GOLD + "Warning : Do NOT play too much with the backups settings");
        sender.sendMessage(ChatColor.GOLD + "because you could overload your server or have only backups from the last hour");
    }

    private boolean isNumeric(String string) {
        if(string == null) return false;
        try {
            double d = Double.parseDouble(string);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
