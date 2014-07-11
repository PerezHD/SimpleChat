package com.harry5573.chat.command;

import com.harry5573.chat.SimpleChatPlugin;
import com.harry5573.chat.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class CommandChat implements CommandExecutor {

    static SimpleChatPlugin plugin = SimpleChatPlugin.get();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.logMessage("That is not a console command!");
            return true;
        }

        if (!player.hasPermission("simplechat.admin")) {
            player.sendMessage(plugin.prefix + ChatColor.RED + " Permission Denied");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(plugin.prefix + ChatColor.RED + " Usage: /chat <clear|toggle|reload>");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                ChatUtils.clearChat(player);
                return true;
            }

            if (args[0].equalsIgnoreCase("toggle")) {
                ChatUtils.toggleChat(player);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                player.sendMessage(plugin.prefix + ChatColor.GREEN + " You have reloaded the configuration.");
                return true;
            }

            player.sendMessage(plugin.prefix + ChatColor.RED + " Usage: /simplechat <clear|toggle|reload>");
            return true;
        }
        return false;
    }
}
