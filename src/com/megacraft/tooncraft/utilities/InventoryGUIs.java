package com.megacraft.tooncraft.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.megacraft.menuapi.Coordinates;
import com.megacraft.menuapi.Menu;
import com.megacraft.menuapi.MenuObject;
import com.megacraft.tooncraft.storage.DBConnection;

public class InventoryGUIs {
	
	public static HashMap<Player, ItemStack[]> inventoryContents = new HashMap<Player, ItemStack[]>();
	
	public static void GagsInventory(Player player) {
		List<String> Gags = Arrays.asList("Throw", "Squirt");
		Inventory Inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Please select a gag to use!");
		Menu menu = new Menu(Inv);		
		
		for (int i = 0; i < Gags.size(); i++) {
			PreparedStatement stafflist;
			try {
				stafflist = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_Gags" + Gags.get(i) + " WHERE uuid = ?");
				stafflist.setString(1, player.getUniqueId().toString());
				ResultSet rs = stafflist.executeQuery();		
				
				inventoryContents.put(player, player.getInventory().getContents());
				player.getInventory().clear();
							
				for (int i1 = 1; i1 <= 7; i1++) {					
					ItemStack itemStack = new ItemStack(Material.EGG, rs.getInt("L"+String.valueOf(i1)+"Amount"));
					ItemMeta itemMeta = itemStack.getItemMeta(); // You must declare a separate variable for the meta for this to work. i.e. You can NOT just use: itemStack.getItemMeta().setDisplayName("§aWool");
					itemMeta.setDisplayName(Gags.get(i) + " Level " + String.valueOf(i1)); // Using §-codes instead of ChatColors is a personal preference of mine.
					itemStack.setItemMeta(itemMeta); // Make sure you set the ItemMeta for things to work.
					MenuObject menuObject = new MenuObject(itemStack);
					menu.setMenuObjectAt(new Coordinates(menu, i1, i+1), menuObject);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		menu.openForPlayer(player);
	}
}
