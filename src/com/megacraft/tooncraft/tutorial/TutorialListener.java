package com.megacraft.tooncraft.tutorial;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.megacraft.menuapi.page.Page;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.configuration.Config;
import com.megacraft.tooncraft.storage.DBConnection;
import com.megacraft.tooncraft.utilities.ChatLoader;
import com.megacraft.tooncraft.utilities.PlayerManager;

public class TutorialListener implements Listener {
	
	public ToonCraft plugin;

	static FileConfiguration config = ToonCraft.plugin.getConfig();
	Config ToonNames = new Config(new File("tcNames.yml"));
		
	FileConfiguration Names = ToonNames.get();
	
	List<String> FirstNames = Names.getStringList("ToonCraft.Names.First");
	List<String> MiddleNames = Names.getStringList("ToonCraft.Names.Middle");
	List<String> LastNames = Names.getStringList("ToonCraft.Names.Last");
	
	List<Integer> ExtraInvSlot = Arrays.asList(4, 6);
	List<String> ExtraItems = Arrays.asList("Anvil", "Map");
	List<String> ExtraNames = Arrays.asList("Custom name", "Random name");
	List<String> InvName = Arrays.asList("What gender are you?", "What animal do you want to be?", "Choose your first name:", "Choose your middle name:", "Choose your last name:");

	List<Byte> Ext = Arrays.asList();
    	
	public TutorialListener(final ToonCraft plugin) {
		this.plugin = plugin;
	}
	
	int CurrentPage = 0;	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		if(event.getCurrentItem() != null) {
			if(event.getCurrentItem().getType() != Material.AIR) {
				
				String Inv = ChatColor.stripColor(event.getInventory().getName());
				String Item = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				if(InvName.contains(Inv)) {	
					if(Item == "Next") {
						CurrentPage = CurrentPage+1;
						player.closeInventory();			
						if(Inv == "Choose your first name:") {
							Page.NameList(player, FirstNames, "Choose your first name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Inv == "Choose your middle name:") {
							Page.NameList(player, MiddleNames, "Choose your middle name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Inv == "Choose your last name:") {
							Page.NameList(player, LastNames, "Choose your last name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						}
					} else if(Item == "Previous") {
						CurrentPage = CurrentPage-1;
						player.closeInventory();
						if(Inv == "Choose your first name:") {
							Page.NameList(player, FirstNames, "Choose your first name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Inv == "Choose your middle name:") {
							Page.NameList(player, MiddleNames, "Choose your middle name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Inv == "Choose your last name:") {
							Page.NameList(player, LastNames, "Choose your last name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						}
					//} else if(Item == "Custom name") {
						//InventoryLoader.openAnvilInventory(player);
					} else {
						
						if(Item == "Random name") {
							Random randomGenerator = new Random();
							int randomInt = 0;
							
							if(Inv == "Choose your first name:") {
								randomInt = randomGenerator.nextInt(FirstNames.size());
								Item = FirstNames.get(randomInt);
							} else if(Inv == "Choose your middle name:") {
								randomInt = randomGenerator.nextInt(MiddleNames.size());
								Item = MiddleNames.get(randomInt);
							} else if(Inv == "Choose your last name:") {
								randomInt = randomGenerator.nextInt(LastNames.size());
								Item = LastNames.get(randomInt);
							}
						}
						
						player.closeInventory();
						Integer Position = InvName.indexOf(Inv);

						if(Inv.toLowerCase().contains("gender")) {
							DBConnection.TCPlayerInfo(player, "Gender", Item);
						} else if(Inv.toLowerCase().contains("animal")) {
							DBConnection.TCPlayerInfo(player, "Animal", Item);
						} else if(Inv.toLowerCase().contains("first name")) {
							DBConnection.TCPlayerInfo(player, "FirstName", Item);
						} else if(Inv.toLowerCase().contains("middle name")) {
							DBConnection.TCPlayerInfo(player, "MiddleName", Item);
						} else if(Inv.toLowerCase().contains("last name")) {
							DBConnection.TCPlayerInfo(player, "LastName", Item);
						}
						
						if(InvName.get(Position) == InvName.get(InvName.size()-1)) {
							ChatLoader.TutorialChat(player);
							PlayerManager.AddRemoveGag(player, "Throw", "L1", "1", "add");
							PlayerManager.AddRemoveGag(player, "Squirt", "L1", "1", "add");
							return;
						}
						
					    String Run = InvName.get(Position+1);
					    
						if(Run == "What animal do you want to be?") {
							TutorialLoader.AnimalInventory(player);
						} else if(Run == "Choose your first name:") {							
							Page.NameList(player, FirstNames, "Choose your first name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Run == "Choose your middle name:") {
							Page.NameList(player, MiddleNames, "Choose your middle name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						} else if(Run == "Choose your last name:") {							
							Page.NameList(player, LastNames, "Choose your last name:", CurrentPage, ExtraItems, ExtraNames, ExtraInvSlot);
						}
					}
				} else if(ChatColor.stripColor(event.getInventory().getName()) == "Custom name") {
					event.setCancelled(true);
				}		
			}
		} 
	}
}
