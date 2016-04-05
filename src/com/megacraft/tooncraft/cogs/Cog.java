package com.megacraft.tooncraft.cogs;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.enums.CogAttackType;
import com.megacraft.tooncraft.enums.CogType;
import com.megacraft.tooncraft.storage.tcBlockData;
import com.megacraft.tooncraft.timers.BattleData;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;

public class Cog  {

	private boolean isTrapped;
	private List<CogStats> atkStats = new ArrayList<CogStats>();
	private CogType type;
	private NPC npc;
	private int pathIndex;
	private int maxPathIndex;
	private long moveDelay;
	private Hologram holo;
	private Hologram xpHolo;
	private Long xpHoloDelay = 0l;
	private int level;
	private boolean isDead = false;
	//private LivingEntity entity;
	private LivingEntity entity;
	private List<tcBlockData> changedBlocks = new ArrayList<tcBlockData>();
	
		public Cog(NPC npc, CogType type, int level) {
			
				this.npc = npc;
				
					
					
				
				this.setEntity((LivingEntity)entity);
				this.setType(type);
				this.level = level;
				
				switch(type) {
				
				case FLUNKY:
					this.atkStats.add(new CogStats(CogAttackType.CLIP_ON_TIE, new int [] {1,1,2,2,3}, new int[] {75,80,85,90,95}, new int[]{60,50,40,30,20}));
					this.atkStats.add(new CogStats(CogAttackType.SHRED, new int [] {3,4,5,6,7}, new int[] {50,55,60,65,70}, new int[]{10,15,20,25,30}));
					break;
				case BIGCHEESE:
					break;
				case COLD_CALLER:
					break;
				case CORPRAIDER:
					break;
				case DOWNSISER:
					break;
				case GLAD_HANDLER:
					break;
				case HEADHUNTER:
					break;
				case HOLLYWOOD:
					break;
				case MICROMAN:
					break;
				case MINGLER:
					break;
				case MOVERSHAKER:
					break;
				case NAME_DROPPER:
					break;
				case PPUSHER:
					break;
				case TELEMARKETER:
					this.atkStats.add(new CogStats(CogAttackType.CLIP_ON_TIE, new int [] {2,2,3,3,4}, new int[] {75,75,75,75,75}, new int[]{15,15,15,15,15}));
					break;
				case TWO_FACE:
					break;
				case YESMAN:
					break;
				default:
					break;
				}
				//this bit enables a skill for that particular cog type.
				
				
				
				//when cogs are spawned in, add them to the cog list and set their battle data to null.
				//as long as battle data is null they are considered interactable.
				//you can then quickly check to see if a cog is in battle, and if so find out whom with via BattleData
				
				ToonCraft.cogList.put(this, null);  
				 
				
		}
		
		public CogAttackType getAttack()
		{
			
			for(CogStats cs:this.atkStats)
			{
				int chance = ThreadLocalRandom.current().nextInt(1,100);
				if(cs.getUseChance(this.level, this.type) > chance)
				{
					return cs.getAttackType();
				}
			}
			
			return null;
		}

		public boolean trapNewPlayer(Player player, BattleData bd)
		{
			
			Player lastAdded = bd.getInvolvedPlayers().get(bd.getInvolvedPlayers().size()-1).getPlayer();
			Location pLoc = lastAdded.getLocation();
			pLoc.setPitch(0);
			
			pLoc.setYaw(pLoc.getYaw() + 90);
			Vector dir = pLoc.getDirection();
			
			Block plrBlock = pLoc.add(dir.add(dir.clone().normalize().multiply(2)).toLocation(player.getWorld())).getBlock();
			Location plrLoc = plrBlock.getLocation().subtract(.5,0,.5);
	        plrLoc.setY(player.getLocation().getY());
	        plrLoc.setYaw(plrLoc.getYaw()-90);
			player.teleport(plrLoc);
			
			
						return true;
		}

		
		public boolean trapCog(Player player)
		{
			Location pLoc = player.getLocation();
			pLoc.setPitch(0);
			player.teleport(pLoc);
			
			Vector dir = pLoc.getDirection();
			boolean validLoc = true;
			int tryCount = 0;
			
	        Block mobBlock = player.getLocation().add(dir.add(dir.clone().normalize().multiply(5)).toLocation(player.getWorld())).getBlock();
	        Location mobLoc = mobBlock.getLocation().subtract(.5,0,.5);
	        mobLoc.setY(player.getLocation().getY());
	        
	        Location location = player.getLocation();
	        while(tryCount <= 6)
	        {
	        	validLoc = true;
	        	BlockIterator blocksToAdd = new BlockIterator(location, 2, 5);
	        	Location blockToAdd;
	        	
	        	while(blocksToAdd.hasNext()) {
	        		blockToAdd = blocksToAdd.next().getLocation();
	        		if(blockToAdd.getBlock().getType() != Material.AIR) {
	        			location.setYaw(location.getYaw() + 90);
	        			validLoc = false;
	        			//System.out.println("Rotating because invalid: " + blockToAdd.getBlock().getType() + " try count: " + tryCount);
	        			break;
                	}
	        		
	        	}
	        	if(validLoc)
	        		break;
	        	tryCount++;
	        }
	        
	        if(tryCount >=4) {
	        	System.out.println("Unable to find a valid location to set up player / cog battle!");
	        	pLoc = player.getLocation();
	        	System.out.println("Player: " + player.getName() + " coords: " + pLoc.getBlockX() + " x, " + pLoc.getBlockY() + " y, " + pLoc.getBlockZ() + " z ");
	        	return false;
	        	
	        	
	        }
	        if(location != pLoc)
	           	player.teleport(location);
	        
	        pLoc = player.getLocation();
        	dir = pLoc.getDirection();
        	mobBlock = pLoc.add(dir.add(dir.clone().normalize().multiply(5)).toLocation(player.getWorld())).getBlock();
        	mobLoc = mobBlock.getLocation().subtract(.5,0,.5);
	        mobLoc.setY(player.getLocation().getY());
	        this.npc.getNavigator().cancelNavigation();
	        this.getEntity().teleport(mobLoc);

	        
			BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			
			Block b = this.getEntity().getLocation().add(0,2,0).getBlock();  //start with top block
			
			this.changedBlocks.add(new tcBlockData(b));
			b.setType(Material.BARRIER);
			for(BlockFace f:faces)
			{
				b = this.getEntity().getLocation().getBlock().getRelative(f);
				this.changedBlocks.add(new tcBlockData(b));
				b.setType(Material.BARRIER);
			}
			
			
			
			if(this.holo != null) {
				
				this.updateHealth();
			}
			
			return true;
		}
		
		public void revertBlocks()
		{
			if(this.changedBlocks != null && !this.changedBlocks.isEmpty())
				for(tcBlockData bd: this.changedBlocks)
					bd.restore();
				
				
		}
		public void updateHealth()
		{
			
			if(this.holo != null)
			{
				long health = Math.round(this.getEntity().getHealth() * 100) / 100;
				ChatColor col = ChatColor.GREEN;
				
				float percent = (float) ((health*100)/ this.getEntity().getMaxHealth());
				
				if(percent < 75)
					col = ChatColor.DARK_GREEN;
				if(percent < 50)
					col = ChatColor.RED;
				if(percent < 25)
					col = ChatColor.DARK_RED;
				
				String healthTxt =  col+  " (" + health + ")";
				
				if(this.holo.getLines().size() > 1)
					this.holo.removeLine(this.holo.getLine(1));
				
				HologramLine hl = new TextLine(this.holo, healthTxt);
				this.holo.addLine(hl);
			
			}
			
		}
		
		public void remove()
		{
			
			
			if(!this.changedBlocks.isEmpty())
				this.revertBlocks();
			
			if(this.holo != null)
				ToonCraft.holomanager.deleteHologram(this.holo);
			
		}
		public static Cog findCog(LivingEntity e)
		{
			if(e instanceof Player)
				return null;
			
			if(ToonCraft.cogList.isEmpty())
				return null;
			
			
			
				for(Cog c:ToonCraft.cogList.keySet())
					if(c.getEntity() == e)
						return c;
			
			
			
			return null;
				
				
		}
		
		public LivingEntity getEntity() {
			return entity;
		}

		public void setEntity(LivingEntity entity) {
			this.entity = (Player) entity;
		}

		public CogType getType() {
			return type;
		}

		public void setType(CogType type) {
			this.type = type;
		}


		public boolean isTrapped() {
			return isTrapped;
		}


		public void setTrapped(boolean isTrapped) {
			this.isTrapped = isTrapped;
		}


		public int getLevel() {
			return level;
		}


		public void setLevel(int level) {
			this.level = level;
		}
		public String getTaunt() {
			
			if(this.getType().getPhrases() == null)
				return "nothing but looks pretty angry!";
			
			String[] phrases = this.getType().getPhrases();
			
			return phrases[ThreadLocalRandom.current().nextInt(0,phrases.length)]; 
		}
		public void doAttack(TCPlayer currentPlayer, CogAttackType attackType) {
			
			switch(attackType)
			{
			case CLIP_ON_TIE:
				break;
			case SHRED:
				break;
			default:
				break;
			
			}
			
		}

		public int getPathIndex() {
			return pathIndex;
		}

		public void setPathIndex(int pathIndex) {
			this.pathIndex = pathIndex;
		}

		public int getMaxPathIndex() {
			return maxPathIndex;
		}

		public void setMaxPathIndex(int maxPathIndex) {
			this.maxPathIndex = maxPathIndex;
		}

		public long getMoveDelay() {
			return moveDelay;
		}

		public void setMoveDelay(long moveDelay) {
			this.moveDelay = moveDelay;
		}

		public Hologram getHolo() {
			return holo;
		}

		public void setHolo(Hologram holo) {
			this.holo = holo;
		}

		public void updateHologram() {
			
				if(this.getHolo().getLocation().distance(this.getEntity().getLocation().add(0,2.5,0)) > .5)
					if(!this.npc.getNavigator().isNavigating()) {

					this.getHolo().teleport(this.getEntity().getLocation().add(0,2.5,0));
					this.getHolo().spawn();
					
				}
			
			
		}

		public NPC getNpc() {
			return npc;
		}

		public void setNpc(NPC npc) {
			this.npc = npc;
		}

		public boolean isDead() {
			return isDead;
		}

		public void setDead(boolean isDead) {
			this.isDead = isDead;
		}

		public Hologram getXpHolo() {
			return xpHolo;
		}

		public void setXpHolo(Hologram xpHolo) {
			this.xpHolo = xpHolo;
		}

		public Long getXpHoloDelay() {
			return xpHoloDelay;
		}

		public void setXpHoloDelay(Long xpHoloDelay) {
			this.xpHoloDelay = xpHoloDelay;
		}

	

		
	
}
