package com.megacraft.tooncraft.gagSkills;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.timers.LocationData;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class SquirtAttack {

	private LocationData ld;
	private Effect efx;
	int step = 0;
	long lastUpdate = 0l;
	private long startTime = 0l;
	private boolean cancel = false;
	
	public SquirtAttack(LocationData locData) {
		
		this.ld = locData;
		
		if(ld.getTarget() != null)  //had a target lets shoot at it.
		{
			if(this.efx == null || this.efx.isDone()) {
				this.efx = new ArcEffect(ToonCraft.em);
				this.efx.setDynamicOrigin(new DynamicLocation(this.ld.getPlayer().getLocation()));
				this.efx.setDynamicTarget(new DynamicLocation(this.ld.getTarget()));
				
				((ArcEffect) this.efx).particle = ParticleEffect.WATER_SPLASH;
				((ArcEffect) this.efx).type = EffectType.REPEATING;
				this.efx.start();
				
				
			}
		}
		if(this.cancel)
		{
			if(this.efx != null)
				this.efx.cancel();
			
			this.efx = null;
			this.startTime = 0l;
			this.lastUpdate = 0l;
		
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

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

}