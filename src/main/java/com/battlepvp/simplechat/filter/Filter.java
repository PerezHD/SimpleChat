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
package com.battlepvp.simplechat.filter;

import com.battlepvp.simplechat.SimpleChat;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public abstract class Filter<E extends Event> {

      @Getter
      private final String name;
      @Getter
      private final ConfigurationSection filter_configuration;
      @Getter
      private final boolean enabled;
      @Getter
      private final Class<? extends Event> filterEvent;

      public Filter(String name, Class<? extends Event> event) {
            this.name = name;
            this.filter_configuration = SimpleChat.getPluginInstance().getCurrentConfiguration().getFilters_section().getConfigurationSection(name);
            this.enabled = filter_configuration.getBoolean("enabled");
            this.filterEvent = event;
      }

      public abstract void handleEvent(E event);

      public abstract void handlePlayerLeave(Player player);

      public abstract void handlePlayerJoin(Player player);

      public abstract void handleCommandEvent(PlayerCommandPreprocessEvent event);
}
