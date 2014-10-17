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
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterNoSwear extends Filter<AsyncPlayerChatEvent> {

      public FilterNoSwear() {
            super("no-swear", AsyncPlayerChatEvent.class);

            if (super.isEnabled()) {
                  SimpleChat.getPluginInstance().logMessage("Loaded and enabled filter: " + super.getName());
            }
      }

      final List<Character> delimeters = Arrays.asList('.', ',', ';', ':', '-', '_', '|', '/', '?', '(', ')', '!', '@', '#', '4', '5', '6', '7', '8', '9', '0', '`', '~', '+', '<', '>');

      private String getRemovedDelimeters(String string) {
            String noDelimeterWord = "";
            for (char c : string.toCharArray()) {
                  if (!delimeters.contains(c)) {
                        noDelimeterWord = noDelimeterWord + c;
                  }
            }
            return noDelimeterWord;
      }

      private boolean isSwearWord(String word) {
            /* BASIC CHECK */
            if (SimpleChat.getPluginInstance().getCurrentConfiguration().getSwear_words().contains(word.toLowerCase())) {
                  return true;
            }

            /* DELIMETER REDUCTION CHECK */
            String noDelimeterWord = getRemovedDelimeters(word);
            for (String sw : SimpleChat.getPluginInstance().getCurrentConfiguration().getSwear_words()) {
                  if (noDelimeterWord.toLowerCase().equals(sw)) {
                        return true;
                  }
            }

            return false;
      }

      private static String buildPlaceholders(int length, String placeHolderChar) {
            StringBuilder placeHolderBuilder = new StringBuilder();
            for (int i = 1; i <= length; i++) {
                  placeHolderBuilder.append(placeHolderChar);
            }
            return placeHolderBuilder.toString();
      }

      @Override
      public void handleEvent(final AsyncPlayerChatEvent event) {
            if (!super.isEnabled()) {
                  return;
            }

            StringBuilder newMessage = new StringBuilder();
            for (String word : event.getMessage().split(" ")) {
                  if (newMessage.length() > 0) {
                        newMessage.append(" ");
                  }

                  if (word.length() <= 1) {
                        newMessage.append(word);
                        continue;
                  }

                  if (isSwearWord(word.toLowerCase())) {
                        newMessage.append(buildPlaceholders(getRemovedDelimeters(word).length(), "*"));
                  } else {
                        newMessage.append(word);
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
