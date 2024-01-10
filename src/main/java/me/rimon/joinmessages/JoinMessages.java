package me.rimon.joinmessages;

import org.bukkit.plugin.java.JavaPlugin;

public class JoinMessages extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the event listener for both join and quit events
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        // Register the command executor
        this.getCommand("togglejoinmessage").setExecutor(new ToggleCommand(this));
        // Setup configuration
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
