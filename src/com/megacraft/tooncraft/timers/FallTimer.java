package com.megacraft.tooncraft.timers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;


import org.bukkit.entity.Player;

import com.megacraft.tooncraft.ToonCraft;

public class FallTimer implements Runnable {

	ToonCraft plugin;
	public FallTimer(ToonCraft toonCraft) {
		this.plugin = toonCraft;
	}

	@Override
	public void run() {
		
		if(!ToonCraft.interactables.isEmpty())
		{
			for(LivingEntity e:ToonCraft.interactables)
			{
				Block b = e.getLocation().getBlock();
				if(b.getType() == Material.AIR)  //cog is in air
					e.setVelocity(e.getVelocity().setY(0.02));
				e.setFallDistance(0);
				e.setFireTicks(0);
			}
		}

		/*
		 * need to be able to manage effects, like for particle projections etc  do stuff here with the main list of effects
		
		*/
	}


	
}
