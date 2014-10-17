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
package com.battlepvp.simplechat.commands;

import com.battlepvp.simplechat.SimpleChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class CommandChat implements CommandExecutor {

      private final SimpleChat plugin_instance;

      public CommandChat(SimpleChat plugin_instance) {
            this.plugin_instance = plugin_instance;
            this.plugin_instance.getCommand("chat").setExecutor(this);
      }

      @Override
      public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
            if (!sender.isOp() && !sender.hasPermission("simplechat.admin")) {
                  sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You do not have permission to execute this command.");
                  return true;
            }

            if (args.length == 1 && args[0].toLowerCase().startsWith("clear")) {
                  for (int i = 0; i < 100; i++) {
                        for (Player player : plugin_instance.getServer().getOnlinePlayers()) {
                              player.sendMessage(" ");
                        }
                  }
                  for (Player player : plugin_instance.getServer().getOnlinePlayers()) {
                        player.sendMessage(ChatColor.AQUA + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                        player.sendMessage(ChatColor.GREEN + "The chat has been cleared by " + ChatColor.YELLOW + (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName()) + ChatColor.GREEN + ".");
                        player.sendMessage(ChatColor.AQUA + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                  }

                  sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.GREEN + " You have successfuly cleared the chat.");
                  return true;
            }

            if (args.length == 1 && args[0].toLowerCase().startsWith("toggle")) {
                  if (plugin_instance.isChatMuted()) {
                        for (Player player : plugin_instance.getServer().getOnlinePlayers()) {
                              player.sendMessage(ChatColor.DARK_GREEN + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                              player.sendMessage(ChatColor.GREEN + "The chat has been unmuted by " + ChatColor.YELLOW + (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName()) + ChatColor.GREEN + ".");
                              player.sendMessage(ChatColor.DARK_GREEN + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                        }
                        plugin_instance.setChatMuted(false);

                        sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.GREEN + " You have unmuted the chat.");
                  } else {
                        for (Player player : plugin_instance.getServer().getOnlinePlayers()) {
                              player.sendMessage(ChatColor.RED + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                              player.sendMessage(ChatColor.GREEN + "The chat has been globally muted by " + ChatColor.YELLOW + (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName()) + ChatColor.GREEN + ".");
                              player.sendMessage(ChatColor.RED + "♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪♫♪♫♪♫♪♫♪♫♪♫♫♪♫♪");
                        }
                        plugin_instance.setChatMuted(true);

                        sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.RED + " You have globally muted the chat.");
                  }
                  return true;
            }

            if (args.length == 1 && args[0].toLowerCase().startsWith("reload")) {
                  plugin_instance.reload();
                  sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.GREEN + " You have successfuly reloaded the configuration.");
                  return true;
            }

            sender.sendMessage(plugin_instance.getCurrentConfiguration().getMessage_prefix() + ChatColor.YELLOW + " Command usage: /chat <clear|toggle|reload>");
            return true;
      }

}
