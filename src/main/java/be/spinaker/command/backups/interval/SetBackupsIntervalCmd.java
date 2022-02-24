package be.spinaker.command.backups.interval;

import be.spinaker.command.Command;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetBackupsIntervalCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;

    public SetBackupsIntervalCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        if(args.length >= 1  && isNumeric(args[0])){
            int backupInterval = Integer.parseInt(args[0]);
            plugin.getConfig().set("default_timer", backupInterval);
            plugin.saveConfig();
            player.sendMessage(ChatColor.AQUA + "There will now be " + backupInterval + " backup stored");
        }
        else {
            player.sendMessage(ChatColor.RED + "Usage : /setBackupInterval <Amount of time in seconds> ");
        }
        return false;
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