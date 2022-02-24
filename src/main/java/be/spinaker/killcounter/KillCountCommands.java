package be.spinaker.killcounter;

import be.spinaker.configs.ConfigType;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class KillCountCommands implements CommandExecutor {

    private MainDiscordBotMinecraftMaven main;
    private FileConfiguration killCount;

    public KillCountCommands(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        killCount = ConfigType.KILLCOUNT.getConfig().getFileConfiguration();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command c, @NotNull String cmd, @NotNull String[] args) {
        if(sender instanceof Player && killCount.get(((Player) sender).getDisplayName()) != null) {
            Player player = (Player) sender;
            for(String elem : killCount.getConfigurationSection(player.getDisplayName()).getKeys(true)) {
                player.sendMessage(elem + " : " + killCount.getInt(player.getDisplayName() + "." + elem));
            }
        }
        return false;
    }

}
