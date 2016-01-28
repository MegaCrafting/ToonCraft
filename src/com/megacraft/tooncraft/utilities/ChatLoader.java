package com.megacraft.tooncraft.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.megacraft.tooncraft.ToonCraft;

public class ChatLoader {	
	static FileConfiguration config = ToonCraft.plugin.getConfig();
	
	public static void TutorialChat(Player player) {
		player.setDisplayName("Toon");
		player.sendMessage("Your name has been submitted for review, you will appear as Toon until your name has been accepted by staff.");
		
		int x = config.getInt("ToonCraft.locations.Tutorial.x");
		int y = config.getInt("ToonCraft.locations.Tutorial.y");
		int z = config.getInt("ToonCraft.locations.Tutorial.z");
		World world = Bukkit.getWorld(config.getString("ToonCraft.locations.Tutorial.World"));
		
		Location location = new Location(world, x, y, z);
		player.teleport(location);
		
		player.sendMessage("There are evil mobs taking over mega craft, and you have to stop them!");
		player.sendMessage("Quickly! There is a mob outside! Please help us by killing it!");
		
		MobSpawner.CreeperSpawner(player);
	}
}
