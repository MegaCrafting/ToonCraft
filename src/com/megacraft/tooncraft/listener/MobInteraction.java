package com.megacraft.tooncraft.listener;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.cogs.Cog;
import com.megacraft.tooncraft.gagSkills.SquirtAttack;
import com.megacraft.tooncraft.timers.BattleData;
import com.megacraft.tooncraft.utilities.InventoryGUIs;



public class MobInteraction implements Listener {	
	public ToonCraft plugin;
	


	public MobInteraction(ToonCraft plugin) {
		this.plugin = plugin;
	}

	/* is onPlayerMove the best for detecting if a player is good to go for combat?   On one hand yes maybe it should
	 * be this way, that way if they go AFK and a mob walks up on them they wont trigger a fight until they come back
	 * and actually make a movement.   If we put this on a timer, then monsters could initiae a fight, as it is now if
	 * a mob walks up to you and you don't wish to fight it then simply stand still and it will just stand there looking
	 * at you.   Whilst another player could in theory walk up and fight it..  IDK think about it.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) { 
		Player player = event.getPlayer();
		
		if(ToonCraft.battleList.containsKey(player))  {  //Player is in a battle prevent them from moving.
			/*
			 * adjusted anti movement code to allow ~120 degrees of view left/right and ~45 degrees up or down.    Cancels move
			 * event if this threshold is passed instead of snapping them back to the straight ahead position.
			 */
			
			if(!TCPlayer.findTCP(player).isMoveAllowed())
				event.setCancelled(true);
			
			
			return;  //currently this should prevent other mobs from joining the fight.
			
		} else {   //player isn't in combat check for nearby cogs.
			Cog cog = getNearbyInteractable(player.getLocation(), 2);
			
			
			if(cog != null)
				initiateCombat(player, cog);  //found a cog, lets start/join the fight!
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		
		Player p = event.getPlayer();
		if(p.getName().equalsIgnoreCase("dNiym"))
		{
			p.sendMessage("Checking fight data");
			p.sendMessage("Battle Data Size: " + ToonCraft.battleList.size() + " Cog List Size: " + ToonCraft.cogList.size());
			BattleData bd = ToonCraft.battleList.get(p);
			if(bd == null) {
				p.sendMessage("You are not currently in a battle");
			}else{
				p.sendMessage("You are fighting with: " + ((bd.getInvolvedPlayers().size())-1) + " other players against: " + bd.getInvolvedCogs().size() + " cogs");
				String players = "";
				String cogs = "";
				
				for(TCPlayer tcp:bd.getInvolvedPlayers())
					players = players + tcp.getPlayer().getName() + ", ";
				for(Cog c:bd.getInvolvedCogs())
					cogs = cogs + c.getEntity().getCustomName() + ", ";
				
				p.sendMessage("Players: " + players + " / Cogs: " + cogs);
				
				p.sendMessage("Currently on turn#: " + bd.getTurnCounter() + " @ delay: " + bd.getTurnDelay());
				p.sendMessage("Current Player: " + bd.getCurrentPlayer().getPlayer().getName() + " has attacked? " + bd.isPlayerAttackFinished());
				p.sendMessage("Current cog: " + bd.getCurrentCog().getEntity().getCustomName() );
			}
		}
		/*
		if(CitizensAPI.getNPCRegistry() != null)
			for(NPC npc:CitizensAPI.getNPCRegistry())
				if(Cog.findCog((LivingEntity) npc.getEntity()) != null)
					continue;
				else {
					npc.destroy();
				}
		*/
		if(!ToonCraft.holomanager.getActiveHolograms().isEmpty())
			for(String h:ToonCraft.holomanager.getActiveHolograms().keySet())
				ToonCraft.holomanager.deleteHologram(ToonCraft.holomanager.getActiveHolograms().get(h));
				
		
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityIgnigte(EntityCombustEvent event) {
		
		if(event.getEntity() instanceof LivingEntity)
			if(Cog.findCog((LivingEntity) event.getEntity()) != null)
				event.setCancelled(true);  //dont let our cogs burn up
	}
	
	
	public void initiateCombat(Player player, Cog cog)
	{
		
		
			
			BattleData bd = ToonCraft.cogList.get(cog);  //check to see if cog has battle data set yet, if so they're in combat already.
			Boolean joining = false;
			
			if(bd != null) { 
				//this cog was already involved in a battle lets join in!
				ToonCraft.battleList.put(player, bd);
				player.sendMessage(ChatColor.GREEN + "You have joined the battle against " + cog.getEntity().getCustomName());
				for(TCPlayer tcp:bd.getInvolvedPlayers())
					tcp.getPlayer().sendMessage(ChatColor.AQUA + player.getName() + "has joined your fight!");
				joining = true;
				//do whatever to lock down add on players so they cant move.
			} else {
				player.sendMessage("You are near a " + ChatColor.stripColor(cog.getEntity().getCustomName()));
				bd = BattleData.findBD(player);   //they had no battle data saved, lets create a new battle.
				ToonCraft.cogList.put(cog, bd);  //now they have battle data
				
				//ToonCraft.interactables.remove(cog); replaced with the above.
			}
			TCPlayer tcp = TCPlayer.findTCP(player);   //get the players data.
			
			if(!bd.getInvolvedCogs().contains(cog)) 
				bd.getInvolvedCogs().add(cog);   //add the cog to this battle.  should be maybe for future when mobs can gang up on players.
			
			
			if(!bd.getInvolvedPlayers().contains(player))
				bd.getInvolvedPlayers().add(tcp);
			else
				return;   //we were already in this fight... don't try to re-add us.
			
			
				player.sendMessage("You have engaged a " + cog.getEntity().getCustomName());
				
				if(!joining) {
					if(!cog.isTrapped())	
						if(!cog.trapCog(player)) { 	
							System.out.println("Failed to trap cog..  abort?");
							bd.clearVariables();
						} else {
							Location pLoc = player.getLocation();
							pLoc.setPitch(0);
							tcp.setLockedLocation(pLoc);
							tcp.setTarget(cog);
						
							bd.setTurnCounter(0);
							bd.setTurnDelay(System.currentTimeMillis() + 5000);
							player.sendMessage(cog.getType().getName() + " says, " + cog.getTaunt());
						}
				} else {
					if(!cog.trapNewPlayer(player,bd))
						System.out.println("Failed to find a location to place player joining battle!: " + player.getName());
					
					//another player joined the battle, dont teleport the mob teleport the player!
				}
					
						
					
			/*
			 * dNiym 
			 * improved mob detection/teleport routine (i think so anyway)
			 * routine leaves player in whatever direction they were facing then records this location.
			 * mob is teleported 5 blocks ahead of the player as long as the player can still see the cog.  
			 * If there is no line of sight, the location is rotated and tested again until a valid location is found.
			 * 
			 *  Additionally this code also spawns the blocks around the mob, it saves them to a map rather than just
			 *  mathing their positions which might prove un-reliable later, when the mob dies or combat ends the blocks
			 *  are replaced.
			 */
			
				

			
			
	}

	/*
	 * Temp Code just for testing effects.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSwing(PlayerAnimationEvent event) {

		Player p = event.getPlayer();

		int  slot = p.getInventory().getHeldItemSlot();

		if(slot == 0)
		{
			/*
			if(ToonCraft.playerFightMode.contains(p)) //we are in fact in combat
			{
				BattleData ld = BattleData.findLD(TCPlayer.findTCP(p));
				new SquirtAttack(ld);
			}
			p.sendMessage("Running test effect 1");
*/

		}


	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {

		
		if((event.getEntity() instanceof Player)) {
			
			Player p = (Player) event.getEntity();
			if(ToonCraft.battleList.containsKey(p))
				return;
			
			Cog cog = Cog.findCog(event.getEntity());
			
			
			if(event.getEntity().getKiller() == null)
			{
				if(cog == null)
					return;  //wasnt a cog that died
				
				if(ToonCraft.cogList.get(cog) != null)
					ToonCraft.cogList.get(cog).endBattle(cog);  //died from something other than a player killing it in battle..
				else
					cog.remove();  //otherwise it must have died of natural causes.
				return;

			}
			
			Player player = event.getEntity().getKiller();
			
// This needs to be redone not sure its working at all probably move it to clearvariables to handle all players fighting this cog..
			if(!InventoryGUIs.inventoryContents.containsKey(player)) {
				return;
			} else {
				for(ItemStack is:InventoryGUIs.inventoryContents.get(player)) {
					if(is != null) {
						player.getInventory().addItem(is);
					}
				}
			}

			InventoryGUIs.inventoryContents.remove(player);

			mobBlockRemoval(cog, player);
			BattleData ld = ToonCraft.battleList.get(player);
			
			//pass the dead cog back to BattleData then award xp for everyone.
			if(ld.endBattle(cog))  
			{
				//at this point, all players have been removed from the BattleData as well as the dead cog.
				//player movement should be restored.
				//so the battle will have totally been ended at this point.
				
			}
			System.out.println("battle over destroying cog.");
			CitizensAPI.getNPCRegistry().getNPC(event.getEntity()).destroy();	
			//need to make the players switch up their targets to others in the battle now.
			
			
		}
	}



	public static void mobBlockRemoval(Cog cog, Player player) {
		
		if(ToonCraft.cogList.containsKey(cog)) 
			cog.remove();

	}

	public Cog getNearbyInteractable(Location plrLoc, Integer range) {

		Cog alreadyFighting = null;  //this variable will be used to give prefernce to cogs that are NOT already fighing.  
									// if 2 cogs are near a player, and another player walks up we will set combat on a cog NOT already fighing
		 							// rather than jumping in and assisting.
		
		for(Cog c:ToonCraft.cogList.keySet())
		{
			if(c.getEntity() == null)  //entity hasn't been set yet because the cog isn't offically spawned.
				continue;
			
			if(!c.getEntity().getWorld().equals(plrLoc.getWorld())) //not in the same world skip it.
				continue;
			
			if(c.getEntity().getLocation().distanceSquared(plrLoc) <= range)  //found a cog within range
				if(ToonCraft.cogList.get(c) != null)
					alreadyFighting = c;   //this cog already is in battle, look for another one first.
				else
					return c;  //found a cog, who wasn't fighting anyone.
			
			
		}

		 
		return alreadyFighting;  //will return null unless there was a mob nearby that was already fighting another player.
	}	
}
