/*Copyright (C) Harry5573 2013-14

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package com.harry5573.simplechat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class CommandListener implements CommandExecutor {

    public static SimpleChat plugin;

    public CommandListener(SimpleChat instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (p == null) {
            plugin.log("That is not a console command!");
            return true;
        }
        
        if (!p.hasPermission("simplechat.admin")) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " Permission Denied");
            return true;
        }
        
        if (args.length != 1) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " Usage: /simplechat <clear|toggle|reload>");
            return true;
        }  
        
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (int i = 0; i < 75; i++) {
                    Bukkit.broadcastMessage(" ");
                }

                Bukkit.broadcastMessage(ChatColor.DARK_RED + "!!! " + ChatColor.DARK_AQUA + "Chat cleared by " + ChatColor.DARK_GREEN + p.getName() + ChatColor.DARK_RED + " !!!");
                p.sendMessage(plugin.prefix + ChatColor.GREEN + " Chat cleared!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("toggle")) {
                plugin.handleChat(p);
                p.sendMessage(plugin.prefix + ChatColor.GREEN + " Chat toggled!");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.loadPrefix();
                p.sendMessage(plugin.prefix + ChatColor.GREEN + " Config reloaded!");
                return true;
            }
            
            p.sendMessage(plugin.prefix + ChatColor.RED + " Usage: /simplechat <clear|toggle|reload>");
            return true;
        }
        return false;
    }
}
