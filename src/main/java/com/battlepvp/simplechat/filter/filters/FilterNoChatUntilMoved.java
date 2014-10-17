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
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterNoChatUntilMoved extends Filter<AsyncPlayerChatEvent> {

      private final int needed_blocks_to_move;

      public FilterNoChatUntilMoved() {
            super("no-chat-until-moved", AsyncPlayerChatEvent.class);

            this.needed_blocks_to_move = super.getFilter_configuration().getInt("needed-blocks-to-move");

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());

                  // Player move task
                  SimpleChat.getPluginInstance().getServer().getScheduler().runTaskTimer(SimpleChat.getPluginInstance(), new Runnable() {
                        public void run() {
                              for (Player player : SimpleChat.getPluginInstance().getServer().getOnlinePlayers()) {
                                    if (player_location_storage.containsKey(player.getUniqueId())) {
                                          Location stored_location = player_location_storage.get(player.getUniqueId());

                                          if (!player.getLocation().getWorld().getName().equals(stored_location.getWorld().getName()) || player.getLocation().distance(stored_location) >= needed_blocks_to_move) {
                                                player_location_storage.remove(player.getUniqueId());
                                          }
                                          continue;
                                    }
                              }
                        }
                  }, 20L, 20L);
            }
      }

      private final ConcurrentMap<UUID, Location> player_location_storage = new ConcurrentHashMap<>();
      private final DecimalFormat formatter = new DecimalFormat("#.#");

      @Override
      public void handleEvent(AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            if (player_location_storage.containsKey(event.getPlayer().getUniqueId())) {
                  Location stored_location = player_location_storage.get(event.getPlayer().getUniqueId());

                  if (!event.getPlayer().getLocation().getWorld().getName().equals(stored_location.getWorld().getName()) || event.getPlayer().getLocation().distance(stored_location) >= needed_blocks_to_move) {
                        player_location_storage.remove(event.getPlayer().getUniqueId());
                        return;
                  }

                  event.setCancelled(true);
                  event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You need to move " + formatter.format(needed_blocks_to_move - event.getPlayer().getLocation().distance(stored_location)) + " blocks to be able to chat.");
            }
      }

      @Override
      public void handlePlayerLeave(Player player) {
            if (!super.isEnabled()) {
                  return;
            }

            player_location_storage.remove(player.getUniqueId());
      }

      @Override
      public void handlePlayerJoin(Player player) {
            if (!super.isEnabled()) {
                  return;
            }

            player_location_storage.put(player.getUniqueId(), player.getLocation());
      }

      @Override
      public void handleCommandEvent(PlayerCommandPreprocessEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            if (player_location_storage.containsKey(event.getPlayer().getUniqueId())) {
                  Location stored_location = player_location_storage.get(event.getPlayer().getUniqueId());

                  if (!event.getPlayer().getLocation().getWorld().getName().equals(stored_location.getWorld().getName()) || event.getPlayer().getLocation().distance(stored_location) >= needed_blocks_to_move) {
                        player_location_storage.remove(event.getPlayer().getUniqueId());
                        return;
                  }

                  event.setCancelled(true);
                  event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You need to move " + formatter.format(needed_blocks_to_move - event.getPlayer().getLocation().distance(stored_location)) + " blocks to be able to chat.");
            }
      }

}
