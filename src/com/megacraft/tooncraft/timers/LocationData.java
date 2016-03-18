package com.megacraft.tooncraft.timers;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.menuapi.Menu;
import com.megacraft.tooncraft.gagSkills.SquirtAttack;
import com.megacraft.tooncraft.listener.MobInteraction;
import com.megacraft.tooncraft.utilities.InventoryGUIs;
import com.megacraft.tooncraft.utilities.MobSpawner;

public class LocationData {

	private Location lockedLoc;
	private Player player;
	private List<BlockState> changedBlocks = new ArrayList<BlockState>();
	private LivingEntity target;

	//Fight Specific Data:
	private Gag selGag;


	private int turnCounter = 0;   // 0 unset , 1 = players turn, 2 = cogs turn
	private boolean playerAttackSelected = false;  //set once a player has selected their gag
	private boolean mobAtackFinished = false;      //set once the cog has retaliated.
	private boolean animationRunning = false;      //set while the fight animation is happening
	private Menu gagMenu = null;
	public boolean isMenuOpen = false;
	Object gagType = null;
	private long turnDelay = 0l;   

	public LocationData(Player player) {

		this.setPlayer(player);
		this.setLockedLoc(player.getLocation());

	}

	public void clearVariables(LocationData ld)
	{
		this.setSelGag(null);


		this.turnCounter = 0;   // 0 unset , 1 = players turn, 2 = cogs turn
		this.playerAttackSelected = false;  //set once a player has selected their gag
		this.mobAtackFinished = false;      //set once the cog has retaliated.
		this.animationRunning = false;      //set while the fight animation is happening
		this.gagMenu = null;
		this.isMenuOpen = false;
		this.gagType = null;
		this.setTurnDelay(0l);
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

	public void progressFight() {

		if(this.animationRunning) {
			progressAnimations();
			return;
		}

		if(this.turnCounter == 0) // Setup Initial attack phase.
		{
			player.sendMessage("You have engaged a " + this.target.getCustomName());
			player.sendMessage("Please select a gag to use!");
			this.setGagMenu(InventoryGUIs.getGagsInventory(player));
			this.getGagMenu().openForPlayer(player);
			this.isMenuOpen = true;
			this.turnCounter ++;
			return;
		}
		if(this.turnCounter == 1)
		{
			if(this.getSelGag() == null) {  //player hasn't selected a gag, open their menu if its not open.
				if(!this.isMenuOpen) {
					this.getGagMenu().openForPlayer(player); 
					return;
				} 
			} else {   //advance the counter 
				if(this.isMenuOpen) {
					this.getGagMenu().close(this.player);  //shouldn't happen but just in case.
					this.isMenuOpen = false;
				}
				this.setTurnDelay(System.currentTimeMillis() + 3000);  //wait 4 seconds before doing stuff.
				this.turnCounter++;
			}

		}
		
		
		if(this.turnCounter == 2)
				this.getSelGag().runGag(this);
				

		if(this.turnCounter == 4 && System.currentTimeMillis() > this.getTurnDelay())
		{


			dealDamage(this.target, this.getSelGag());
			clearVariables(this);
			///now dish out some damgae to the mob and set it up to retalliate!
		}
	}

	private void dealDamage (LivingEntity cog, Gag gag)
	{
		//this function will deal damage to the cogs, and players. Need to figure out a concrete system for hit/miss and levels.
		/*
		 * Hit or miss logic
		 * if mobs level = player's skill level?  50% chance to hit each other.
		 * For every level the attacker is above the target add 5%?
		 * For every level the attacker is below the target subtract 15%
		 *    
		 */

		int cogLevel = MobSpawner.getCogLevel((int) cog.getMaxHealth());
		int adjust = 0;

		if(!(cog instanceof Player)) {
			if(cogLevel > gag.getLevel())
				adjust = gag.getLevel() - cogLevel;
			else if(cogLevel < gag.getLevel())
				adjust = gag.getLevel() - cogLevel;



			int atkAcc = 0; //propAcc + trackExp + tgtDef + bonus
			int chance = ThreadLocalRandom.current().nextInt(0,99 );

			if( atkAcc > chance)
				System.out.println("HIT! " + chance);
			else
				System.out.println("MISS: " + chance);
		}
	}
	private void progressAnimations() {
		// TODO Auto-generated method stub

	}

	public boolean isMenuOpen() {
		return isMenuOpen;
	}

	public void setMenuOpen(boolean isMenuOpen) {
		this.isMenuOpen = isMenuOpen;
	}

	public Menu getGagMenu() {
		return gagMenu;
	}

	public void setGagMenu(Menu gagMenu) {
		this.gagMenu = gagMenu;
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

	public boolean isPlayerAttackSelected() {
		return playerAttackSelected;
	}

	public void setPlayerAttackSelected(boolean playerAttackSelected) {
		this.playerAttackSelected = playerAttackSelected;
	}

	public boolean isMobAtackFinished() {
		return mobAtackFinished;
	}

	public void setMobAtackFinished(boolean mobAtackFinished) {
		this.mobAtackFinished = mobAtackFinished;
	}

	public int getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(int turnCounter) {
		this.turnCounter = turnCounter;
	}

	public boolean isAnimationRunning() {
		return animationRunning;
	}

	public void setAnimationRunning(boolean animationRunning) {
		this.animationRunning = animationRunning;
	}

	public Gag getSelGag() {
		return selGag;
	}

	public void setSelGag(Gag selGag) {
		this.selGag = selGag;
	}

	public long getTurnDelay() {
		return turnDelay;
	}

	public void setTurnDelay(long turnDelay) {
		this.turnDelay = turnDelay;
	}


}
