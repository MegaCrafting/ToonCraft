package com.megacraft.tooncraft.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.megacraft.tooncraft.storage.DBConnection;

public class PlayerManager {
	
	public static void AddRemoveGag(Player player, String GagType, String Level, String amount, String addremove) {
		try {
			PreparedStatement stafflist = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_Gags" + GagType + " WHERE uuid = ?");
			stafflist.setString(1, player.getUniqueId().toString());
			ResultSet rs = stafflist.executeQuery();
			if (!rs.next()) {
				
			}
			String type = Level + "Amount";
			Integer amount1 = 0;
			if(addremove.equalsIgnoreCase("add")){
				amount1 = Integer.parseInt(rs.getString(type)) + Integer.parseInt(amount);
			} else if(addremove.equalsIgnoreCase("remove")){
				amount1 = Integer.parseInt(rs.getString(type)) - Integer.parseInt(amount);
			}
			String amount2 = amount1.toString();
			
			stafflist = DBConnection.sql.getConnection().prepareStatement("UPDATE tc_Gags" + GagType + " SET " + type + " = ? WHERE uuid = ?");
			stafflist.setString(2, player.getUniqueId().toString());
			stafflist.setString(1, amount2);
			stafflist.execute();
		} catch (SQLException c) {
			c.printStackTrace();
		}
	}
	
	public static void NameChecker(Player player) {
		try {
		PreparedStatement updateName = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_playersInfo WHERE uuid = ?");
		updateName.setString(1, player.getUniqueId().toString());

		ResultSet NameRS = updateName.executeQuery();
		
		DBConnection.TCPlayerInfo(player, "Approved", "yes");
		
		String FirstName = NameRS.getString(5);
		String MiddleName = NameRS.getString(6);
		String LastName = NameRS.getString(7);
		
		String Approved = NameRS.getString(8);
									
		String Name = "";
				
		if(Approved.equalsIgnoreCase("no")) {
			Name = "Toon";
		} else if(Approved.equalsIgnoreCase("pending")) {
			Name = "Toon";
		} else if(Approved.equalsIgnoreCase("yes")) {
			Name = FirstName + " " + MiddleName + " " + LastName;
		}
									
		player.setDisplayName(Name);
		player.sendMessage("Your new name is " + Name);
		
		
		updateName = DBConnection.sql.getConnection().prepareStatement("UPDATE tc_playersInfo SET player = ? WHERE uuid = ?");
		updateName.setString(2, player.getUniqueId().toString());
		updateName.setString(1, player.getName());
		updateName.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void AddPlayerToGagTables(Player player, String GagType) {
		try {
			PreparedStatement stafflist = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_Gags" + GagType + " WHERE uuid = ?");
			stafflist.setString(1, player.getUniqueId().toString());
			ResultSet rs = stafflist.executeQuery();
			if (!rs.next()) {
				stafflist = DBConnection.sql.getConnection().prepareStatement("INSERT INTO tc_Gags" + GagType + " (uuid, Level, Experience, L1Amount, L2Amount, L3Amount, L4Amount, L5Amount, L6Amount, L7Amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stafflist.setString(1, player.getUniqueId().toString());
				stafflist.setString(2, "1");
				stafflist.setString(3, "0");
				stafflist.setString(4, "0");
				stafflist.setString(5, "0");
				stafflist.setString(6, "0");
				stafflist.setString(7, "0");
				stafflist.setString(8, "0");
				stafflist.setString(9, "0");
				stafflist.setString(10, "0");
				
				stafflist.execute();
			}
		} catch (SQLException c) {
			c.printStackTrace();
		}
	}

	
}
