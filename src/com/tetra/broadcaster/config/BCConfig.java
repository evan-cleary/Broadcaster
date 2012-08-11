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
    private int sCooldown;

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
        if (!config.contains("general.speed.cooldown")) {
            config.set("general.speed.cooldown", 900);
            changed = true;
        } else {
            this.sCooldown = config.getInt("general.speed.cooldown");
        }
        if (changed) {
            try {
                config.save(configLoc);
                changed = false;
                load();
            } catch (IOException ex) {
            }
        } else {
            System.out.println("[FearForAll] Done loading config!");
        }
    }

    public int getBCooldown() {
        return bCooldown;
    }

    public int getSCooldown() {
        return sCooldown;
    }
}
