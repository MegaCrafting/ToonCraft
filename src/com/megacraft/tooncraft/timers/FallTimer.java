package com.megacraft.tooncraft.timers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;


import org.bukkit.entity.Player;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.cogs.Cog;

public class FallTimer implements Runnable {

	ToonCraft plugin;
	public FallTimer(ToonCraft toonCraft) {
		this.plugin = toonCraft;
	}

	@Override
	public void run() {
		
		if(!ToonCraft.cogList.isEmpty())
		{
			for(Cog e:ToonCraft.cogList.keySet())
			{
				if(ToonCraft.cogList.get(e) != null) //if they have battle data, they can't be falling skip this check.
					continue;
			
				if(e.getNpc().isSpawned()) {
					LivingEntity p = (LivingEntity) e.getNpc().getEntity();
					
					Block b = p.getLocation().getBlock();
					if(b.getType() == Material.AIR)  //cog is in air
						p.setVelocity(p.getVelocity().setY(0.002));
					p.setFallDistance(0);
					p.setFireTicks(0);
				}
			}
		}

		/*
		 * need to be able to manage effects, like for particle projections etc  do stuff here with the main list of effects
		
		*/
	}


	
}
