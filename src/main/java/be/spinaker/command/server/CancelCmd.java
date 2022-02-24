package be.spinaker.command.server;

import be.spinaker.command.Command;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CancelCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;
    private DiscordCommands bot;

    public CancelCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
        this.bot = plugin.getDiscordCommands();
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        if(plugin.isStopping()) {
            plugin.getCooldown().stop();
            plugin.resetCooldown();
            Bukkit.broadcastMessage(ChatColor.GREEN + "The shutting down of the server has been canceled !");
            bot.sendRespondToCommand("The shutting down of the server has been canceled !");
        }
        else {
            player.sendMessage("The plugin is not stopping...");
        }
        return false;
    }
}