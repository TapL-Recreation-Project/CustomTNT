package me.swipez.customtnt;

import me.swipez.customtnt.command.ExplosionTest;
import me.swipez.customtnt.listeners.TntPlaceListener;
import me.swipez.customtnt.tnt.TntManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomTNT extends JavaPlugin {

    public static CustomTNT plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("explosiontest").setExecutor(new ExplosionTest());
        getServer().getPluginManager().registerEvents(new TntPlaceListener(), this);
        TntManager.initRecipes();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
