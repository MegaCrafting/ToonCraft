package com.megacraft.tooncraft.gagSkills;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.timers.LocationData;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class SquirtAttack {

	private LocationData ld;
	private Effect efx;
	int step = 0;
	long lastUpdate = 0l;
	private long startTime = 0l;
	
	public SquirtAttack(LocationData locData) {
		
		this.ld = locData;
		
		if(ld.getTarget() != null)  //had a target lets shoot at it.
		{
			if(this.efx == null || this.efx.isDone()) {
				this.efx = new ArcEffect(ToonCraft.em);
				this.efx.setDynamicOrigin(new DynamicLocation(this.ld.getPlayer().getLocation()));
				this.efx.setDynamicTarget(new DynamicLocation(ld.getTarget()));
				((ArcEffect) this.efx).particle = ParticleEffect.WATER_SPLASH;
				this.efx.start();
				this.startTime = System.currentTimeMillis();
			}
		}
		
	}

	public void Cancel() 
	{
		if(this.efx != null)
			this.efx.cancel();
		this.efx = null;
		this.startTime = 0l;
		this.lastUpdate = 0l;
		
		
	}
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
