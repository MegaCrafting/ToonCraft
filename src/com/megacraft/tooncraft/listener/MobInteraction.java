package com.megacraft.tooncraft.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.megacraft.menuapi.page.Page;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.gagSkills.SquirtAttack;
import com.megacraft.tooncraft.timers.LocationData;
import com.megacraft.tooncraft.utilities.InventoryGUIs;



public class MobInteraction implements Listener {	
	public ToonCraft plugin;
	public static List<LocationData> playerLoc = new ArrayList<LocationData>();
	
	
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
		Location plrLoc = player.getLocation();
		LivingEntity mob = getNearbyInteractable(plrLoc, 2);
		
		if(ToonCraft.playerFightMode.contains(player)) {
			LocationData ld = LocationData.findLD(player);  //create / update new location data to track player movement
			if(!ld.moveAllowed())
				event.setCancelled(true);
				//player.teleport(ld.getLockedLoc());
		//	Location plrLoc1 = new Location(player.getWorld(), Math.floor(player.getLocation().getX())+0.5, player.getLocation().getBlockY(), Math.floor(player.getLocation().getZ())+0.5, -90, 0);
		//	player.teleport(plrLoc1);
		}
		
		if(mob == null){
			
		} else {
			if(!ToonCraft.playerFightMode.isEmpty() && ToonCraft.playerFightMode.contains(player))  //dont let mobs come up and bother the player whilst he / she is fighting.
				return;
			
			player.sendMessage("You are near a " + ChatColor.stripColor(mob.getCustomName()));
			ToonCraft.interactables.remove(mob);
			//ToonCraft.mobFightMode.add(mob);
			
			List<Player> fighting = new ArrayList<Player>(); //list of players fighting this monster.
			if(ToonCraft.mobFightMode.isEmpty() || !ToonCraft.mobFightMode.containsKey(mob))  //mob isn't fighting anyone at the moment!
			{
				player.sendMessage("initiating combat with: " + mob.getName());
				fighting.add(player);
				ToonCraft.mobFightMode.put(mob, fighting);  //add the first player
			} else {  //mob is already fighting someone... let another player join!
				fighting = ToonCraft.mobFightMode.get(mob);
				if(!fighting.contains(player)) //check to see if this player is already fighting this mob, if so do nothing.
					fighting.add(player);
				
				player.sendMessage("You have joined a fellow toon in battle!");
			}
					
			/*
			 * dNiym 
			 * improved mob detection/teleport routine (i think so anyway)
			 * routine leaves player in whatever direction they were facing then records this location.
			 * mob is teleported 5 blocks ahead of the player no matter what (may end up with mobs in walls if unchecked)
			 * adjusted anti movement code to allow ~120 degrees of view left/right and ~45 degrees up or down.    Cancels move
			 * event if this threshold is passed instead of snapping them back to the straight ahead position.
			 */
			//Location mobLoc = new Location(mob.getWorld(), Math.floor(mob.getLocation().getX())+x, mob.getLocation().getBlockY(), Math.floor(mob.getLocation().getZ())+z, 90, 0);
			Vector dir = player.getLocation().getDirection();
	        Block mobBlock = player.getLocation().add(dir.add(dir.clone().normalize().multiply(6)).toLocation(player.getWorld())).getBlock();
	        Location mobLoc = mobBlock.getLocation().subtract(.5,0,.5);
	        mobLoc.setY(player.getLocation().getY());
	        
	        
			mob.teleport(mobLoc);
			
			Location plrLoc1 = new Location(mob.getWorld(), Math.floor(mob.getLocation().getX())-5.5, mob.getLocation().getBlockY(), Math.floor(mob.getLocation().getZ())+0.5, -90, 0);
			//Location plrLoc1 = new Location(mob.getWorld(), Math.floor(mob.getLocation().getX())-x, mob.getLocation().getBlockY(), Math.floor(mob.getLocation().getZ())-z, -90, 0);
		//	player.teleport(plrLoc1);
		    ToonCraft.playerFightMode.add(player);
		  
		    	LocationData ld = LocationData.findLD(player);
		    	ld.setTarget(mob);
		    	
		    	playerLoc.add(ld);
		    	
			
		    	System.out.println(ToonCraft.mobFightMode.size());
		    	/*
		    	 * Small issue with this code is signs or torches or grass, anything that isn't air but does not
		    	 * inhibit movement will still let the monster walk right through it!
		    	 * we need to update this to take into account intransigent blocks.
		    	 */
			if(mob.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR) {
				mob.getLocation().add(0, 2, 0).getBlock().setType(Material.STONE);
			}
			if(mob.getLocation().add(1, 0, 0).getBlock().getType() == Material.AIR) {
				mob.getLocation().add(1, 0, 0).getBlock().setType(Material.STONE);
			}
			if(mob.getLocation().add(0, 0, 1).getBlock().getType() == Material.AIR) {
				mob.getLocation().add(0, 0, 1).getBlock().setType(Material.STONE);
			}
			if(mob.getLocation().subtract(1, 0, 0).getBlock().getType() == Material.AIR) {
				mob.getLocation().subtract(1, 0, 0).getBlock().setType(Material.STONE);
			}
			if(mob.getLocation().subtract(0, 0, 1).getBlock().getType() == Material.AIR) {
				mob.getLocation().subtract(0, 0, 1).getBlock().setType(Material.STONE);
			}
			
			//InventoryGUIs.GagsInventory(player);  //now handled in locationdata.
			
		}		
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
			if(ToonCraft.playerFightMode.contains(p)) //we are in fact in combat
			{
				LocationData ld = LocationData.findLD(p);
				new SquirtAttack(ld);
			}
			p.sendMessage("Running test effect 1");
			
			 
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity mob = event.getEntity();
		Player player = event.getEntity().getKiller();
		
		if(!InventoryGUIs.inventoryContents.containsKey(player)) {
			return;
		} else {
			for(ItemStack is:InventoryGUIs.inventoryContents.get(player)) {
				if(is != null) {
					player.getInventory().addItem(is);
				}
			}
		}
		/*for(int i = 0; i <= inv.length; i++) {
			if(inv[i] != null) {
				player.getInventory().addItem(inv[i]);
			}
		}*/
		InventoryGUIs.inventoryContents.remove(player);
		LocationData ld = LocationData.findLD(player);
		ld.setLockedLoc(null);
		ld.setTarget(null);
		
		mobBlockRemoval(mob, player);
	}

	

	public static void mobBlockRemoval(LivingEntity mob, Player player) {   
		if(ToonCraft.mobFightMode.containsKey(mob)) {
			
			
			for(Player p:ToonCraft.mobFightMode.get(mob))  //loop through the players that may have been fighting this mob and free them up
				if(p != null && ToonCraft.playerFightMode.contains(p))
					ToonCraft.playerFightMode.remove(p);
			
			
			
			
			//if(player != null) 
				//ToonCraft.playerFightMode.remove(player);
			
						
			if(mob.getLocation().add(0, 2, 0).getBlock().getType() == Material.STONE) {
				mob.getLocation().add(0, 2, 0).getBlock().setType(Material.AIR);
			}
			if(mob.getLocation().add(1, 0, 0).getBlock().getType() == Material.STONE) {
				mob.getLocation().add(1, 0, 0).getBlock().setType(Material.AIR);
			}
			if(mob.getLocation().add(0, 0, 1).getBlock().getType() == Material.STONE) {
				mob.getLocation().add(0, 0, 1).getBlock().setType(Material.AIR);
			}
			if(mob.getLocation().subtract(1, 0, 0).getBlock().getType() == Material.STONE) {
				mob.getLocation().subtract(1, 0, 0).getBlock().setType(Material.AIR);
			}
			if(mob.getLocation().subtract(0, 0, 1).getBlock().getType() == Material.STONE) {
				mob.getLocation().subtract(0, 0, 1).getBlock().setType(Material.AIR);
			}
			
			ToonCraft.mobFightMode.remove(mob);  //then remove the mob from the hashmap.
		}
	}
	
	public LivingEntity getNearbyInteractable(Location plrLoc, Integer range) {
        for(LivingEntity e:ToonCraft.interactables){
        	if(!e.getWorld().equals(plrLoc.getWorld()))  //skip this monster as its not in the same world as the player.
        		continue;
        	if(e.getLocation().distanceSquared(plrLoc) <= range)  //note the distanceSquared() function is a little faster than .distance()
        		return e;  //return the first mob we find within range.
        }
        return null;
	}	
}
