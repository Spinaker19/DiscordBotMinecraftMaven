package be.spinaker.command.backups.max;

import be.spinaker.command.Command;
import be.spinaker.configs.ConfigType;
import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GetMaxBackupsCmd extends Command {

    private MainDiscordBotMinecraftMaven plugin;

    public GetMaxBackupsCmd(MainDiscordBotMinecraftMaven plugin, String name)
    {
        super(plugin, name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args)
    {
        player.sendMessage(ChatColor.AQUA + "The maximum numbers of backups stored is : " + plugin.getConfig().getInt("max_backups"));
        return false;
    }
}