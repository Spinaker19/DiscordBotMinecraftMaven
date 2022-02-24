package be.spinaker.command;

import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command implements CommandExecutor {

    protected MainDiscordBotMinecraftMaven plugin;
    protected String name;

    public Command(MainDiscordBotMinecraftMaven plugin, String name)
    {
        this.plugin = plugin;
        plugin.getCommand(name).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
        boolean exit = true;
        if (sender instanceof Player) {
            exit = execute((Player) sender, args);
        }
        return exit;
    }

    public abstract boolean execute(Player player, String[] args);
}