package com.tetra.broadcaster.handlers;

import com.tetra.broadcaster.Broadcaster;
import com.tetra.broadcaster.config.PlayerFile;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class SpeedHandler {

    private Broadcaster plugin;
    private PlayerFile PF;
    private long cooldown;
    private Calendar c;

    public SpeedHandler(Broadcaster instance) {
        this.plugin = instance;
        this.PF = plugin.getPlayerFiles();
        this.cooldown = plugin.getBConfig().getSCooldown() * 1000;
    }

    public boolean speedUsed(Player p) {
        long now = System.currentTimeMillis();
        long started = PF.getSLastUsed(p.getName());
        if (started == 0) {
            return false;
        }
        if (now > started + cooldown) {
            return false;
        }
        return true;
    }

    public void usedSpeed(Player p) {
        plugin.getPlayerFiles().setSLastUsed(p.getName());
    }

    public void sendTimeRemain(Player player) {
        long started = PF.getSLastUsed(player.getName());
        long cd = plugin.getBConfig().getSCooldown();
        c = new GregorianCalendar();
        c.setTimeInMillis(started);
        c.add(Calendar.SECOND, (int) cd);
        c.add(Calendar.MILLISECOND, (int) ((cd * 1000.0) % 1000.0));
        player.sendMessage(ChatColor.RED
                + "You must wait "
                + Util.formatDateDiff(c.getTimeInMillis()) + " before performing this action.");
    }
}
