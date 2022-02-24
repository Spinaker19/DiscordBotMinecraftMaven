package be.spinaker.main;

import be.spinaker.discordbot.DiscordCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class Cooldown extends BukkitRunnable {

    private int timer = 90;
    private MainDiscordBotMinecraftMaven main;
    private DiscordCommands bot;

    public Cooldown(MainDiscordBotMinecraftMaven plugin) {
        main = plugin;
        bot = main.getDiscordCommands();
    }

    @Override
    public void run() {
        if(!main.isStopping()) setupTimer();
        switch(timer){
            case(30):
                Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in 30s");
                break;
            case(20):
                Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in 20s");
                break;
            case(10):
                Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in 10s");
                break;
        }
        if(timer < 5) {
            Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in " + timer + "s");
        }
        if(timer == 3) {
            try {
                Runtime.getRuntime().exec("shutdown -s -t 23");
            } catch(IOException e) {
                e.printStackTrace();
                this.stop();
                main.resetCooldown();
                Bukkit.broadcastMessage(ChatColor.RED + "The server wasn't able to stop...");
                bot.sendRespondToCommand("The server wasn't able to stop");
            }
        }
        if(timer == 0) {
            Bukkit.broadcastMessage(ChatColor.RED + "Server shutting down...");
            this.cancel();
            Bukkit.shutdown();
        }
        timer--;
    }

    private void setupTimer() {
        timer = 30;
        main.setIsStopping(true);
    }

    public void stop() {
        this.cancel();
        main.setIsStopping(false);
    }
}
