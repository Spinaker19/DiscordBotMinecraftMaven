package be.spinaker.command.backups.max;

import be.spinaker.command.Command;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetMaxBackupsCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;

    public SetMaxBackupsCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        if(args.length >= 1 && isNumeric(args[0])){
            int backupNumber = Integer.parseInt(args[0]);
            plugin.getConfig().set("max_backups", backupNumber);
            plugin.saveConfig();
            player.sendMessage(ChatColor.AQUA + "There will now be " + backupNumber + " backup stored");
        }
        else {
            player.sendMessage(ChatColor.RED + "Usage : /setMaxBackup <Amount of Backups> ");
        }
        return false;
    }

    private boolean isNumeric(String string) {
        if(string == null) return false;
        try {
            double d = Double.parseDouble(string);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}