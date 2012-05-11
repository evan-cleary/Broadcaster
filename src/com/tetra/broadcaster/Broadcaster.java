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

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.tetra.broadcaster.handlers.BroadcastHandler;

public class Broadcaster extends JavaPlugin {
	public ArrayList<Player> ShoutMarks = new ArrayList<Player>();
	public ArrayList<Integer> ShoutTimers = new ArrayList<Integer>();
	private BroadcastHandler SH = new BroadcastHandler(this);
	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		log.info("Broadcaster has been activated");
	}

	public void onDisable() {
		log.info("Shutting down broadcaster");
	}

	public String getStaff(Player[] players) {
		String OnlineStaff = "";
		for (int i = 0; i < players.length; i++) {
			if (players[i].hasPermission(new Permission("broadcaster.staff"))) {
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

	// Command Handlers
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
				final Player fp = player;
				if (!SH.shoutUsed(player)) {
					if (args.length > 0) {
						SH.usedShout(player);
						this.getServer().broadcastMessage(
								ChatColor.YELLOW + "[" + ChatColor.RED
										+ "FearPvP" + ChatColor.YELLOW + "] "
										+ "'" + ChatColor.WHITE
										+ player.getDisplayName()
										+ ChatColor.RED + "' - "
										+ SH.buildString(args));
						int timer = this.getServer().getScheduler()
								.scheduleSyncDelayedTask(this, new Runnable() {
									public void run() {
										SH.shoutReset(fp);
									}
								}, 36000L);
						ShoutTimers.add(timer);
					} else {
						player.sendMessage(ChatColor.RED + "Usage: "
								+ cmd.getUsage());
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "You have already used that command recently.");
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("shoutlist")) {
			sender.sendMessage("TradeShout List Size: " + ShoutMarks.size());
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("shoutpurge")) {
			SH.shoutPurge();
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("apb")) {
			if (player == null) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				if (args.length > 0) {
					SH.perfAdminShout(player, args);
					return true;
				}
			}
			return false;
		}

		return false;
	}
}
