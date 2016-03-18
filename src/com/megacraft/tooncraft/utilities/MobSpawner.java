package com.megacraft.tooncraft.utilities;

import io.netty.util.internal.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import com.megacraft.tooncraft.ToonCraft;

import net.md_5.bungee.api.ChatColor;

public class MobSpawner {
	private static int getHealth(int level)
	{
		switch(level)
		{
		case 1:
			return 6;
		case 2:
			return 12;
		case 3: 
			return 20;
		case 4:
			return 30;
		case 5:
			return 42;
		}
		return 6;
	}
	
	public static int getCogLevel(int maxHealth) {
		
		switch(maxHealth)
		{
		case 6:
			return 1;
		case 12:
			return 2;
		case 30:
			return 3;
		case 42:
			return 4;
		}
		
		return 0;
		
	}
	public static void FlunkySpawn(Location loc) {
		
		Zombie flunky = (Zombie) loc.getWorld().spawn(loc.add(0,7,0), Zombie.class);
		flunky.setBaby(false);
		flunky.setCustomNameVisible(true);
		flunky.setVelocity(flunky.getVelocity().setY(1.2));
		int lvl = ThreadLocalRandom.current().nextInt(1,5);
		
		flunky.setMaxHealth(getHealth(lvl));
		flunky.setHealth(flunky.getMaxHealth());
		flunky.setCustomName(ChatColor.RED + "Flunky - Lvl " + lvl + "(" +  flunky.getHealth() + ")"  );
		ToonCraft.interactables.add(flunky);
		
		
		
		
		
		
	}
	public static void CreeperSpawner(Player player) {
		Location location = new Location(player.getWorld(), player.getLocation().getX()+4, player.getLocation().getY(), player.getLocation().getZ()-10);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);
		
		creeper.setCustomName(ChatColor.RED + "Creeper");
		creeper.setCustomNameVisible(true);
		
		ToonCraft.interactables.add(creeper);
	}
}
