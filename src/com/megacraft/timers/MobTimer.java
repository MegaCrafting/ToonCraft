package com.megacraft.timers;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.utilities.MobSpawner;

public class MobTimer implements Runnable {
	ToonCraft plugin;
	public World toonWorld;
	public String regionName;
	private static List<LivingEntity> spawnedMobs = new ArrayList<LivingEntity>();
	private static List<Block> pathBlocks = new ArrayList<Block>();
	private static Block startPt;  //starting point of the path.
	private static Block endPt;  //end point of path where mobs should despawn.
	private static BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,BlockFace.WEST};
	
	public boolean brokenConfig = false;
	
	
	
	public MobTimer(ToonCraft toonCraft) {
		this.plugin = toonCraft;
		String worldName = ToonCraft.plugin.getConfig().getString("Settings.ToonWorld");
		String startPoint = ToonCraft.plugin.getConfig().getString("Settings.StartPoint");
		
		if(startPoint == null || worldName == null || worldName.equals("") || startPoint.equals("")) {
			System.out.println("Warning could not locate a path start or the toon world!  Check Config!!");
			ToonCraft.tasks.get(0).cancel();
		}
		this.toonWorld = toonCraft.getServer().getWorld(worldName);
		if(this.toonWorld == null)
			ToonCraft.tasks.get(0).cancel();  //need to change this to a hashmap or something else (0) may not always be this task unless we never add one to the list before it.  The main tasks's list can be used to shut down all tasks.
		
		String coords[] = startPoint.split(",");
		this.startPt = this.toonWorld.getBlockAt(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		
	}

	@Override
	public void run() {
		
		if(brokenConfig)
			return;

		if(ToonCraft.interactables.isEmpty()) {
			if(pathBlocks == null || pathBlocks.isEmpty())
			{
				
				pathBlocks.add(startPt);
				
				boolean ended = false;
				Block currentBlock = startPt;
				int pathLen = 0;
				while(!ended)
				{
					
					if(pathLen >= 3000)
						ended = true;
					
					Boolean nextFound = false;
					
					for(BlockFace f:faces) 
					{
						Block b = currentBlock.getRelative(f);
							if(pathBlocks.contains(b))
								continue;
						
						if(b.getType() == Material.BEDROCK)
						{
							currentBlock = b;
							b.setType(Material.DIAMOND_BLOCK);
							pathBlocks.add(currentBlock);
							nextFound = true;
							break;
						}
					}
					pathLen++;
					if(!nextFound) {
						ended = true;
						break;
					}
						
				}
				//System.out.println("Path Size: " + pathBlocks.size() + " len: " + pathLen);
			}
		
			if(pathBlocks.size() > 0) {
				int randSpawn = ThreadLocalRandom.current().nextInt(0,pathBlocks.size());
				Location loc = pathBlocks.get(randSpawn).getLocation().add(0, 3, 0);
				MobSpawner.FlunkySpawn(loc);
			} else {
				System.out.println("ToonCraft: Unable to find a valid path to spawn monsters on!");
			}
		}
		
		
		
	}

}
