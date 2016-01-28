package com.megacraft.tooncraft.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import com.megacraft.tooncraft.ToonCraft;

import net.md_5.bungee.api.ChatColor;

public class MobSpawner {	
	public static void CreeperSpawner(Player player) {
		Location location = new Location(player.getWorld(), player.getLocation().getX()+4, player.getLocation().getY(), player.getLocation().getZ()-10);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);
		
		creeper.setCustomName(ChatColor.RED + "Creeper");
		creeper.setCustomNameVisible(true);
		
		ToonCraft.interactables.add(creeper);
	}
}
