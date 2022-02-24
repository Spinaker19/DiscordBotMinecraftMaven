package be.spinaker.configs;

import be.spinaker.main.MainDiscordBotMinecraftMaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private MainDiscordBotMinecraftMaven plugin;
    private String name;
    private File saveFile;
    private FileConfiguration config;

    public Config(MainDiscordBotMinecraftMaven plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        loadConfig();
    }

    private void loadConfig() {
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        saveFile = new File(plugin.getDataFolder(), this.name);
        if(!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("Unable to load the " + this.name);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(saveFile);
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void save() {
        try {
            this.config.save(this.saveFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return config;
    }
}
