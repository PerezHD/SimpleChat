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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterAdvertisingIPAddress extends Filter<AsyncPlayerChatEvent> {

      public FilterAdvertisingIPAddress() {
            super("advertising-ipaddress", AsyncPlayerChatEvent.class);

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());
            }
      }

      private final Pattern ip_address_pattern = Pattern.compile("(?:\\d{1,3}[.,-:;\\/()=?}+ ]{1,4}){3}\\d{1,3}");

      @Override
      public void handleEvent(final AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            Matcher regex_matcher = ip_address_pattern.matcher(ChatColor.stripColor(event.getMessage()));
            while (regex_matcher.find()) {
                  if (regex_matcher.group().length() > 0) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(SimpleChat.getPluginInstance().getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " That message seemed like it contained a ip address. We do not allow players to post ip addresses.");
                        break;
                  }
            }
      }

      @Override
      public void handlePlayerLeave(Player player) {
            if (!super.isEnabled()) {
                  return;
            }
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
