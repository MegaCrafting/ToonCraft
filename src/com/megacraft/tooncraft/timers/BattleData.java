package com.megacraft.tooncraft.timers;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.menuapi.Menu;
import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.cogs.Cog;
import com.megacraft.tooncraft.enums.CogAttackType;
import com.megacraft.tooncraft.utilities.InventoryGUIs;
import com.megacraft.tooncraft.utilities.MobSpawner;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;

public class BattleData  {

	
	
//	private TCPlayer tcp;
	private List<BlockState> changedBlocks = new ArrayList<BlockState>();

	//Fight Specific Data:
	private TCPlayer currentPlayer;   //the player currently attacking
	
	
	private Cog currentCog;  //the cog currently attacking
	private CogAttackType cogAttack;
	
    private List<TCPlayer> involvedPlayers = new ArrayList<TCPlayer>();
    private List<Cog> involvedCogs = new ArrayList<Cog>();
    
    
    private int playerCounter = 0;  // used to advance to the next player in line.
    private int cogCounter = 0;
    			
    							   //turn counter advances combat, if multi players are involved round counter is also used to allow others to attack.
	private int turnCounter = 0;   //a turn is defined as waiting on a player to select an attack, doing it, the cog returning fire, then repeating.
	
	
	private boolean playerAttackFinished = false;  //set once a player's gag has finished
	private boolean mobAtackStarted = false;      //set once the cog has retaliated.
	private boolean mobAtackFinished = false;      //set once the cog has retaliated.
	private boolean animationRunning = false;      //set while the fight animation is happening

	Object gagType = null;
	private long turnDelay = 0l;   
	
	public BattleData(Player plr) {
		ToonCraft.battleList.put(plr, this);
		this.currentPlayer = TCPlayer.findTCP(plr);
	}
	
	public static BattleData findBD(Player plr) {
		
		if(ToonCraft.battleList == null || ToonCraft.battleList.isEmpty()) 
			return new BattleData(plr);
		
		if(ToonCraft.battleList.containsKey(plr))
			return ToonCraft.battleList.get(plr);
		
		return new BattleData(plr);
	}

	public boolean endBattle(Cog c)
	{
		
		if(c != null)  {
			c.remove();
			this.involvedCogs.remove(c);
			if(!c.isDead())
				c.setDead(true);
		}
		
		if(this.involvedCogs.isEmpty()) {
			for(TCPlayer tcp:this.involvedPlayers)
				tcp.endBattle();
			
			this.involvedPlayers.clear();
			
			this.clearVariables();
			
			
			return true;
		}
		return false;
		
	}
	public void clearVariables()   //invoked when a round is over, signaling that it is time to move on to the next. 
									//does NOT end the battle use endBattle() for that.
	{
		
		
		if(this.involvedPlayers.size() > 1)
			this.playerCounter ++;  //select the next player in line when the next turn starts provided we continue on.
		if(this.playerCounter >= this.involvedPlayers.size())  
			this.playerCounter = 0; //reset after all players take a turn.  Probably need to implement a time based thing with multi players.
		
		
		this.turnCounter = 0;   // 0 unset , 1 = players turn, 2 = cogs turn
		this.playerAttackFinished = false;  //set once a player has selected their gag
		this.mobAtackFinished = false;      //set once the cog has retaliated.
		this.animationRunning = false;      //set while the fight animation is happening
		
		
		if(this.getCurrentPlayer() != null) {
			if(this.getCurrentPlayer().isMenuOpen)
				if(this.getCurrentPlayer().getGagMenu() != null)
					this.getCurrentPlayer().getGagMenu().close(this.getCurrentPlayer().getPlayer());
			this.getCurrentPlayer().setSelGag(null);
			this.getCurrentPlayer().setGagMenu(null);
			this.getCurrentPlayer().isMenuOpen = false;
		}
			
		
		
		this.gagType = null;
		this.setCurrentPlayer(null);
		if(this.getCurrentCog() == null || this.getCurrentCog().getEntity().isDead())
		{
			this.setCurrentCog(null);
			this.cogCounter++;
			if(this.cogCounter >= this.involvedCogs.size())
				this.cogCounter = 0;
		}
		this.setTurnDelay(0l);
	}
	

	
	public static BattleData findViaCog(Cog cog) {
		
		if(ToonCraft.battleList == null || ToonCraft.battleList.isEmpty())
			return null;
		
		for(Player p: ToonCraft.battleList.keySet())
		{
			BattleData bd  = ToonCraft.battleList.get(p);
			if(bd.getInvolvedCogs().contains(cog))
				return bd;
			
		}
		return null;
		
	}
	
	public void progressFight() {


		if(this.involvedPlayers.isEmpty())
		{
			System.out.println("No players involved in this battle... Ending");
			this.endBattle(this.currentCog);
			
		}
		
		if(!this.involvedPlayers.isEmpty())
			if(this.getCurrentPlayer() == null)
				this.setCurrentPlayer(this.involvedPlayers.get(this.playerCounter));    //get the next player in the involvedPlayers list
		
		if(!this.involvedCogs.isEmpty()) {
			if(this.getCurrentCog() == null) {	
				this.setCurrentCog(this.involvedCogs.get(this.cogCounter)); //get the next cog scheduled to attack
				System.out.println("No current cog selected.  Picked: " + this.cogCounter + " / " + this.currentCog.getEntity().getCustomName());
			}
			
		} else {
			System.out.println("No Cogs left In This Battle! Ending Early! ");
			this.endBattle(null);
			return;
		}

				
		if(this.turnCounter == 0 && System.currentTimeMillis() > this.turnDelay) // Setup Initial attack phase.
		{
			
			
			this.getCurrentPlayer().getPlayer().sendMessage("Please select a gag to use!");
			this.getCurrentPlayer().openMenu();
			this.turnCounter ++;
			//System.out.println("Initially Opening Gag Menu" + this.getGagMenu().getInventory().getName());
			return;
		}
		if(this.turnCounter == 1)
		{
			if(this.getCurrentPlayer().getSelGag() == null) {  //player hasn't selected a gag, open their menu if its not open.
					this.getCurrentPlayer().openMenu();
					return;
				
			} else {   //advance the counter
				this.getCurrentPlayer().closeMenu();  //this shouldn't really happen because selecting a gag closes it.
				
				this.setTurnDelay(System.currentTimeMillis() + 6000);  //wait 4 seconds before doing stuff.
				this.turnCounter++;
			}

		}
		
		
		if(this.turnCounter == 2)
				this.getCurrentPlayer().getSelGag().runGag(this);
				

		if(this.turnCounter == 4 && System.currentTimeMillis() > this.getTurnDelay())
		{

			///now dish out some damgae to the mob and set it up to retalliate!
			if(this.getCurrentCog() == null) {  //target cant retaliate if dead!
				this.endBattle(null);
				System.out.println("Cog died before the turn was over.. Canceling Battle");
				return;
			}
			
			
			if(!this.playerAttackFinished) {
				dealDamage(this.getCurrentCog(), this.getCurrentPlayer(), this.getCurrentPlayer().getSelGag());
				this.turnDelay = System.currentTimeMillis() + 3000;
				this.playerAttackFinished = true;
			}  else {
				if(!this.mobAtackStarted && System.currentTimeMillis() > this.turnDelay)
				{
						this.cogAttack = this.currentCog.getAttack();
						this.currentPlayer.getPlayer().sendMessage(this.currentCog.getEntity().getCustomName() + " uses " + this.cogAttack.getName() + "!");
						this.turnDelay = System.currentTimeMillis() + 4000;
						this.mobAtackStarted = true;
						this.currentCog.doAttack(this.currentPlayer, this.cogAttack);
				} else if(this.mobAtackStarted && System.currentTimeMillis() > this.turnDelay) {
					
				}
				
				
				//now do the mob stuff.
				
				
			}
			
			
				
			
			
			
			
			
			
			this.clearVariables(); //dont clear vars until the cog has retalliated.
			
		}
	}

	

	private void dealDamage (Cog cog, TCPlayer tcp, Gag gag)
	{
		//this function will deal damage to the cogs, and players. Need to figure out a concrete system for hit/miss and levels.
		/*
		 * Hit or miss logic
		 * if mobs level = player's skill level?  50% chance to hit each other.
		 * For every level the attacker is above the target add 5%?
		 * For every level the attacker is below the target subtract 15%
		 *    
		 */

		int cogLevel = MobSpawner.getCogLevel((int) cog.getEntity().getMaxHealth());
		int adjust = 0;

		if(!(cog instanceof Player)) {
			if(cogLevel > gag.getLevel())
				adjust = gag.getLevel() - cogLevel;
			else if(cogLevel < gag.getLevel())
				adjust = gag.getLevel() - cogLevel;

			
			int propAcc = gag.getAccuracy().getValue();
			
			int trackExp = 10*(TCPlayer.getHighestTrack(tcp)-1);
			int tgtDef = -5*(cogLevel-1);
			int bonus = 0; //calculate stun bonus..    //if prev gag, hit and was not same gag. 
							//must have hit whole group with one, or attack on same cog w/diff gag.
			
			if(this.getCurrentPlayer().getPrevGag() != null)
			{
				int vCount = 0;
				
				 if(this.getCurrentPlayer().getPrevTarget() == this.getCurrentCog())  //haven't worked fighting groups into it yet.
				 {
					 vCount = 1;  //target is same as last round
				 } else {  //was a new target, set prev target to this one.
					 this.getCurrentPlayer().setPrevTarget(this.getCurrentCog());  //set the new target.
					 this.getCurrentPlayer().setPrevHits(0);
				 }
				 if(this.getCurrentPlayer().getPrevGag() != gag)
					 vCount ++;  //
				 else
					 this.getCurrentPlayer().setPrevGag(gag);
				 
				 if(vCount == 2)  //condtions are met!  Except for AOE spells..  Apply bonus
				 {
					 
					 if(this.getCurrentPlayer().getPrevHits() > 3)
						 this.getCurrentPlayer().setPrevHits(3);
					 
					
					/*
					 *  conditions are met, PrevHits is incremented by the number of previous hits * 20 on the target(s) in the current round, 
					 *  with a bonus of +60 being possible (3 priors hits).  
					 */
					 if(this.getCurrentPlayer().getPrevHits() >= 0)
						 bonus = this.getCurrentPlayer().getPrevHits() * 20;
					 
					 
				 }
					 
				 
			}
			/*
			 * The luredRatio bonus is given to all Sound gags and the Throw/Squirt level 7 gags, when there is a mixed set of lured 
			 * and unlured Cogs on the field. It is calculated using the following formula.
    					luredRatio = ([number of Cogs lured]) / ([total Cogs on the field]) * 100 
			 */
			
			
			
			
			/* 
			 * tkAcc is the final result after the four aforementioned variables are evaluated. Once atkAcc was determined, it will be compared 
			 * to an pseudo-RNG that rolls a number from 0-99. If atkAcc is greater than the RNG, it is given an atkHit value of 1, and will hit. 
			 * Otherwise, the gag is given an atkHit value of 0, and will miss. 
			 */
			int atkAcc = propAcc + trackExp + tgtDef + bonus;
			
			int chance = ThreadLocalRandom.current().nextInt(0,99 );
			int atkHit = 0;
			
			if( atkAcc > chance)
				atkHit = 1;
			
					//    When a Trap gag is used, atkAcc is automatically set to 100, and atkHit is set to 1.
					//    For all other gags, if atkAcc exceeds 95, it will automatically be reduced to 95.
			if(gag.getType() == GagType.TRAP) {
				atkAcc = 100;
				atkHit = 1;
			}
			
			if(atkHit == 1) {
				this.getCurrentPlayer().setPrevGag(gag);
				if(this.getCurrentPlayer().getPrevHits() < 3)
					this.getCurrentPlayer().setPrevHits(this.getCurrentPlayer().getPrevHits() + 1);
				
				this.getCurrentPlayer().setPrevTarget(this.getCurrentCog());
				int dmg = ThreadLocalRandom.current().nextInt(gag.getMinDmg(),gag.getMaxDmg());
				System.out.println("Hitting " + cog.getEntity().getCustomName() + " for " + dmg + " damage with " + this.getCurrentPlayer().getSelGag().getName());
	//dmg = dmg + 10;
				
				
				//cog.getEntity().damage((double) dmg,tcp.getPlayer());
				Location cogLoc = cog.getEntity().getLocation();
				if(dmg <= cog.getEntity().getHealth()) {
					cog.getEntity().setHealth(cog.getEntity().getHealth() - dmg);
				} else {
					dmg = (int) cog.getEntity().getHealth();
					
					CitizensAPI.getNPCRegistry().deregister(cog.getNpc());
					System.out.println("NPC Killed in combat.");
					cog.setDead(true);

				}

				
				if(cog.getEntity().getHealth() < 1) {
				
					cog.setDead(true);
					System.out.println("removing the NPC entry from registry.");

				}
				
				
				
				cog.updateHealth();
				if(cog.getXpHolo() == null)
				{
					Hologram h = new Hologram("xpHolo" + cog.getNpc().getId() ,cogLoc.add(0,1.5,0) );
					h.setPersistent(false);
					
					HologramLine hl = new TextLine(h, ChatColor.GREEN + "+" + gag.getLevel() + " xp"); 
					h.addLine(hl);
					ToonCraft.holomanager.addActiveHologram(h);
					cog.setXpHolo(h); 
					this.getCurrentPlayer().updateXp(gag);
					
				}
				
				if(cog.isDead()) {
					//cog died so lets award xp and end the fight.
					this.currentPlayer.getPlayer().sendMessage("You have defeated a level " + cog.getLevel() + " "+ cog.getType().getName() + "! Well done toon!");
					cog.getNpc().destroy();
					
					this.endBattle(cog);
				}
				//otherwise the fight continues.
				
			}
			else
				tcp.getPlayer().sendMessage("You Missed!");
		}
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


	public long getTurnDelay() {
		return turnDelay;
	}

	public void setTurnDelay(long turnDelay) {
		this.turnDelay = turnDelay;
	}

	public List<TCPlayer> getInvolvedPlayers() {
		return involvedPlayers;
	}

	public void setInvolvedPlayers(List<TCPlayer> involvedPlayers) {
		this.involvedPlayers = involvedPlayers;
	}

	public List<Cog> getInvolvedCogs() {
		return involvedCogs;
	}

	public void setInvolvedCogs(List<Cog> involvedCogs) {
		this.involvedCogs = involvedCogs;
	}

	public TCPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(TCPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Cog getCurrentCog() {
		return currentCog;
	}

	public void setCurrentCog(Cog currentCog) {
		this.currentCog = currentCog;
	}

	public boolean isPlayerAttackFinished() {
		return playerAttackFinished;
	}

	public void setPlayerAttackFinished(boolean playerAttackFinished) {
		this.playerAttackFinished = playerAttackFinished;
	}




}
