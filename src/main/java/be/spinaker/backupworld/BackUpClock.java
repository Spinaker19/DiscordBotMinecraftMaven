package be.spinaker.backupworld;

import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class BackUpClock extends BukkitRunnable {

    private int timer;
    private MainDiscordBotMinecraftMaven main;
    private BackUp backUp;

    public BackUpClock(MainDiscordBotMinecraftMaven plugin) {
        timer = plugin.getConfig().getInt("timer_backup");
        System.out.println("Timer for backup is : " + timer);
        main = plugin;
        backUp = main.getBackup();
    }

    @Override
    public void run() {
        switch(timer) {
            case(60):
                Bukkit.broadcastMessage(ChatColor.GREEN + "Backup in 1min");
                break;
            case(30):
                Bukkit.broadcastMessage(ChatColor.GREEN + "Backup in 30s");
                break;
            case(10):
                Bukkit.broadcastMessage(ChatColor.GREEN + "Backup in 10s");
                break;
            case(0):
                Bukkit.broadcastMessage(ChatColor.GREEN + "Saving the world...");
                backUp.make();
                timer = main.getConfig().getInt("default_timer");
                break;
        }

        timer--;
    }

    public int getTimer() {
        return timer;
    }
}
