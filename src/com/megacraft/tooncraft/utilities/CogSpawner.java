package com.megacraft.tooncraft.utilities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import com.megacraft.tooncraft.ToonCraft;

import net.md_5.bungee.api.ChatColor;

public class CogSpawner {	
	
	public static ToonCraft plugin;
	
	public CogSpawner(final ToonCraft plugin) {
		this.plugin = plugin;
	}
	
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
		World world = plugin.getServer().getWorld("ChandlersTest");
		Location location = new Location(world, -77, 79, 114);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);
		
		double x = ((level+1)*(level+2))*2;
		
		creeper.setCustomName(ChatColor.RED + "Flunky Level " + level);
		creeper.setCustomNameVisible(true);
		creeper.setHealth(x);
		
		ToonCraft.interactables.add(creeper);
	}
}
