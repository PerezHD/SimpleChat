/*Copyright (C) Harry5573 2013-14

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.harry5573.chat.listener;

import com.harry5573.chat.SimpleChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Harry5573
 */
public class EventListener implements Listener {

    static SimpleChatPlugin plugin = SimpleChatPlugin.get();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String msg = event.getMessage();

        if (p.hasPermission("simplechat.admin")) {
            return;
        }

        if (plugin.isChatHalted) {
            p.sendMessage(plugin.prefix + ChatColor.GRAY + " Chat is currently halted.");
            event.setCancelled(true);
            return;
        }

        if (plugin.getConfig().getBoolean("blockchatuntilmoved") && plugin.hasntMoved.contains(p.getUniqueId())) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " You cannot chat until you have moved!");
            event.setCancelled(true);
            return;
        }

        if (plugin.getConfig().getBoolean("blockdupemsg") != false) {
            if (plugin.lastMessage.containsKey(p.getUniqueId())) {
                String oldmsg = plugin.lastMessage.get(p.getUniqueId());
                plugin.lastMessage.remove(p.getUniqueId());
                if (msg.contains(oldmsg)) {
                    p.sendMessage(plugin.prefix + ChatColor.RED + " Please do not send duplicate messages!");
                    event.setCancelled(true);
                    plugin.lastMessage.put(p.getUniqueId(), msg);
                    return;
                }
            }
            plugin.lastMessage.put(p.getUniqueId(), msg);
        }

        if (plugin.checkForAdvertising(msg) != 0) {
            plugin.handleAdvertiser(p);
            event.setCancelled(true);
            return;
        }

        // GOODBYE CAPITALS!
        event.setMessage(event.getMessage().toLowerCase());
        event.setMessage(capitalizeFirstLetter(event.getMessage()));
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKick(PlayerKickEvent event) {
        plugin.lastMessage.remove(event.getPlayer().getUniqueId());
        plugin.hasntMoved.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        plugin.lastMessage.remove(event.getPlayer().getUniqueId());
        plugin.hasntMoved.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        plugin.hasntMoved.add(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        plugin.hasntMoved.remove(event.getPlayer().getUniqueId());
    }
}
