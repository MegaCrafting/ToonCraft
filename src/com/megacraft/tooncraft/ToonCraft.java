package com.megacraft.tooncraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.megacraft.tooncraft.commands.Commands;
import com.megacraft.tooncraft.configuration.ConfigManager;
import com.megacraft.tooncraft.listener.MainListener;
import com.megacraft.tooncraft.listener.MobInteraction;
import com.megacraft.tooncraft.storage.DBConnection;
import com.megacraft.tooncraft.tutorial.TutorialListener;
import com.megacraft.tooncraft.utilities.InventoryGUIs;

public class ToonCraft extends org.bukkit.plugin.java.JavaPlugin {
	
	public static ToonCraft plugin;
	public static Logger log;
    
    public static Boolean InventoryAPIEnabled = true;
	public static List<LivingEntity> interactables = new ArrayList<LivingEntity>();
	
	public static List<LivingEntity> mobFightMode = new ArrayList<LivingEntity>();
	public static List<Player> playerFightMode = new ArrayList<Player>();
    
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
		
	}
	
	@Override
	public void onDisable() {
		if (DBConnection.isOpen != false) {
			DBConnection.sql.close();
		}
		
		for(int i = 0; i < ToonCraft.mobFightMode.size(); i++) {
			LivingEntity mob = mobFightMode.get(i);
			mob.damage(100);
			MobInteraction.mobBlockRemoval(mob, null);
		}	

		for(int i = 0; i < ToonCraft.interactables.size(); i++) {
        	interactables.get(i).damage(100);
		}		
	}
}