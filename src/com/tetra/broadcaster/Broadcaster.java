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
import com.tetra.broadcaster.handlers.SpeedHandler;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Broadcaster extends JavaPlugin {

    private BroadcastHandler BH;
    private SpeedHandler SH;
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
        this.SH = new SpeedHandler(this);
        log.info("Broadcaster has been activated");
    }

    @Override
    public void onDisable() {
        log.info("Shutting down broadcaster");
    }

    public String getStaff(Player[] players) {
        String OnlineStaff = "";
        for (int i = 0; i < players.length; i++) {
            if (players[i].hasPermission(new Permission("broadcaster.staff"))) {
                if (OnlineStaff.equals("")) {
                    OnlineStaff += (players[i].getName());
                } else {
                    OnlineStaff += (" " + players[i].getName());
                }
            }
        }
        if (!OnlineStaff.equals("")) {
            return OnlineStaff.replace(" ", ", ");
        } else {
            return "None";
        }
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
                        String msg = BH.buildString(args);
                        char[] breakDown = msg.toCharArray();
                        int capCount = 0;
                        for (char c : breakDown) {
                            if (Character.isUpperCase(c)) {
                                capCount++;
                            }
                        }
                        this.getServer().broadcastMessage(
                                ChatColor.YELLOW + "[" + ChatColor.RED
                                + "FearPvP" + ChatColor.YELLOW + "] "
                                + "'" + ChatColor.WHITE
                                + player.getDisplayName()
                                + ChatColor.RED + "' - "
                                + msg);
                        BH.usedShout(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: "
                                + cmd.getUsage());
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

        if (cmd.getName().equalsIgnoreCase("speed")) {
            if (player == null) {
                sender.sendMessage("This command can only be run by a player");
            } else {
                if (!SH.speedUsed(player)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.SPEED, 600, 1);
                    player.addPotionEffect(pe);
                    SH.usedSpeed(player);
                    return true;
                } else {
                    SH.sendTimeRemain(player);
                    return true;
                }
            }
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
