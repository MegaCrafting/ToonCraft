package com.megacraft.tooncraft.tutorial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.megacraft.menuapi.Coordinates;
import com.megacraft.menuapi.Menu;
import com.megacraft.menuapi.MenuObject;
import com.megacraft.tooncraft.storage.DBConnection;

public class TutorialLoader {
	public static void GenderInventory(Player player) {
		List<Integer> Places = Arrays.asList(4, 6);
		List<String> Names = Arrays.asList("Boy", "Girl");
		
		Inventory Inv = Bukkit.createInventory(null, 9, ChatColor.RED + "What gender are you?");
		Menu menu = new Menu(Inv);
		
		for (int i = 0; i < Places.size(); i++) {					
			MenuObject menuObject = new MenuObject(Material.DIAMOND, (short) 0, Names.get(i), Arrays.asList(""));
			menu.setMenuObjectAt(new Coordinates(menu, Places.get(i), 1), menuObject);
			
		}
		
		menu.openForPlayer(player);
	}
	
	public static void AnimalInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 9, "What animal do you want to be?");
		Menu menu = new Menu(inventory);
		
		short[] Short = new short[] { 90, 91, 92, 93, 95, 96, 98, 100, 101 };
		List<String> Names = Arrays.asList("§aPig", "§aSheep", "§aCow", "§aChicken", "§aWolf", "§aMooshroom", "§aOcelot", "§aHorse", "§aRabbit");
		int i = 0;
		
		for (short number : Short) {
			MenuObject menuObject = new MenuObject(Material.MONSTER_EGG, number, Names.get(i), Arrays.asList(""));
			menu.setMenuObjectAt(new Coordinates(menu, i+1, 1), menuObject);
			i++;
		}
		
		menu.openForPlayer(player);
	}

    public static void anvilInventory(Player player) {
    	  /* Block b = player.getLocation().getBlock();
    	   b.setType(Material.ANVIL);
    	   
    	   World worldbukkit = b.getLocation().getWorld();  //regular bukkit world
    	            net.minecraft.server.v1_8_R3.World world = ((CraftWorld) worldbukkit).getHandle();
    	            BlockPosition bp = new BlockPosition(b.getX(), b.getY(), b.getZ());
    	            //Now we have the blocks position..

    	            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    	            
    	            ContainerAnvil container = new net.minecraft.server.v1_8_R3.ContainerAnvil(entityPlayer.inventory, world, bp, entityPlayer);
    	                	            
    	            entityPlayer.openContainer(container);*/
   
    	
    
    }

    public static void approveListInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 9, "List of players waiting to be approved: ");
		Menu menu = new Menu(inventory);
		try {
			PreparedStatement approveList = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_playersInfo WHERE Approved = ?");
			approveList.setString(1, "Pending");
			ResultSet rs = approveList.executeQuery();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> Names = Arrays.asList("§aPig", "§aSheep", "§aCow", "§aChicken", "§aWolf", "§aMooshroom", "§aOcelot", "§aHorse", "§aRabbit");

		for (int i = 0; i < Names.size(); i++) {
			
		}
		
		menu.openForPlayer(player);
    }

}