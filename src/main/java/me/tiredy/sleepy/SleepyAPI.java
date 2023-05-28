package me.tiredy.sleepy;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class SleepyAPI {
    private final JavaPlugin plugin;
    private final BukkitAudiences audiences;

    public SleepyAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create(plugin);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public BukkitAudiences getAudiences() {
        return audiences;
    }
}
