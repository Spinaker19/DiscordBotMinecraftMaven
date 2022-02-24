package be.spinaker.minecraft;

import be.spinaker.main.MainDiscordBotMinecraftMaven;
import be.spinaker.discordbot.DiscordCommands;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Map;

public class MinecraftListner implements Listener {

    private MainDiscordBotMinecraftMaven main;
    private DiscordCommands bot;
    private Map<String, String> advancements;

    public MinecraftListner(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        bot = main.getDiscordCommands();
        advancements = main.getAdvancements();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        bot.sendMessage(event.getPlayer(), event.getJoinMessage(), true, Color.GREEN);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Location loc = event.getPlayer().getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        bot.sendMessage((event.getPlayer()), event.getQuitMessage() + " at  x:" + x + "  ,y:" + y + "  ,z:" + z, true, Color.RED);
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        bot.sendMessage(event.getPlayer(), event.getMessage(), false, Color.CYAN);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Location loc = player.getLocation();
        bot.sendMessage(player, event.getDeathMessage() + "at : x:" + loc.getBlockX()+ ", y: " + loc.getBlockY() + ", z:" + loc.getBlockZ(), true, Color.RED);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        String advcancementKey = event.getAdvancement().getKey().getKey();
        String display = advancements.get(advcancementKey);
        if(display == null) return;
        bot.sendMessage(event.getPlayer(), event.getPlayer().getDisplayName() + " has made an advancement [" + display + "]", true, Color.CYAN);
    }
}
