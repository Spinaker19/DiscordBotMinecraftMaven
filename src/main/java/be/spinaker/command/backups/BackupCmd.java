package be.spinaker.command.backups;

import be.spinaker.command.Command;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BackupCmd extends Command{

    private MainDiscordBotMinecraftMaven plugin;

    public BackupCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        plugin.getBackup().make();
        return false;
    }
}
