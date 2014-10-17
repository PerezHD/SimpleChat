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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterMaxUppercaseLetters extends Filter<AsyncPlayerChatEvent> {

      private final int max_uppercase_letters;

      public FilterMaxUppercaseLetters() {
            super("max-uppercase-letters", AsyncPlayerChatEvent.class);

            this.max_uppercase_letters = super.getFilter_configuration().getInt("max-uppercase-letters");

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());
            }
      }

      @Override
      public void handleEvent(final AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            int current_uppercase_count = 0;
            StringBuilder newMessage = new StringBuilder();
            for (char c : event.getMessage().toCharArray()) {
                  if (Character.isUpperCase(c)) {
                        current_uppercase_count += 1;

                        if (current_uppercase_count > max_uppercase_letters) {
                              newMessage.append(Character.toLowerCase(c));
                        } else {
                              newMessage.append(c);
                        }
                  } else {
                        newMessage.append(c);
                  }
            }

            event.setMessage(newMessage.toString());
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
