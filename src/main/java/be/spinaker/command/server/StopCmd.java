package be.spinaker.command.server;

import be.spinaker.command.Command;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StopCmd implements CommandExecutor {

    private MainDiscordBotMinecraftMaven plugin;
    private DiscordCommands bot;

    public StopCmd(MainDiscordBotMinecraftMaven plugin) {
        this.plugin = plugin;
        this.bot = plugin.getDiscordCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
        if(sender instanceof ConsoleCommandSender) Bukkit.shutdown();
        if(Bukkit.getOnlinePlayers().size() == 1) {
            bot.sendRespondToCommand(sender.getName() + " is shuting down the server");
            Bukkit.broadcastMessage(ChatColor.DARK_RED + sender.getName() + " is shuting down the server");
            plugin.getCooldown().runTaskTimer(plugin, 0, 20);
        }
        else if(Bukkit.getOnlinePlayers().size() > 1) {
            Bukkit.broadcastMessage(ChatColor.GOLD + " You need to be alone on the server for you to shut it down.");
        }

        return false;
    }
}