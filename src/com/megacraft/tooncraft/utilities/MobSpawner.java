package com.megacraft.tooncraft.utilities;

import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.cogs.Cog;
import com.megacraft.tooncraft.enums.CogType;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
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
	public static Cog FlunkySpawn(Location loc, int PathIndex, int maxPathIndex) {

	     
	
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, CogType.FLUNKY.getName());
		
		
		
		npc.spawn(loc.add(0,7,0));

		if(npc.getEntity() == null)
			System.out.println("Entity is still null.");
		
		LivingEntity flunky = (LivingEntity) npc.getEntity();
		flunky.setVelocity(flunky.getVelocity().setY(1.2));
		int lvl = ThreadLocalRandom.current().nextInt(1,5);
		flunky.setMaxHealth(getHealth(lvl));
		flunky.setHealth(flunky.getMaxHealth());

		Cog cog = new Cog(npc, CogType.FLUNKY, lvl);

		cog.setPathIndex(PathIndex);
		cog.setMaxPathIndex(maxPathIndex);
		ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData());
		flunky.getEquipment().setHelmet(is);


		System.out.println("Spawned one new NPC count is now: " + npc.getId() + " Cog count is: " + ToonCraft.cogList.size());
		return cog;

	}
	public static void CreeperSpawner(Player player) {
		Location location = new Location(player.getWorld(), player.getLocation().getX()+4, player.getLocation().getY(), player.getLocation().getZ()-10);
		Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class);

		creeper.setCustomName(ChatColor.RED + "Creeper");
		creeper.setCustomNameVisible(true);

		//ToonCraft.interactables.add(creeper);
	}

	public static void VerifyCogs() {
		Iterable<NPCRegistry> reg = CitizensAPI.getNPCRegistries();
		if(reg == null)
			System.out.println("Registry was null?");
		if(reg != null)
			for(NPC npc:reg.iterator().next()) {
				boolean valid = false;
				for(Cog c: ToonCraft.cogList.keySet())
					if(c.getNpc() == npc) {
						valid = true;
						break;
					}

				if(!valid) {
					CitizensAPI.getNPCRegistry().deregister(npc);;
					System.out.println("Non valid NPC detected, removing");
				}
			}
	}

}
