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

import com.harry5573.chat.core.SimpleChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Harry5573
 */
public class EventListener implements Listener {

    SimpleChat plugin;

    public EventListener(SimpleChat instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        if (p.hasPermission("simplechat.admin")) {
            return;
        }

        if (plugin.isChatHalted) {
            e.getPlayer().sendMessage(ChatColor.GRAY + "Chat is currently halted.");
            e.setCancelled(true);
            return;
        }

        if (plugin.getConfig().getBoolean("blockchatuntilmoved") && plugin.hasntMoved.contains(p)) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " You cannot chat until you have moved!");
            e.setCancelled(true);
            return;
        }

        // Check they are not in the cooldown
        if (plugin.cantChat.contains(p)) {
            p.sendMessage(ChatColor.RED + "You cannot chat again yet! (" + plugin.getConfig().getInt("delay") + " second delay)");
            e.setCancelled(true);
            return;
        }

        // Stop duplicate messages
        if (plugin.getConfig().getBoolean("blockdupemsg") != false) {
            
            if (plugin.lastMessage.containsKey(p)) {
                String oldmsg = plugin.lastMessage.get(p);
                plugin.lastMessage.remove(p);
                if (msg.contains(oldmsg)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Please do not send duplicate messages!");
                    e.setCancelled(true);
                }
            }

            //After all that add them
            plugin.lastMessage.put(p, msg);
        }

        // Check if there advertising
        int i = plugin.checkForAdvertising(msg);
        if (i != 0) {
            plugin.handleAdvertiser(p);
            e.setCancelled(true);
        }
        
        plugin.chatCooldown(p);
        
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!plugin.hasntMoved.contains(p)) {
            plugin.hasntMoved.add(p);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {

        if ((e.getFrom().getBlockX() == e.getTo().getBlockX()) && (e.getFrom().getBlockZ() == e.getTo().getBlockZ())) {
            return;
        }
        Player p = e.getPlayer();
        if (plugin.hasntMoved.contains(p)) {
            plugin.hasntMoved.remove(p);
        }
    }
}
