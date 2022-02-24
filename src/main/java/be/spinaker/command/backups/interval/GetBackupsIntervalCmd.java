package be.spinaker.command.backups.interval;

import be.spinaker.command.Command;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class GetBackupsIntervalCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;

    public GetBackupsIntervalCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        player.sendMessage(ChatColor.AQUA + "The Interval between two backups is : " + plugin.getConfig().getInt("default_timer") + "s");
        return false;
    }
}