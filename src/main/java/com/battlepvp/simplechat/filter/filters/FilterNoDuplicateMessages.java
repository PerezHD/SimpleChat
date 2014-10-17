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
package com.battlepvp.simplechat.filter.filters;

import com.battlepvp.simplechat.SimpleChat;
import com.battlepvp.simplechat.filter.Filter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterNoDuplicateMessages extends Filter<AsyncPlayerChatEvent> {

      private final int timeout_time_seconds;

      public FilterNoDuplicateMessages() {
            super("no-duplicate-messages", AsyncPlayerChatEvent.class);

            this.timeout_time_seconds = super.getFilter_configuration().getInt("timeout-time-seconds");

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());
            }
      }

      private final ConcurrentMap<UUID, LastChatData> players = new ConcurrentHashMap<>();

      @RequiredArgsConstructor
      class LastChatData {

            @Getter
            private final long time;
            @Getter
            private final String message;
      }

      @Override
      public void handleEvent(final AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            if (players.containsKey(event.getPlayer().getUniqueId())) {
                  LastChatData last_data = players.get(event.getPlayer().getUniqueId());

                  boolean should_check_timeout = timeout_time_seconds > -1;
                  if (!should_check_timeout && last_data.getMessage().toLowerCase().equals(ChatColor.stripColor(event.getMessage().toLowerCase()))) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " Stop trying to send duplicate messages.");
                  } else {
                        if ((System.currentTimeMillis() <= (last_data.getTime() + (timeout_time_seconds * 1000))) && last_data.getMessage().toLowerCase().equals(ChatColor.stripColor(event.getMessage().toLowerCase()))) {
                              event.setCancelled(true);
                              event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " Stop trying to send duplicate messages in such a short period of time.");
                        }
                  }
            }

            // We need to apply the updated attempt to the map.
            players.put(event.getPlayer().getUniqueId(), new LastChatData(System.currentTimeMillis(), ChatColor.stripColor(event.getMessage())));
      }

      @Override
      public void handlePlayerLeave(Player player) {
            if (!super.isEnabled()) {
                  return;
            }

            players.remove(player.getUniqueId());
      }

      @Override
      public void handlePlayerJoin(Player player) {
            if (!super.isEnabled()) {
                  return;
            }
      }

      @Override
      public void handleCommandEvent(PlayerCommandPreprocessEvent event) {
            if (!super.isEnabled()) {
                  return;
            }
      }

}
