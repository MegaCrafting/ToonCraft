package com.megacraft.tooncraft;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.menuapi.Menu;
import com.megacraft.tooncraft.cogs.Cog;
import com.megacraft.tooncraft.playerGagData.PlayerGagData;
import com.megacraft.tooncraft.storage.DBConnection;
import com.megacraft.tooncraft.timers.BattleData;
import com.megacraft.tooncraft.utilities.InventoryGUIs;

public class TCPlayer {

	private Player player;
	
	private HashMap<GagType,PlayerGagData> GagLevels = new HashMap<GagType,PlayerGagData>();

	private Gag selGag;
    private Gag prevGag;
    
    
    private Cog prevTarget;
    private int prevHits;

	
	private BattleData battleData;
	private int level;
	private int exp;
	private Location lockedLocation;
	private double lockedYaw;
	
	private Cog target;
	
	private Menu gagMenu = null;
	public boolean isMenuOpen = false;
	
	
	public TCPlayer(Player player) {
		this.setPlayer(player);
		DBConnection.getPlayerGags(this);
		ToonCraft.tcPlayers.put(player, this);
		
	
	}

	public void endBattle()
	{
		DBConnection.updatePlayerData(this);
		ToonCraft.battleList.remove(this.player);
		this.lockedLocation = null;
		this.target = null;
		this.battleData = null;
		
		
		
	}
	
	public boolean isMoveAllowed()   //allow them a little movement looking around but if they move too far, re-center their view
	{

		if(this.lockedLocation == null)
			return true;
		
		Player p = this.player;  
		Location movedLoc = p.getLocation();

		if(movedLoc.getBlockX() != this.getLockedLocation().getBlockX()) 
			return false;

		if(movedLoc.getBlockZ() != this.getLockedLocation().getBlockZ()) 
			return false;

		
		double yawLock = this.getLockedLocation().getYaw();
		double diff = 0;

		if(yawLock > 180)
			diff = (movedLoc.getYaw() + this.getLockedLocation().getYaw());
		else 
			diff = (movedLoc.getYaw() - this.getLockedLocation().getYaw());

		if(diff > 60 || diff < -60)
			return false;

		double diff2 = (Math.abs(movedLoc.getPitch()) - Math.abs(this.getLockedLocation().getPitch())); 
		if( diff2 >=65) 
			return false;



		return true;
	}

	public void openMenu()
	{
		if(!this.isMenuOpen) {
			if(this.getGagMenu() == null)
				this.setGagMenu(InventoryGUIs.getGagsInventory(this.getPlayer()));
		
			this.getGagMenu().openForPlayer(this.getPlayer()); 
			this.isMenuOpen = true;
		}
	}
	public void closeMenu()
	{
		if(this.isMenuOpen) {
			this.getGagMenu().close(this.getPlayer());
			this.isMenuOpen = false;
		}
	}
	public static Integer getHighestTrack(TCPlayer tcp)
	{
		int topLvl = 0;
		int lastTop = 0;
		
		
		if(tcp.getGagLevels().isEmpty())
			System.out.println("Warning gagLevels map was empty!");
		
		
		for(GagType gt:tcp.getGagLevels().keySet())
		{
			
			topLvl = 0;	
			PlayerGagData pd = tcp.GagLevels.get(gt);
			topLvl = pd.getHighestTrack();
			
			if(topLvl > lastTop) {
				lastTop = topLvl;  //overwrite the previous top level with the current one.
			}
		}
		System.out.println("Highest track level was: " + lastTop);
		return lastTop;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}



	public static TCPlayer findTCP(Player plr) {
		
		if(ToonCraft.tcPlayers.isEmpty())
			return new TCPlayer(plr);

		if(ToonCraft.tcPlayers.containsKey(plr))
			return ToonCraft.tcPlayers.get(plr);
		
		return new TCPlayer(plr);
	}


	public HashMap<GagType,PlayerGagData> getGagLevels() {
		return GagLevels;
	}


	public void setGagLevels(HashMap<GagType,PlayerGagData> gagLevels) {
		GagLevels = gagLevels;
	}


	public Location getLockedLocation() {
		return lockedLocation;
	}


	public void setLockedLocation(Location lockedLocation) {
		this.lockedLocation = lockedLocation;
	}


	public Cog getTarget() {
		return target;
	}


	public void setTarget(Cog target) {
		this.target = target;
	}

	public BattleData getBattleData() {
		return battleData;
	}

	public void setBattleData(BattleData battleData) {
		this.battleData = battleData;
	}

	public Menu getGagMenu() {
		return gagMenu;
	}

	public void setGagMenu(Menu gagMenu) {
		this.gagMenu = gagMenu;
	}

	public Gag getSelGag() {
		return selGag;
	}

	public void setSelGag(Gag selGag) {
		this.selGag = selGag;
	}

	public Gag getPrevGag() {
		return prevGag;
	}

	public void setPrevGag(Gag prevGag) {
		this.prevGag = prevGag;
	}

	public Cog getPrevTarget() {
		return prevTarget;
	}

	public void setPrevTarget(Cog prevTarget) {
		this.prevTarget = prevTarget;
	}

	public int getPrevHits() {
		return prevHits;
	}

	public void setPrevHits(int prevHits) {
		this.prevHits = prevHits;
	}

	public void updateXp(Gag gag) {
		
		PlayerGagData gd = this.GagLevels.get(gag.getType());
		gd.updateXp(gag.getLevel());
		
	}

	
}
