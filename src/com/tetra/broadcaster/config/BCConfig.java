package com.tetra.broadcaster.config;

import com.tetra.broadcaster.Broadcaster;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Evan
 */
public class BCConfig {

    Broadcaster BC;
    File configLoc;
    YamlConfiguration config = new YamlConfiguration();
    boolean changed = false;
    private int bCooldown;
    private String bFormat;
    private String apbFormat;

    public BCConfig(Broadcaster instance) {
        this.BC = instance;
        this.configLoc = new File(BC.getDataFolder() + "/config.yml");
    }

    public void load() {
        try {
            config.load(configLoc);
            System.out.println("[Broadcaster] Loading config...");
        } catch (Exception ex) {
            System.out.println("[Broadcaster] Generating config...");
        }
        if (!config.contains("general.broadcast.cooldown")) {
            config.set("general.broadcast.cooldown", 1800);
            changed = true;
        } else {
            this.bCooldown = config.getInt("general.broadcast.cooldown");
        }
        if (!config.contains("general.broadcast.format")) {
            config.set("general.broadcast.format", "§3[§8RoguePvP§3] §8'{player}§8'§c - {message}");
            changed = true;
        } else {
            this.bFormat = config.getString("general.broadcast.format");
        }
        if (!config.contains("general.apb.format")) {
            config.set("general.apb.format", "§c[APB Broadcast]§f '{player}'§6 - {message}");
            changed = true;
        } else {
            this.apbFormat = config.getString("general.apb.format");
        }
        if (changed) {
            try {
                config.save(configLoc);
                changed = false;
                load();
            } catch (IOException ex) {
            }
        } else {
            System.out.println("[Broadcaster] Done loading config!");
        }
    }

    public int getBCooldown() {
        return bCooldown;
    }

    public String getBFormat() {
        return bFormat;
    }

    public String getAPBFormat() {
        return apbFormat;
    }
}
