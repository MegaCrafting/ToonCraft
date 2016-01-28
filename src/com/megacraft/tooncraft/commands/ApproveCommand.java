package com.megacraft.tooncraft.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.megacraft.tooncraft.storage.DBConnection;

import java.util.List;

public class ApproveCommand extends TCCommand {
	public ApproveCommand() {
		super("approve", "/tooncraft approve [list/approve/show] (name) (yes)", "This command provides information on the name that the player wants.", new String[] { "approve", "a" });
	}

	public static void execute(CommandSender sender, List<String> args) {
		Player player = (Player) sender;
		if (!correctLength(player, args.size(), 1, 2)) {
			return;
		} else if (args.size() == 1) {
			if(args.get(0).equalsIgnoreCase("list")) {
				list(player);
			} else if(args.get(0).equalsIgnoreCase("show")) {
				player.sendMessage("The show command requires a player name to use.");
			}
		} else {
			if(args.get(0).equalsIgnoreCase("list")) {
				player.sendMessage("Too many arguments!");
			} else if(args.get(0).equalsIgnoreCase("show")) {
				show(player, args.get(1));
			} else {
				approve(player, args.get(0), args.get(1));
			}
		}
	}

	private static void show(Player player, String string) {
		DBConnection.TCApproved(player, string, "show", null);
	}

	private static void approve(Player player, String name, String approved) {
		if(approved.equalsIgnoreCase("yes")) {
			player.sendMessage("You approved " + name + "'s Toon name");

			DBConnection.TCApproved(player, name, "approved", approved);
		} else if(approved.equalsIgnoreCase("no")) {
			player.sendMessage("You denied " + name + "'s Toon name");

			DBConnection.TCApproved(player, name, "approved", approved);
		} else {
			player.sendMessage("Please only put yes or no in for the final argument");
		}
	}

	private static void list(Player player) {
		DBConnection.TCApproved(player, null, "list", null);		
	}
	

}
