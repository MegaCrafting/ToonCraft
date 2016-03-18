package com.megacraft.gags;


import java.util.ArrayList;
import java.util.List;

import com.megacraft.enums.Accuracy;
import com.megacraft.enums.GagType;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.gagSkills.SquirtAttack;
import com.megacraft.tooncraft.timers.LocationData;

public class Gag {

	private String name;
	private int level;
	private int expReq;
	private Accuracy accuracy;
	private boolean AOE= false;
	private int minDmg;
	private int maxDmg;
	private String organicBoost;
	private int ammoStart;
	private int ammoMax;
	private String perm;
	private Object efx;
	private GagType type;
	private int pos;
	private List<String> lore = new ArrayList<String>();
	public Gag(String name) {

		this.name = name;
	}

	
	public static Gag getGagFromInvPos(int slot)
	{
		
		for(GagType gt:ToonCraft.loadedGags.keySet())
		{
			List<Gag> gags = ToonCraft.loadedGags.get(gt);
			for(Gag g:gags) {
				int row = gt.getRow();
				row = row * 8; 
				int col = g.getLevel()- 1;
				
				int pos = row;
				pos = row+col;
			//	System.out.println("Row: " + row + " Col: " + col + " calculated slot:  " + pos + " actual slot: " + slot);
				if(pos == slot) {
					return g;
				}
			}
		}
		System.out.println("Failed to find a gag at slot: " + slot);
		return null;
		
	}
	
	/* 
	 * This is gonna be a big daddy when it's done.   Because we need to handle all gag attack animations here.
	 */
	
	public void runGag(LocationData ld) {
		switch(this.getType()) {
		case DROP:
			break;
		case LURE:
			break;
		case SOUND:
			break;
		case SQUIRT:
			handleSquirt(ld);
			break;
		case THROW:
			break;
		case TOONUP:
			break;
		case TRAP:
			break;
		default:
			break;
		
		}
	}

	public void handleSquirt(LocationData ld)  //run effects for each squirt type.
	{
		switch(this.level)
		{
		case 1:
			if(!ld.isAnimationRunning()) 
			{
				this.efx = new SquirtAttack(ld);
				ld.setAnimationRunning(true);
				ld.setTurnDelay(System.currentTimeMillis() + 1500);
				
			} else if(System.currentTimeMillis() >= ld.getTurnDelay()) {
				((SquirtAttack) this.efx).Cancel();
				ld.setTurnCounter(4);
				System.out.println("Squrt stopped");
				ld.setTurnDelay(System.currentTimeMillis() + 3500);
				ld.setAnimationRunning(false);
			} else {
				System.out.println("time check");
			}
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
			
		}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExpReq() {
		return expReq;
	}

	public void setExpReq(int expReq) {
		this.expReq = expReq;
	}

	public Accuracy getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Accuracy accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isAOE() {
		return AOE;
	}

	public void setAOE(boolean aOE) {
		AOE = aOE;
	}

	public int getMinDmg() {
		return minDmg;
	}

	public void setMinDmg(int minDmg) {
		this.minDmg = minDmg;
	}

	public int getMaxDmg() {
		return maxDmg;
	}

	public void setMaxDmg(int maxDmg) {
		this.maxDmg = maxDmg;
	}

	public String getOrganicBoost() {
		return organicBoost;
	}

	public void setOrganicBoost(String string) {
		this.organicBoost = string;
	}

	public int getAmmoStart() {
		return ammoStart;
	}

	public void setAmmoStart(int ammoStart) {
		this.ammoStart = ammoStart;
	}

	public int getAmmoMax() {
		return ammoMax;
	}

	public void setAmmoMax(int ammoMax) {
		this.ammoMax = ammoMax;
	}

	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public GagType getType() {
		return type;
	}

	public void setType(GagType type) {
		this.type = type;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}


    
}

/*
 * Load Up The Gags
 * 		config.addDefault("Tooncraft.gag.squirt.level", 7);
		config.addDefault("Tooncraft.gag.squirt.name", "Geyser");
		config.addDefault("Tooncraft.gag.squirt.expReq", 10000);
		config.addDefault("Tooncraft.gag.squirt.accuracy", "high");
		config.addDefault("Tooncraft.gag.squirt.aoe", true);
		config.addDefault("Tooncraft.gag.squirt.minDmg", 105);
		config.addDefault("Tooncraft.gag.squirt.maxDmg", 105);
		config.addDefault("Tooncraft.gag.squirt.organicBoost", "115-115");
		config.addDefault("Tooncraft.gag.squirt.ammoStart", 0);
		config.addDefault("Tooncraft.gag.squirt.ammoMax", 1);

 */
