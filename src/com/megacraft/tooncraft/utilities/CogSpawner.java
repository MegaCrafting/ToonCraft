package com.megacraft.tooncraft.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import com.megacraft.tooncraft.ToonCraft;

import net.md_5.bungee.api.ChatColor;

public class CogSpawner {	
	public static void CreeperSpawner(Player player, int level) {
		Location location = new Location(player.getWorld(), player.getLocation().getX()+4, player.getLocation().getY(), player.getLocation().getZ()-10);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);
		
		double x = ((level+1)*(level+2))*2;
		
		creeper.setCustomName(ChatColor.RED + "Creeper");
		creeper.setCustomNameVisible(true);
		creeper.setHealth(x);
		
		ToonCraft.interactables.add(creeper);
	}
	
	
	
	public static void spawnFlunky(int level) {
		Location location = new Location(, -77, 79, 114);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);
		
		double x = ((level+1)*(level+2))*2;
		
		creeper.setCustomName(ChatColor.RED + "Flunky Level " + level);
		creeper.setCustomNameVisible(true);
		creeper.setHealth(x);
		
		ToonCraft.interactables.add(creeper);
	}
}
