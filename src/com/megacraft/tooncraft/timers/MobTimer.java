package com.megacraft.tooncraft.timers;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.tree.Behavior;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.waypoint.Waypoint;
import net.citizensnpcs.trait.waypoint.WaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.trait.waypoint.triggers.WaypointTrigger;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.cogs.Cog;
import com.megacraft.tooncraft.utilities.MobSpawner;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;

public class MobTimer implements Runnable {
	ToonCraft plugin;
	public World toonWorld;
	public String regionName;
	private static List<LivingEntity> spawnedMobs = new ArrayList<LivingEntity>();
	private static List<Block> pathBlocks = new ArrayList<Block>();
	private static Block startPt;  //starting point of the path.
	private static Block endPt;  //end point of path where mobs should despawn.
	private static BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,BlockFace.WEST};
	private boolean debug = true;
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


		if(ToonCraft.battleList.keySet().size() > 0)
		{

			for(Player p:ToonCraft.battleList.keySet()) {
				if(this.debug) {
					System.out.println("Progressing fight for: " + p);
					this.debug = false;
				}
				ToonCraft.battleList.get(p).progressFight();
			}

		}




		if(!ToonCraft.cogList.isEmpty()) {
			List<Cog> remove = new ArrayList<Cog>();

			for(Cog c:ToonCraft.cogList.keySet()) {

				if(c.getEntity() == null)
					if(c.getNpc() != null && c.getNpc().isSpawned())
						c.setEntity((LivingEntity) c.getNpc().getEntity());
				
				
				if(c.getXpHolo() != null && System.currentTimeMillis() > c.getXpHoloDelay())
				{
					c.setXpHoloDelay(System.currentTimeMillis() + 1000);
					c.getXpHolo().teleport(c.getXpHolo().getLocation().add(0,2.5,0));
					if(c.getXpHolo().getLocation().distance(c.getEntity().getLocation())> 5) {
						ToonCraft.holomanager.deleteHologram(c.getXpHolo());
						c.setXpHolo(null); 
					}
						
				}
				
				if(c.isDead())
					remove.add(c);
					
				
			/*				
				if(c.getEntity() == null || c.getEntity().isDead())
				{
					System.out.println("Removed one dead cog");

					BattleData bd = ToonCraft.cogList.get(c);
					if(bd != null)
						bd.endBattle(c);
					remove.add(c);
					
					if(c.getNpc() != null) {
						CitizensAPI.getNPCRegistry().deregister(c.getNpc());
						System.out.println("removing the NPC entry from registry.");
					}

				} else {
				*/
					if (c.getEntity().isOnGround() && !c.getNpc().getNavigator().isNavigating()) 
						if(c.getHolo() == null ) {
							String name = c.getType().getName() + ToonCraft.cogList.size() + 1;
							Hologram h = new Hologram(name,c.getEntity().getLocation().add(0,2.5,0) );
							name = c.getType().getName() + " - Level " + c.getLevel();
							HologramLine hl = new TextLine(h, name);
							h.setPersistent(false);
							h.addLine(hl);
							ToonCraft.holomanager.addActiveHologram(h);
							c.setHolo(h);
						} else {
							c.updateHologram();
						}
					if(System.currentTimeMillis() < c.getMoveDelay() || !c.getEntity().isOnGround())
						continue;


					BattleData bd = ToonCraft.cogList.get(c);
					if(bd == null) {
						//NPC npc = CitizensAPI.getNPCRegistry().getNPC(c.getEntity());
						NPC npc = c.getNpc();
						
						int nextPoint = c.getPathIndex() +3;
						if(nextPoint < c.getMaxPathIndex())  //on the path lets continue.
						{	
							//System.out.println("Cog Moving next point:");
							if(!npc.getNavigator().isNavigating() && npc.getNavigator().getTargetAsLocation() == null)
							{ 
								Location loc = pathBlocks.get(nextPoint).getLocation().add(0, 3, 0);
								npc.getNavigator().setTarget(loc);
								npc.getNavigator().setPaused(false);
								loc.getWorld().playEffect(loc.add(0,1,0), Effect.CRIT, 1);
								c.setPathIndex(nextPoint);
								//System.out.println("Unpaused cog, should be navigating.:");

							}
							c.setMoveDelay(System.currentTimeMillis()+5000);
						} else {
							System.out.println("Cog has reached end of line.");
							ToonCraft.holomanager.deleteHologram(c.getHolo());
							CitizensAPI.getNPCRegistry().deregister(npc);
							npc.destroy();

						}

					}

				}


			

			if(!remove.isEmpty())
				for(Cog c:remove)
					ToonCraft.cogList.remove(c);
		}

		
		/*
		 * redo this as we create more maps and shit..  Right now it just makes one place to auto spawn mobs.
		 */
		if(ToonCraft.cogList.isEmpty() || ToonCraft.cogList.size() < ToonCraft.tcPlayers.size() + 2) {  
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

				for(Player p:ToonCraft.battleList.keySet())
					if(ToonCraft.battleList.get(p) != null)
						if(p.getLocation().distanceSquared(loc) < 8) 
							return;   //dont spawn a new mob right on top of a player in battle.

				//	MobSpawner.VerifyCogs();

				MobSpawner.FlunkySpawn(loc,randSpawn, pathBlocks.size());
			} else {
				System.out.println("ToonCraft: Unable to find a valid path to spawn monsters on!");
			}
		} 





	}

}
