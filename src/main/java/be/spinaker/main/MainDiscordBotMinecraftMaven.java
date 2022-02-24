package be.spinaker.main;

import be.spinaker.backupworld.BackUp;
import be.spinaker.backupworld.BackUpClock;
import be.spinaker.command.backups.BackupCmd;
import be.spinaker.command.backups.interval.GetBackupsIntervalCmd;
import be.spinaker.command.backups.interval.SetBackupsIntervalCmd;
import be.spinaker.command.backups.max.GetMaxBackupsCmd;
import be.spinaker.command.backups.max.SetMaxBackupsCmd;
import be.spinaker.command.server.CancelCmd;
import be.spinaker.command.server.HelpCmd;
import be.spinaker.command.server.StopCmd;
import be.spinaker.command.todolist.ToDoListCmd;
import be.spinaker.command.todolist.TodoListTabCompleter;
import be.spinaker.configs.Config;
import be.spinaker.configs.ConfigType;
import be.spinaker.killcounter.KillCountCommands;
import be.spinaker.killcounter.KillCounter;
import be.spinaker.minecraft.MinecraftListner;
import be.spinaker.discordbot.DiscordCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.TextChannel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public final class MainDiscordBotMinecraftMaven extends JavaPlugin {

    private static final String token = "OTIwNTg1NjgwMzQwNTI5MjQy.YbmgOQ.NFPvuVLm39j_u7aaaRMgw5k7iv4";
    private JDA jda;
    private DiscordCommands discordCommands;
    private MinecraftListner minecraftListner;
    private TextChannel chatchannel;
    private TextChannel commandChannel;
    private Cooldown cooldown;
    private boolean isStopping = false;
    private Map<String, String> advancements = new HashMap<>();
    private BackUpClock backUpClock;
    private BackUp backUp;

    @Override
    public void onEnable() {

        System.out.println("Discord bot loading...");


        /*
        // Mit dans configManager.loadConfigs()

        saveDefaultConfig();
        ConfigurationSection configurationSection = getConfig().getConfigurationSection("advancementMap");
        System.out.println(configurationSection);
        if(configurationSection != null) {
            System.out.println("config section not null");
            System.out.println(configurationSection.getKeys(false));
            for(String key : configurationSection.getKeys(false)) {
                System.out.println("coucou");
                System.out.println(configurationSection.getString(key));
                advancements.put(key, configurationSection.getString(key));
            }
            System.out.println("advancements maps is : " + advancements);
        }
        */



        jda = startBot();
        if(jda == null) {
            Bukkit.broadcastMessage("Unable to load the discordBotPlugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        cooldown = new Cooldown(this);

        backUp = new BackUp(this);
        backUpClock = new BackUpClock(this);
        backUpClock.runTaskTimer(this, 0, 20);

        setUpConfigs();
        setUpComs();
        setUpMinecraftCommands();
        setUpToDoList();
        setUpKillCounter();
        /*discordCommands.sendMessage("The server is now online !");
        discordCommands.sendRespondToCommand("Server is online !");*/

        System.out.println("Discord bot loaded");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin shutting down...");

        //save the timer for backup
        int timer = backUpClock.getTimer();
        System.out.println("Timer is " + timer);
        getConfig().set("timer_backup", timer);
        System.out.println(getConfig().get("timer_backup"));
        saveConfig();

        if(jda != null) jda.shutdownNow();
        System.out.println("Plugin shutdown successfully !");
    }

    private JDA startBot() {
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.setStatus(OnlineStatus.ONLINE);
            return jdaBuilder.build().awaitReady();
        } catch(LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUpComs() {
        chatchannel = jda.getTextChannelById(937943567987523594L);
        commandChannel = jda.getTextChannelById(937943591718899743L);

        discordCommands = new DiscordCommands(this);
        jda.addEventListener(discordCommands);

        minecraftListner = new MinecraftListner(this);
        getServer().getPluginManager().registerEvents(minecraftListner, this);

    }

    private void setUpMinecraftCommands() {

        // J'ai pas su mettre sous la même forme que les autres vu qu'il faut garder la possibilité que
        // la commande soit envoyée par la console
        this.getCommand("stop").setExecutor(new StopCmd(this));
        new CancelCmd(this, "cancel");

        new BackupCmd(this, "backup");

        new GetMaxBackupsCmd(this, "getMaxBackups");
        new SetMaxBackupsCmd(this, "setMaxBackups");

        new GetBackupsIntervalCmd(this, "getBackupsInterval");
        new SetBackupsIntervalCmd(this, "setBackupsInterval");

        new HelpCmd(this, "help");

        // Tester de rebuild en mettant en commentaire ?
        /*
        MinecraftCommands minecraftCommands = new MinecraftCommands(this);
        this.getCommand("stop").setExecutor(minecraftCommands);
        this.getCommand("backup").setExecutor(minecraftCommands);
        this.getCommand("cancel").setExecutor(minecraftCommands);
        this.getCommand("getMaxBackups").setExecutor(minecraftCommands);
        this.getCommand("setMaxBackups").setExecutor(minecraftCommands);
        this.getCommand("setBackupsInterval").setExecutor(minecraftCommands);
        this.getCommand("getBackupsInterval").setExecutor(minecraftCommands);
        this.getCommand("help").setExecutor(minecraftCommands);
        */

    }

    private void setUpToDoList() {
        ToDoListCmd toDoListCmd = new ToDoListCmd(this, "todo");
        this.getCommand("todo").setExecutor(toDoListCmd);
        this.getCommand("todo").setTabCompleter(new TodoListTabCompleter());
    }

    private void setUpKillCounter() {
        KillCounter killCounter = new KillCounter(this);
        KillCountCommands killCountCommands = new KillCountCommands(this);
        Bukkit.getPluginManager().registerEvents(killCounter, this);
        this.getCommand("killCount").setExecutor(killCountCommands);
    }

    private void setUpConfigs() {
        ConfigType.CONFIG.setConfig(new Config(this, ConfigType.CONFIG.getName()));
        ConfigType.TODO.setConfig(new Config(this, ConfigType.TODO.getName()));
        ConfigType.KILLCOUNT.setConfig(new Config(this, ConfigType.KILLCOUNT.getName()));
    }

    public boolean isStopping() {
        return isStopping;
    }

    public void setIsStopping(boolean b) {
        isStopping = b;
    }

    public void resetCooldown() {
        cooldown = new Cooldown(this);
    }
    public Cooldown getCooldown() {
        return cooldown;
    }
    public BackUp getBackup() {
        return backUp;
    }
    public Map<String, String> getAdvancements() {
        return advancements;
    }
    public TextChannel getChatChannel() {
        return chatchannel;
    }
    public TextChannel getCommandChannel() {
        return  commandChannel;
    }
    public MinecraftListner getMinecraftListner() {
        return minecraftListner;
    }
    public DiscordCommands getDiscordCommands() {
        return discordCommands;
    }
    public JDA getJda() {
        return jda;
    }
}
