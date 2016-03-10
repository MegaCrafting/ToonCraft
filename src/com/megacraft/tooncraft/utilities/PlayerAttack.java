package com.megacraft.tooncraft.utilities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.megacraft.tooncraft.ToonCraft;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class PlayerAttack {
	
	public static void attackSquirt(Player player, int level) {        
        Vector direction = player.getEyeLocation().getDirection().clone();
        Location location = player.getEyeLocation();
        location = location.clone().add(direction.clone().multiply(3.0));
        
        if(ToonCraft.mobFightMode.containsKey(player)){
        	LivingEntity entity = ToonCraft.mobFightMode.get(player);
        	entity.damage(12, player);
        	
            LineEffect eff = new LineEffect(ToonCraft.getEM());
            eff.particle = ParticleEffect.WATER_DROP;
            eff.particles = 50*level;
            eff.setDynamicOrigin(new DynamicLocation(player.getLocation().add(0,1,0))); //adjust the Y value for height
            eff.setDynamicTarget(new DynamicLocation(entity.getLocation()));
            eff.isZigZag = false; //if you want it to zigzag
            eff.start();
        }
	}
	
}
