package com.tetra.broadcaster.config;

import com.tetra.broadcaster.Broadcaster;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Evan
 */
public class PlayerFile {

    Broadcaster BC;
    File userdataPath;
    YamlConfiguration config = new YamlConfiguration();
    boolean changed = false;

    public PlayerFile(Broadcaster instance) {
        this.BC = instance;
        userdataPath = new File(BC.getDataFolder() + "/userdata");
        if (!userdataPath.exists()) {
            userdataPath.mkdirs();
        }
    }

    public long getBLastUsed(String player_name) {
        String target = player_name.toLowerCase();
        File userfile = new File(userdataPath + "/" + target + ".yml");
        YamlConfiguration userdata = new YamlConfiguration();
        try {
            userdata.load(userfile);
            return userdata.getLong("general.broadcast.lastused", 0);
        } catch (Exception ex) {
        }
        return 0;
    }

    public void setBLastUsed(String player_name) {
        String target = player_name.toLowerCase();
        File userfile = new File(userdataPath + "/" + target + ".yml");
        YamlConfiguration userdata = new YamlConfiguration();
        try {
            userdata.load(userfile);
        } catch (Exception ex) {
        }
        try {
            userdata.set("general.broadcast.lastused", System.currentTimeMillis());
            userdata.save(userfile);
        } catch (Exception ex2) {
        }
    }
}
