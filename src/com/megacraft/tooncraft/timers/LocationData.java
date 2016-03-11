package com.megacraft.tooncraft.timers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.megacraft.tooncraft.listener.MobInteraction;

public class LocationData {

	private Location lockedLoc;
	private Player player;
	private List<BlockState> changedBlocks = new ArrayList<BlockState>();
	private LivingEntity target;
	
	public LocationData(Player player) {
	
		this.setPlayer(player);
		this.setLockedLoc(player.getLocation());
		
	}

	public boolean moveAllowed()   //allow them a little movement looking around but if they move too far, re-center their view
	{
		
		Player p = this.getPlayer();  
		Location movedLoc = p.getLocation();
		
		if(movedLoc.getBlockX() != this.getLockedLoc().getBlockX()) 
			return false;
		
		if(movedLoc.getBlockZ() != this.getLockedLoc().getBlockZ()) 
			return false;
		
		double yaw =  getPlayer().getLocation().getYaw();
		double yawLock = this.lockedLoc.getYaw();
		double diff = 0;
		
		if(yawLock > 180)
			diff = (movedLoc.getYaw() + this.getLockedLoc().getYaw());
		else 
			diff = (movedLoc.getYaw() - this.getLockedLoc().getYaw());
		
		if(diff > 60 || diff < -60)
			return false;
		
		double diff2 = (Math.abs(movedLoc.getPitch()) - Math.abs(this.getLockedLoc().getPitch())); 
		if( diff2 >=45) 
			return false;
		
		
		
		return true;
	}

	public static LocationData findLD(Player player2) {
		if(MobInteraction.playerLoc == null || MobInteraction.playerLoc.isEmpty()) {
			LocationData ld = new LocationData(player2);
			MobInteraction.playerLoc.add(ld);
			return ld;
		}
		for(LocationData ld : MobInteraction.playerLoc)
		{
			if(ld.getPlayer() == player2)
				return ld;
		}
		
		return new LocationData(player2);
	}

	public Location getLockedLoc() {
		return this.lockedLoc;
	}

	public void setLockedLoc(Location lockedLoc) {
		this.lockedLoc = lockedLoc;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
