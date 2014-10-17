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
import com.battlepvp.simplechat.utils.TimeUtils;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterRateLimit extends Filter<AsyncPlayerChatEvent> {

      private final int max_chats_per_minute;
      private final int kick_deny_join_time_seconds;

      public FilterRateLimit() {
            super("rate-limit", AsyncPlayerChatEvent.class);

            this.max_chats_per_minute = super.getFilter_configuration().getInt("max-chats-per-minute");
            this.kick_deny_join_time_seconds = super.getFilter_configuration().getInt("kick-deny-join-time-seconds");

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());
            }
      }

      private final ConcurrentMap<UUID, AtomicInteger> filtered_players = new ConcurrentHashMap<>();

      @Override
      public void handleEvent(final AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            // Add the player if they are not in the map.
            if (!filtered_players.containsKey(event.getPlayer().getUniqueId())) {
                  filtered_players.put(event.getPlayer().getUniqueId(), new AtomicInteger(0));
            }

            // Increment the player.
            int new_chat_count = filtered_players.get(event.getPlayer().getUniqueId()).incrementAndGet();

            // Kick them if they have gone over the rate limit.
            if (new_chat_count >= max_chats_per_minute) {
                  event.setCancelled(true);
                  SimpleChat.getPluginInstance().getFilterListener().getBanned_players().put(event.getPlayer().getUniqueId(), (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(kick_deny_join_time_seconds)));
                  SimpleChat.getPluginInstance().getServer().getScheduler().runTask(SimpleChat.getPluginInstance(), new Runnable() {
                        public void run() {
                              event.getPlayer().kickPlayer(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You exceeded the maximum times you are allowed to chat per minute. You have been banned for " + TimeUtils.toPrettyFromSeconds(kick_deny_join_time_seconds) + ".");
                        }
                  });
                  return;
            }

            // Warn them for under 3 times left.
            if (max_chats_per_minute - new_chat_count <= 3) {
                  event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " Slow down the rate at which you are sending messages or you will be temporarily banned.");
            }

            final UUID f_uuid = event.getPlayer().getUniqueId();
            // Otherwise we schedule the removal.
            SimpleChat.getPluginInstance().getServer().getScheduler().runTaskLater(SimpleChat.getPluginInstance(), new Runnable() {
                  public void run() {
                        if (filtered_players.containsKey(f_uuid)) {
                              int new_value = filtered_players.get(f_uuid).decrementAndGet();
                              if (new_value <= 0) {
                                    filtered_players.remove(f_uuid);
                              }
                        }
                  }
                  //60 seconds.
            }, 1200L);
      }

      @Override
      public void handlePlayerLeave(Player player) {
            if (!super.isEnabled()) {
                  return;
            }

            filtered_players.remove(player.getUniqueId());
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
