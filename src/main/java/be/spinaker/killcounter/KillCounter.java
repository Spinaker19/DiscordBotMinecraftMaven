package be.spinaker.killcounter;

import be.spinaker.configs.Config;
import be.spinaker.configs.ConfigType;
import be.spinaker.discordbot.DiscordCommands;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.io.IOException;

public class KillCounter implements Listener {

    private MainDiscordBotMinecraftMaven plugin;
    private DiscordCommands bot;
    private FileConfiguration killCount;

    public KillCounter(MainDiscordBotMinecraftMaven plugin) {
        this.plugin = plugin;
        this.bot = plugin.getDiscordCommands();
        this.killCount = ConfigType.KILLCOUNT.getConfig().getFileConfiguration();
    }

    @EventHandler
    public void playerKillsPlayer(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            Player killed = event.getEntity();
            killCount.set(killer.getDisplayName() + "." + killed.getDisplayName(), +1);
            ConfigType.KILLCOUNT.save();
        }
    }

}
