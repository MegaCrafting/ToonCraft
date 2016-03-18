package com.megacraft.tooncraft;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


















import com.megacraft.enums.Accuracy;
import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.tooncraft.commands.Commands;
import com.megacraft.tooncraft.configuration.ConfigManager;
import com.megacraft.tooncraft.listener.MainListener;
import com.megacraft.tooncraft.listener.MobInteraction;
import com.megacraft.tooncraft.storage.DBConnection;
import com.megacraft.tooncraft.timers.FallTimer;
import com.megacraft.tooncraft.timers.MobTimer;
import com.megacraft.tooncraft.tutorial.TutorialListener;
import com.megacraft.tooncraft.utilities.InventoryGUIs;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class ToonCraft extends org.bukkit.plugin.java.JavaPlugin {
	
	public static ToonCraft plugin;
	public static Logger log;
    
	public static Plugin wg;
	public static List<BukkitTask> tasks = new ArrayList<BukkitTask>();
	
    public static Boolean InventoryAPIEnabled = true;
	public static List<LivingEntity> interactables = new ArrayList<LivingEntity>();
	
	public static HashMap<LivingEntity, List<Player>> mobFightMode = new HashMap<LivingEntity, List<Player>>();
	
	public static HashMap<GagType, List<Gag>> loadedGags = new HashMap<GagType,List<Gag>>();
	
	//public static List<LivingEntity> mobFightMode = new ArrayList<LivingEntity>();
	public static List<Player> playerFightMode = new ArrayList<Player>();

	public static EffectManager em;
	@Override
	public void onEnable() {
		ToonCraft.plugin = this;
		
        if(getServer().getPluginManager().getPlugin("InventoryAPI") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "To use the custom Inventory's please install InventoryAPI");
            InventoryAPIEnabled = false;
        } else {
        	InventoryAPIEnabled = true;
        }
        
		File mainfile = new File(plugin.getDataFolder().toString());
		if (!mainfile.exists()) {
			mainfile.mkdir();
		}
		
		DBConnection.init();
		if (DBConnection.isOpen() == false) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "It appears that the database has failed to connect! Please fix this issue and try again.");
		}
		
		ToonCraft.log = getLogger();
		new ConfigManager();
				
		getServer().getPluginManager().registerEvents(new MobInteraction(this), this);
		getServer().getPluginManager().registerEvents(new MainListener(this), this);
		getServer().getPluginManager().registerEvents(new TutorialListener(this), this);
		getServer().getPluginManager().registerEvents(new Commands(this), this);
		
		wg = ToonCraft.plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		if(wg != null) {
			 tasks.add(ToonCraft.plugin.getServer().getScheduler().runTaskTimer(this, new MobTimer(this), 0, 10l));
			 tasks.add(ToonCraft.plugin.getServer().getScheduler().runTaskTimer(this, new FallTimer(this), 0, 5l));
			System.out.println("ToonCraft:  Starting random cog spawn timer.");
		} else {
			System.out.println("ToonCraft:  Failed to find worldguard, unable to auto spawn mobs!");
		}
		LoadConfig();
		EffectLib lib = EffectLib.instance();
		em = new EffectManager(lib);
		
		
	}
	
	public void LoadConfig() {
		
			/*
			 * I was originally going to put all these in a config file... But it seems stupid once set there should be no reason to change the gags.
			 */
			List<Gag> squirtGags = new ArrayList<Gag>();
			
			Gag loadGag = new Gag("Squirting Flower");
			loadGag.setLevel(1);
		
			loadGag.setType(GagType.SQUIRT);
			loadGag.setExpReq(0);  
			loadGag.setPerm("Tooncraft.starter");  //unlocked after tutorial?
			loadGag.setAccuracy(Accuracy.HIGH);
			loadGag.setAOE(false);
			loadGag.setMinDmg(3);
			loadGag.setMaxDmg(4);
			loadGag.setOrganicBoost("4-5");  //no idea what this does yet.
			loadGag.setAmmoStart(10);
			loadGag.setAmmoMax(30);
			loadGag.getLore().add(ChatColor.AQUA + "Fires a stream of water into the face of opposing cogs.");
			loadGag.getLore().add(ChatColor.GRAY + "Accuracy"  + loadGag.getAccuracy().name());
			loadGag.getLore().add(ChatColor.GRAY + "Minimum Dmg"  + loadGag.getMinDmg());
			loadGag.getLore().add(ChatColor.GRAY + "Maximum Dmg"  + loadGag.getMaxDmg());
			squirtGags.add(loadGag);
			
			loadGag = new Gag("Glass Of Water");
		
			loadGag.setType(GagType.SQUIRT);
			loadGag.setLevel(2);
			loadGag.setExpReq(10);  
			loadGag.setPerm("Tooncraft.squirt.glass");  
			loadGag.setAccuracy(Accuracy.HIGH);
			loadGag.setAOE(false);
			loadGag.setMinDmg(6);
			loadGag.setMaxDmg(8);
			loadGag.setOrganicBoost("7-9");  
			loadGag.setAmmoStart(5);
			loadGag.setAmmoMax(25);
			loadGag.getLore().add(ChatColor.AQUA + "Dumps a bucket o' water on a single cog.");
			loadGag.getLore().add(ChatColor.GRAY + "Accuracy"  + loadGag.getAccuracy().name());
			loadGag.getLore().add(ChatColor.GRAY + "Minimum Dmg"  + loadGag.getMinDmg());
			loadGag.getLore().add(ChatColor.GRAY + "Maximum Dmg"  + loadGag.getMaxDmg());


			squirtGags.add(loadGag);
			
			loadGag = new Gag("Squirt Gun");
		
			loadGag.setType(GagType.SQUIRT);
			loadGag.setLevel(3);
			loadGag.setExpReq(50);  
			loadGag.setPerm("Tooncraft.squirt.glass");  
			loadGag.setAccuracy(Accuracy.HIGH);
			loadGag.setAOE(false);
			loadGag.setMinDmg(10);
			loadGag.setMaxDmg(12);
			loadGag.setOrganicBoost("11-13");  
			loadGag.setAmmoStart(5);
			loadGag.setAmmoMax(20);
			loadGag.getLore().add(ChatColor.AQUA + "The Hippie Soaker 2000 Water Gun");
			loadGag.getLore().add(ChatColor.GRAY + "Accuracy"  + loadGag.getAccuracy().name());
			loadGag.getLore().add(ChatColor.GRAY + "Minimum Dmg"  + loadGag.getMinDmg());
			loadGag.getLore().add(ChatColor.GRAY + "Maximum Dmg"  + loadGag.getMaxDmg());

			squirtGags.add(loadGag);			
		
			ToonCraft.loadedGags.put(GagType.SQUIRT, squirtGags);
			
			
			
		if(ToonCraft.loadedGags.size() > 0)
		{
			System.out.println("Loaded " + ToonCraft.loadedGags.size() + " Gags from Config.");
		}
				
				
	}
	
	@Override
	public void onDisable() {
		if (DBConnection.isOpen != false) {
			DBConnection.sql.close();
		}
		
		
		System.out.println("fightmode size:" + ToonCraft.mobFightMode.size());
		if(ToonCraft.mobFightMode != null && ToonCraft.mobFightMode.size() > 0) {
			
			for(LivingEntity cog:ToonCraft.mobFightMode.keySet()) {
				if(!ToonCraft.mobFightMode.get(cog).isEmpty())
					for(Player p:ToonCraft.mobFightMode.get(cog))
						ToonCraft.playerFightMode.remove(p);
				
				cog.damage(100);
				MobInteraction.mobBlockRemoval(cog, null);
			
			}
			ToonCraft.mobFightMode.clear();
		}
			
		/*
		for(int i = 0; i < ToonCraft.mobFightMode.size(); i++) {
			LivingEntity mob = mobFightMode.get(i);
			mob.damage(100);
			MobInteraction.mobBlockRemoval(mob, null);
		}	
		 */
		for(BukkitTask bt: tasks)
			bt.cancel();
		
		for(int i = 0; i < ToonCraft.interactables.size(); i++) {
        	interactables.get(i).damage(100);
		}		
	}
}