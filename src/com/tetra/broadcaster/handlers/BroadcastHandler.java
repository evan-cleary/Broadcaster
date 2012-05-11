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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.tetra.broadcaster.Broadcaster;

public class BroadcastHandler {
	private Broadcaster plugin;

	public BroadcastHandler(Broadcaster instance) {
		this.plugin = instance;
	}

	public String buildString(String[] message) {
		StringBuilder sb = new StringBuilder();
		for (String s : message) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

	public boolean shoutUsed(Player p) {
			if (plugin.ShoutMarks.contains(p)) {
				return true;
			}
		return false;
	}

	public void usedShout(Player p) {
		plugin.ShoutMarks.add(p);
	}

	public void shoutReset(Player p) {
		plugin.ShoutTimers.remove(plugin.ShoutMarks.indexOf(p));
		plugin.ShoutMarks.remove(p);
	}

	public String getStaff(Player[] players) {
		String OnlineStaff = "";
		for (int i = 0; i < players.length; i++) {
			if (players[i]
					.hasPermission(new Permission("broadcaster.staff"))) {
				if (OnlineStaff == "") {
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

	public void shoutPurge() {
		plugin.ShoutMarks.clear();
		for(int t: plugin.ShoutTimers){
			plugin.getServer().getScheduler().cancelTask(t);
		}
		plugin.ShoutTimers.clear();
	}

	public void perfAdminShout(Player a, String[] message) {
		if (a.hasPermission(new Permission("broadcaster.staff"))) {
			plugin.getServer().broadcastMessage(
					ChatColor.RED + "[APB Broadcast] " + ChatColor.WHITE+"'"
							+ a.getDisplayName()+"'" + ChatColor.GOLD + " - "
							+ buildString(message));
		} else {
			a.sendMessage(ChatColor.RED
					+ "You do not have the required permissions to perform this command. How did you even get to this arguement?");
		}
	}
}
