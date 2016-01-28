package com.megacraft.tooncraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class TCCommand {

	private static String name;

	private static String properUse;

	private static String description;

	private final String[] aliases;

	public static Map<String, TCCommand> MBinstances = new HashMap<String, TCCommand>();

	public TCCommand(String name, String properUse, String description, String[] aliases) {
		TCCommand.name = name;
		TCCommand.properUse = properUse;
		TCCommand.description = description;
		this.aliases = aliases;
		MBinstances.put(name, this);
	}

	public static String getName() {
		return name;
	}

	public String getProperUse() {
		return properUse;
	}

	public String getDescription() {
		return description;
	}

	public String[] getAliases() {
		return aliases;
	}

	public static void help(Player sender, boolean description, String arg) {
		sender.sendMessage(ChatColor.GOLD + "Proper Usage: " + ChatColor.DARK_AQUA + properUse);
		if (description) {
			sender.sendMessage(ChatColor.YELLOW + TCCommand.description);
		}
	}

	protected static boolean hasPermission(Player sender) {
		if (sender.hasPermission("bending.command." + name )) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			sender.sendMessage("Required Permission: bending.command." + name);
			return false;
		}
	}

	protected static boolean hasPermission(Player sender, String extra) {
		if (sender.hasPermission("bending.command." + name + "." + extra)) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			return false;
		}
	}

	protected static boolean correctLength(Player sender, int size, int min, int max) {
		if (size < min || size > max) {
			TCCommand.help(sender, false, null);
			return false;
		} else {
			return true;
		}
	}

	protected static boolean isPlayer(Player sender) {
		if (sender instanceof Player) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
			return false;
		}
	}
}
