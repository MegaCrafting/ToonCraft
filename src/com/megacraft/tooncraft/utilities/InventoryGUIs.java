package com.megacraft.tooncraft.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.menuapi.Coordinates;
import com.megacraft.menuapi.Menu;
import com.megacraft.menuapi.MenuObject;
import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.playerGagData.PlayerGagData;
import com.megacraft.tooncraft.storage.DBConnection;

public class InventoryGUIs {
	 
	
	public static HashMap<Player, ItemStack[]> inventoryContents = new HashMap<Player, ItemStack[]>();
	
	public static Menu getGagsInventory(Player player) {
		

		TCPlayer tcp = TCPlayer.findTCP(player);
		Inventory Inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Please select a gag to use!");
		Menu menu = new Menu(Inv);		
		
		inventoryContents.put(player, player.getInventory().getContents());
		player.getInventory().clear();
	
		for(GagType gt:ToonCraft.loadedGags.keySet())
		{
//			System.out.println("Loading Gag Types: " + gt.name());
			
			
			for(Gag gag:ToonCraft.loadedGags.get(gt))
			{
	//			System.out.println("Reading in gag: " + gag.getName());
				
				
				PlayerGagData pd = tcp.getGagLevels().get(gt);
				int xpEarned = pd.getXpEarned()[0];
				ItemStack itemStack = new ItemStack(Material.EGG , pd.plrAmmo(gag.getLevel()));  //Figure out a way to save/load player ammo.
				
				ItemMeta itemMeta = itemStack.getItemMeta(); // You must declare a separate variable for the meta for this to work. i.e. You can NOT just use: itemStack.getItemMeta().setDisplayName("§aWool");
				
				itemMeta.setDisplayName(gag.getName() + " Level " + gag.getLevel()); // Using §-codes instead of ChatColors is a personal preference of mine.
				List<String> lore = gag.getLore();
				if(xpEarned < gag.getExpReq())
				{
					lore.clear();
					int diff = gag.getExpReq() - xpEarned;
					lore.add(ChatColor.RED + "" + diff + " xp required to unlock.");
				}
				
				itemMeta.setLore(lore); 
				itemStack.setItemMeta(itemMeta); // Make sure you set the ItemMeta for things to work.
				MenuObject menuObject = new MenuObject(itemStack);
				menu.setMenuObjectAt(new Coordinates(menu, gag.getLevel(), gt.getRow()+1), menuObject);
				
				
			}
			
		}
		
	
		return menu;
	}
	
	
	public static void GagsInventory(Player player) {
		List<String> Gags = Arrays.asList("Throw", "Squirt");
		Inventory Inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Please select a gag to use!");
		Menu menu = new Menu(Inv);		
		
		inventoryContents.put(player, player.getInventory().getContents());
		player.getInventory().clear();
		
		for (int i = 0; i < Gags.size(); i++) {
			PreparedStatement stafflist;
			try {
				stafflist = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_Gags" + Gags.get(i) + " WHERE uuid = ?");
				stafflist.setString(1, player.getUniqueId().toString());
				ResultSet rs = stafflist.executeQuery();		
											
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


	public static ItemStack makeIcon(int id, int stack_size, short item_data) {   //creates an item stack with all the meta bullshit stripped off
		ItemStack is = new ItemStack(id, stack_size, (short) item_data );
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}
	
	public static List<String> formatLore(String text, int size, org.bukkit.ChatColor color) {
	    List<String> ret = new ArrayList<String>();

	    
	    String[] words = text.split(" ");
	    String rebuild = "";
	    
	    int lastAdded = 0;
	    for(int i = 0; i < words.length; i++)
	    {
	    	int wordLen = words[i].length();
	    	if(rebuild.length() + wordLen > 40)
	    	{
	    		lastAdded = i;
	    		ret.add(color + rebuild);
	    		rebuild = "";
	    		
	    	} 
	    		rebuild = rebuild + " " + words[i];
	    	
	    	
	    	
	    }
	    if(!rebuild.equalsIgnoreCase(""))
    		ret.add(color + rebuild);
	    
	    
	    	
	    return ret;
	}	

}
