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
package com.battlepvp.simplechat;

import com.battlepvp.simplechat.commands.CommandChat;
import com.battlepvp.simplechat.configuration.Configuration;
import com.battlepvp.simplechat.filter.FilterRegistry;
import com.battlepvp.simplechat.listener.FilterListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class SimpleChat extends JavaPlugin {

      @Getter
      private static SimpleChat pluginInstance;
      @Getter
      private FilterRegistry filterRegistry;
      @Getter
      private Configuration currentConfiguration;
      @Getter
      private FilterListener filterListener;
      @Getter
      @Setter
      private boolean chatMuted = false;

      @Override
      public void onEnable() {
            pluginInstance = this;
            final long start_time = System.currentTimeMillis();

            logMessage("=[ Plugin version " + getDescription().getVersion() + " starting ]=");

            cancelTasks();

            this.filterRegistry = new FilterRegistry(this);

            reload();

            this.filterListener = new FilterListener(this);
            new CommandChat(this);

            logMessage("=[ Plugin version " + getDescription().getVersion() + " started in " + (System.currentTimeMillis() - start_time) + "ms ]=");
      }

      @Override
      public void onDisable() {
            logMessage("=[ Plugin version " + getDescription().getVersion() + " shutting down ]=");

            cancelTasks();

            logMessage("=[ Plugin version " + getDescription().getVersion() + " shutdown ]=");
      }

      public void logMessage(String message) {
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[" + getName() + "] " + ChatColor.WHITE + message);
      }

      private void cancelTasks() {
            getServer().getScheduler().cancelTasks(this);
      }

      public void reload() {
            saveDefaultConfig();
            reloadConfig();

            this.currentConfiguration = new Configuration(getConfig());
            this.filterRegistry.loadFilters();
      }
}
