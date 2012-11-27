/* Broadcaster, Adds broadcast and social commands to Bukkit.
 Copyright (C) 2012  Evan Cleary

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.tetra.broadcaster;

import com.tetra.broadcaster.config.BCConfig;
import com.tetra.broadcaster.config.PlayerFile;
import com.tetra.broadcaster.handlers.BroadcastHandler;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Broadcaster extends JavaPlugin {

    private BroadcastHandler BH;
    private static Broadcaster instance;
    static final Logger log = Logger.getLogger("Minecraft");
    private BCConfig config;
    private PlayerFile PF;

    @Override
    public void onEnable() {
        Broadcaster.instance = this;
        this.config = new BCConfig(this);
        config.load();
        this.PF = new PlayerFile(this);
        this.BH = new BroadcastHandler(this);
        log.info("Broadcaster has been activated");
    }

    @Override
    public void onDisable() {
        log.info("Shutting down broadcaster");
    }

    // Command Handlers
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (cmd.getName().equalsIgnoreCase("shout")) {
            if (player == null) {
                sender.sendMessage("This command can only be run by a player");
            } else {
                if (!BH.shoutUsed(player)) {
                    if (args.length > 0) {
                        BH.broadcast(player, args);
                        BH.usedShout(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
                    }
                } else {
                    BH.sendTimeRemain(player);
                }
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("apb")) {
            if (player == null) {
                sender.sendMessage("This command can only be run by a player.");
            } else {
                if (args.length > 0) {
                    BH.perfAdminShout(player, args);
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    public static Broadcaster getInstance() {
        return instance;
    }

    public BCConfig getBConfig() {
        return config;
    }

    public PlayerFile getPlayerFiles() {
        return PF;
    }
}
