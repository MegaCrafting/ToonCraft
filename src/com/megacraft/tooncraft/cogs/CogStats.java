package com.megacraft.tooncraft.cogs;

import java.util.HashMap;

import com.megacraft.tooncraft.enums.CogAttackType;
import com.megacraft.tooncraft.enums.CogType;

public class CogStats {
	
	
	
		
	private CogAttackType attackType;
	private int damage[];
	private int accuracy[];    
	private int useChance[];
    
	public CogStats(CogAttackType type, int[] damage, int[] accuracy, int[] useChance)
	{
		this.setAttackType(type);
		this.setDamage(damage);
		this.setAccuracy(accuracy);
		this.setUseChance(useChance);
		
	}

	public CogAttackType getAttackType() {
		return attackType;
	}

	public void setAttackType(CogAttackType attackType) {
		this.attackType = attackType;
	}

	public int[] getDamage() {
		return damage;
	}

	public void setDamage(int damage[]) {
		this.damage = damage;
	}

	public int[] getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy[]) {
		this.accuracy = accuracy;
	}

	public int getUseChance(int level, CogType type) {
		
		int offset  = level - type.getMinLvl();   //how much higher than the minimum are they?
		
		//if min level is 1, and this cog is 3, the offset would 2...
		//meaning they should select the third skill..
		//so if min level is 5 and the cog is 8 then we need to do uh..
		//8-5 = 3..  third skill.
		//finally so if min level is 8 and cog is 8, 8-8 = 0, aka the 1st skill.
		return useChance[offset];
	}

	public void setUseChance(int useChance[]) {
		this.useChance = useChance;
	}
	
	
}
