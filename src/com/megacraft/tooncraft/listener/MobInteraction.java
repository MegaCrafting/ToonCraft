package com.megacraft.tooncraft.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.megacraft.menuapi.page.Page;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.utilities.InventoryGUIs;

public class MobInteraction implements Listener {	
	public ToonCraft plugin;
	
	public MobInteraction(ToonCraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location plrLoc = player.getLocation();
		LivingEntity mob = getNearbyInteractable(plrLoc, 2);
		
		if(ToonCraft.playerFightMode.contains(player)) {
			Location plrLoc1 = new Location(player.getWorld(), Math.floor(player.getLocation().getX())+0.5, player.getLocation().getBlockY(), Math.floor(player.getLocation().getZ())+0.5, -90, 0);
			player.teleport(plrLoc1);
		}
		
		if(mob == null){
			
		} else {
			player.sendMessage("You are near a " + ChatColor.stripColor(mob.getCustomName()));
			ToonCraft.interactables.remove(mob);
			ToonCraft.mobFightMode.add(mob);
			Location mobLoc = new Location(mob.getWorld(), Math.floor(mob.getLocation().getX())+0.5, mob.getLocation().getBlockY(), Math.floor(mob.getLocation().getZ())+0.5, 90, 0);
			mob.teleport(mobLoc);
			
			Location plrLoc1 = new Location(mob.getWorld(), Math.floor(mob.getLocation().getX())-5.5, mob.getLocation().getBlockY(), Math.floor(mob.getLocation().getZ())+0.5, -90, 0);
			player.teleport(plrLoc1);
		    ToonCraft.playerFightMode.add(player);
			
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
			
			InventoryGUIs.GagsInventory(player);
			
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
		
		mobBlockRemoval(mob, player);
	}

	public static void mobBlockRemoval(LivingEntity mob, Player player) {
		if(ToonCraft.mobFightMode.contains(mob)) {
			ToonCraft.mobFightMode.remove(mob);
			
			if(player != null) {
				ToonCraft.playerFightMode.remove(player);
			}
						
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
