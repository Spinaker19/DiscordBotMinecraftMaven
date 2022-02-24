package be.spinaker.configs;

import be.spinaker.main.MainDiscordBotMinecraftMaven;

public enum ConfigType {
    CONFIG("config.yml", null),
    TODO("todo.yml", null),
    KILLCOUNT("kill.yml", null);

    private String name;
    private Config config;

    ConfigType(String name, Config config)
    {
        this.name = name;

    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return this.config;
    }

    public void save() {
        this.config.save();
    }

    public String getName() {return this.name;}
}
