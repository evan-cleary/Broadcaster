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
package com.tetra.broadcaster.handlers;

import com.tetra.broadcaster.Broadcaster;
import com.tetra.broadcaster.config.BCConfig;
import com.tetra.broadcaster.config.PlayerFile;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class BroadcastHandler {

    private Broadcaster plugin;
    private PlayerFile PF;
    private long cooldown;
    private Calendar c;
    private BCConfig config;

    public BroadcastHandler(Broadcaster instance) {
        this.plugin = instance;
        this.PF = plugin.getPlayerFiles();
        this.config = instance.getBConfig();
        this.cooldown = config.getBCooldown() * 1000;
    }

    public String buildString(String[] message) {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (String s : message) {
            if (count != message.length - 1) {
                sb.append(s);
                sb.append(" ");
            } else {
                sb.append(s);
            }
            count++;
        }
        return sb.toString();
    }

    public boolean shoutUsed(Player p) {
        long now = System.currentTimeMillis();
        long started = PF.getBLastUsed(p.getName());
        if (started == 0) {
            return false;
        }
        if (now > started + cooldown) {
            return false;
        }
        return true;
    }

    public void broadcast(Player p, String[] message) {
        String compiledMessage = buildString(message);
        String formattedMessage = config.getBFormat().replace("{player}", p.getDisplayName()).replace("{message}", compiledMessage);
        plugin.getServer().broadcastMessage(formattedMessage);
    }

    public void usedShout(Player p) {
        plugin.getPlayerFiles().setBLastUsed(p.getName());
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

    public void perfAdminShout(Player a, String[] message) {
        if (a.hasPermission(new Permission("broadcaster.staff"))) {
            String msg = buildString(message);
            String formattedMessage = config.getAPBFormat().replace("{player}", a.getDisplayName()).replace("{message}", msg);
            plugin.getServer().broadcastMessage(formattedMessage);
        } else {
            a.sendMessage(ChatColor.RED + "You do not have the required permissions to perform this command. How did you even get to this arguement?");
        }
    }

    public void sendTimeRemain(Player player) {
        long started = PF.getBLastUsed(player.getName());
        long cd = plugin.getBConfig().getBCooldown();
        c = new GregorianCalendar();
        c.setTimeInMillis(started);
        c.add(Calendar.SECOND, (int) cd);
        c.add(Calendar.MILLISECOND, (int) ((cd * 1000.0) % 1000.0));
        player.sendMessage(ChatColor.RED + "You must wait " + Util.formatDateDiff(c.getTimeInMillis()) + " before performing this action.");
    }
}
