package com.megacraft.tooncraft.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.tutorial.TutorialLoader;
import com.megacraft.tooncraft.utilities.InventoryGUIs;

public class Commands implements Listener {
	
	public static Set<String> invincible = new HashSet<String>();
	public static boolean debugEnabled = false;
	public static boolean isToggledForAll = false;
	
	public Commands(ToonCraft plugin) {
		debugEnabled = ToonCraft.plugin.getConfig().getBoolean("Debug: ");
	}

	public static String[]guialiases = { "gui" };
	public static String[]renamealiases = { "rename" };
	public static String[]approvalaliases = { "approve" };
	
	//Miscellaneous
	public static String[] cmdaliases = new String[] { "/tooncraft", "/tc" };

	@EventHandler(priority = EventPriority.NORMAL)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		final String cmd = event.getMessage().toLowerCase();
		final String[] args = cmd.split("\\s+");
		Player player = event.getPlayer();
		List<String> sendingArgs = Arrays.asList(args).subList(2, args.length);
				
		if (Arrays.asList(cmdaliases).contains(args[0].toLowerCase()) && args.length < 2) 
		{
			
			//HelpCommand.execute(sender, null);
			event.setCancelled(true);
			return;
		}
		
		if(args.length < 2)
			return;
		else {
		}
				
		if (Arrays.asList(cmdaliases).contains(args[0].toLowerCase())) {
			if(Arrays.asList(guialiases).contains(args[1])) {
				TutorialLoader.GenderInventory(player);
				return;
			} else if(Arrays.asList(approvalaliases).contains(args[1])) {
				ApproveCommand.execute(player, sendingArgs);
				return;
			} else if("test".equals(args[1])) {
				ItemStack[] inv = InventoryGUIs.inventoryContents.get(player);
				
				for(int i = 0; i <= inv.length; i++) {
					if(inv[i].equals(null)) {
						
					} else {
						player.getInventory().addItem(inv[i]);
						InventoryGUIs.inventoryContents.remove(player);
					}
				}
				
				return;
			}
		}
	}	
}
