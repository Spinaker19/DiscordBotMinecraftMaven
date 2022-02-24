package be.spinaker.command.server;

import be.spinaker.command.Command;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;

    public HelpCmd(MainDiscordBotMinecraftMaven plugin, String name) {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("/stop to stop the server after a countdown of 30s");
        player.sendMessage("/cancel will cancel '/stop' if done on time.");
        player.sendMessage("/backup will make a backup of the server world");
        player.sendMessage("/getMaxBackups gives you the max amount of backups that is being stored");
        player.sendMessage("/setMaxBackups will set the max amount of backups that will be stored");
        player.sendMessage("Be careful not to overload the server storage system");
        player.sendMessage("/getBackupsInterval gives you the amount of time in seconds between two backups");
        player.sendMessage("/setBackupsInterval will set the amount of time in seconds between two backups");
        player.sendMessage(ChatColor.GOLD + "Warning : Do NOT play too much with the backups settings");
        player.sendMessage(ChatColor.GOLD + "because you could overload your server or have only backups from the last hour");

        return false;
    }
}