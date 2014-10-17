/*
 * Copyright (C) 2014 Harry Devane
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.battlepvp.simplechat.listener;

import com.battlepvp.simplechat.SimpleChat;
import com.battlepvp.simplechat.filter.Filter;
import com.battlepvp.simplechat.utils.TimeUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterListener implements Listener {

      private final SimpleChat pluginInstance;

      public FilterListener(SimpleChat pluginInstance) {
            this.pluginInstance = pluginInstance;
            this.pluginInstance.getServer().getPluginManager().registerEvents(this, pluginInstance);
      }

      // 
      // BANS
      //
      @Getter
      private HashMap<UUID, Long> banned_players = new HashMap<>();

      @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
      public void onBannedPlayerJoin(AsyncPlayerPreLoginEvent event) {
            if (banned_players.containsKey(event.getUniqueId())) {
                  // If the ban has expired.
                  if (banned_players.get(event.getUniqueId()) <= System.currentTimeMillis()) {
                        banned_players.remove(event.getUniqueId());
                        return;
                  }

                  event.setResult(PlayerPreLoginEvent.Result.KICK_BANNED);
                  event.setKickMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You are banned from this server for " + TimeUtils.getCountdownStringFromLong(banned_players.get(event.getUniqueId()) - System.currentTimeMillis()) + ChatColor.RED + ".");
            }
      }

      @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
      public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("simplechat.admin")) {
                  return;
            }

            if (pluginInstance.isChatMuted()) {
                  event.setCancelled(true);
                  event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " The chat is currently globally muted.");
                  return;
            }

            Iterator<Filter> filters = pluginInstance.getFilterRegistry().getFilters().iterator();

            while (filters.hasNext()) {
                  Filter next_filter = filters.next();

                  if (next_filter.getFilterEvent().isInstance(event)) {
                        next_filter.handleEvent(event);

                        // Did the filter cancel the event?
                        if (event.isCancelled()) {
                              break;
                        }
                  }
            }
      }

      @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
      public void onPlayerQuit(PlayerQuitEvent event) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("simplechat.admin")) {
                  return;
            }

            Iterator<Filter> filters = pluginInstance.getFilterRegistry().getFilters().iterator();

            while (filters.hasNext()) {
                  filters.next().handlePlayerLeave(event.getPlayer());
            }
      }

      @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
      public void onPlayerJoin(PlayerJoinEvent event) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("simplechat.admin")) {
                  return;
            }

            Iterator<Filter> filters = pluginInstance.getFilterRegistry().getFilters().iterator();

            while (filters.hasNext()) {
                  filters.next().handlePlayerJoin(event.getPlayer());
            }
      }

      @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
      public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("simplechat.admin")) {
                  return;
            }

            Iterator<Filter> filters = pluginInstance.getFilterRegistry().getFilters().iterator();

            while (filters.hasNext()) {
                  filters.next().handleCommandEvent(event);
            }
      }
}
