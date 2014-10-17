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
package com.battlepvp.simplechat.configuration;

import com.battlepvp.simplechat.SimpleChat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Cleanup;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class Configuration {

      @Getter
      private final String message_prefix;
      @Getter
      private final ConfigurationSection filters_section;
      @Getter
      private final CopyOnWriteArrayList<String> swear_words;

      public Configuration(FileConfiguration configuration_file) {
            this.message_prefix = ChatColor.translateAlternateColorCodes('&', configuration_file.getString("message_prefix"));
            this.filters_section = configuration_file.getConfigurationSection("filters");

            this.swear_words = new CopyOnWriteArrayList<>();
            @Cleanup
            Scanner scanner = new Scanner(SimpleChat.getPluginInstance().getResource("swearWords.txt"));

            while (scanner.hasNext()) {
                  String next = scanner.next().toLowerCase();
                  if (!swear_words.contains(next)) {
                        swear_words.add(next);
                  }
            }
      }

}
