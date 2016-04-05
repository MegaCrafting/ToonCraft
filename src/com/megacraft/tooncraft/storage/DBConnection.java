package com.megacraft.tooncraft.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.megacraft.enums.GagType;
import com.megacraft.gags.Gag;
import com.megacraft.tooncraft.TCPlayer;
import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.playerGagData.PlayerGagData;

public class DBConnection {

	public static String Select = "SELECT * FROM ";
	public static String Delete = "DELETE FROM ";
	public static String Insert = "INSERT INTO ";
	
	public static Database sql = new SQLite(ToonCraft.log, "Establishing SQLite Connection.", "tooncraft.db", ToonCraft.plugin.getDataFolder().getAbsolutePath());
	
	public static String host;
	public static int port;
	public static String db;
	public static String user;
	public static String pass;
	public static boolean isOpen = false;

	public static void init() {
		if (((SQLite) sql).open() == null) {
			ToonCraft.log.severe("Disabling due to database error");
			return;
		}

		isOpen = true;
		if (!sql.tableExists("tc_playersInfo")) {
			System.out.println("Creating tc_playersInfo table.");
			String query = "CREATE TABLE `tc_playersInfo` ("
					+ "`player` TEXT(36),"
					+ "`uuid` TEXT(36) PRIMARY KEY,"
					+ "`Gender` TEXT(4),"
					+ "`Animal` TEXT(16),"
					+ "`FirstName` TEXT(50),"
					+ "`MiddleName` TEXT(50),"
					+ "`LastName` TEXT(50),"
					+ "`Approved` TEXT(10));";
			sql.modifyQuery(query);
		} 
		
		if (!sql.tableExists("tc_player_gags"))
		{
			System.out.println("Creating tc_player_gags table.");
			String query = "CREATE TABLE `tc_player_gags` ("
					+ "`uuid` TEXT(36) PRIMARY KEY,"
					+ "`Level` TEXT(4),"
					+ "`Experience` TEXT(10),"
					+ "`squirtGags` TEXT(36),"
					+ "`throwGags` TEXT(36),"
					+ "`soundGags` TEXT(36),"
					+ "`lureGags` TEXT(36),"
					+ "`trapGags` TEXT(36),"
					+ "`toonupGags` TEXT(36),"
					+ "`dropGags` TEXT(36));";
			sql.modifyQuery(query);
		}
		if (!sql.tableExists("tc_GagsThrow")) {
			System.out.println("Creating tc_GagsThrow table.");
			String query = "CREATE TABLE `tc_GagsThrow` ("
					+ "`uuid` TEXT(36) PRIMARY KEY,"
					+ "`Level` TEXT(4),"
					+ "`Experience` TEXT(10),"
					+ "`L1Amount` TEXT(4),"
					+ "`L2Amount` TEXT(4),"
					+ "`L3Amount` TEXT(4),"
					+ "`L4Amount` TEXT(4),"
					+ "`L5Amount` TEXT(4),"
					+ "`L6Amount` TEXT(4),"
					+ "`L7Amount` TEXT(4));";
			sql.modifyQuery(query);
		}
		if (!sql.tableExists("tc_GagsSquirt")) {
			System.out.println("Creating tc_GagsSquirt table.");
			String query = "CREATE TABLE `tc_GagsSquirt` ("
					+ "`uuid` TEXT(36) PRIMARY KEY,"
					+ "`Level` TEXT(4),"
					+ "`Experience` TEXT(10),"
					+ "`L1Amount` TEXT(4),"
					+ "`L2Amount` TEXT(4),"
					+ "`L3Amount` TEXT(4),"
					+ "`L4Amount` TEXT(4),"
					+ "`L5Amount` TEXT(4),"
					+ "`L6Amount` TEXT(4),"
					+ "`L7Amount` TEXT(4));";
			sql.modifyQuery(query);
		}
		
	}

	//load player gag info from database, and populate the data.
	public static void getPlayerGags(TCPlayer tc) {
		
		
		try {
			String squirtGags = "1~5~0,0~0~0,0~0~0,0~0~0,0~0~0,0~0~0,0~0~0";
			String otherGags = "0~0~0,0~0~0,0~0~0,0~0~0,0~0~0,0~0~0,0~0~0";
			
			String sql = "SELECT * FROM tc_player_gags WHERE uuid ='" + tc.getPlayer().getUniqueId().toString() + "';'";
			ResultSet rs = DBConnection.sql.readQuery(sql);
			if(!rs.next())
			{
				System.out.println("New Player added to table!");
				tc.getGagLevels().put(GagType.SQUIRT, new PlayerGagData("squirtGags", squirtGags));
				tc.getGagLevels().put(GagType.THROW, new PlayerGagData("throwGags", squirtGags));
				tc.getGagLevels().put(GagType.TRAP, new PlayerGagData("trapGags", otherGags));
				tc.getGagLevels().put(GagType.TOONUP, new PlayerGagData("toonupGags", otherGags));
				tc.getGagLevels().put(GagType.SOUND, new PlayerGagData("soundGags", otherGags));
				tc.getGagLevels().put(GagType.LURE, new PlayerGagData("lureGags", otherGags));
				tc.getGagLevels().put(GagType.DROP, new PlayerGagData("dropGags", otherGags));
				
				
				
				squirtGags = "'" + squirtGags + "'";
				otherGags = "'" + otherGags + "'";
				
				//note i typoed "throwGags" in the database table :\
				sql = "INSERT INTO tc_player_gags (uuid,level,experience, squirtGags, throwGags, soundGags, lureGags,trapGags,toonupGags,dropGags) VALUES ('" +
						tc.getPlayer().getUniqueId().toString() + "'," + tc.getLevel() + "," + tc.getExp() + ","  + squirtGags + "," +
						squirtGags + "," + otherGags + "," + otherGags + "," + otherGags + "," + otherGags + "," + otherGags + ");";
				
				
				DBConnection.sql.modifyQuery(sql);
				
				
			} else {
					tc.getGagLevels().put(GagType.SQUIRT, new PlayerGagData("squirtGags", rs.getString("squirtGags")));
					tc.getGagLevels().put(GagType.THROW, new PlayerGagData("throwGags", rs.getString("throwGags")));
					tc.getGagLevels().put(GagType.TRAP, new PlayerGagData("trapGags", rs.getString("trapGags")));
					tc.getGagLevels().put(GagType.TOONUP, new PlayerGagData("toonupGags", rs.getString("toonupGags")));
					tc.getGagLevels().put(GagType.SOUND, new PlayerGagData("soundGags", rs.getString("soundGags")));
					tc.getGagLevels().put(GagType.LURE, new PlayerGagData("lureGags", rs.getString("lureGags")));
					tc.getGagLevels().put(GagType.DROP, new PlayerGagData("dropGags", rs.getString("dropGags")));
				
			}
		
		
		} catch (SQLException c) {
			c.printStackTrace();
		}
		
	}
		
		
	public static void TCPlayerInfo(Player player, String type, String string) {
		try {
			PreparedStatement stafflist = DBConnection.sql.getConnection().prepareStatement(Select + "tc_playersInfo WHERE uuid = ?");
			stafflist.setString(1, player.getUniqueId().toString());
			ResultSet rs = stafflist.executeQuery();
			if (!rs.next()) {
				stafflist = DBConnection.sql.getConnection().prepareStatement("INSERT INTO tc_playersInfo (player, uuid, Gender, Animal, FirstName, MiddleName, LastName, Approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				stafflist.setString(1, player.getName());
				stafflist.setString(2, player.getUniqueId().toString());
				stafflist.setString(3, string);
				stafflist.setString(4, "");
				stafflist.setString(5, "");
				stafflist.setString(6, "");
				stafflist.setString(7, "");
				stafflist.setString(8, "Pending");
				
				
				stafflist.execute();
			}
			
			stafflist = DBConnection.sql.getConnection().prepareStatement(Select + "tc_playersInfo WHERE uuid = ?");
			stafflist.setString(1, player.getUniqueId().toString());
			rs = stafflist.executeQuery();
			
			if(rs.next()) {
				stafflist = DBConnection.sql.getConnection().prepareStatement("UPDATE tc_playersInfo SET " + type + " = ? WHERE uuid = ?");
				stafflist.setString(2, player.getUniqueId().toString());
				
				stafflist.setString(1, string);
				
				stafflist.execute();
			}
			
		} catch (SQLException c) {
			c.printStackTrace();
		}
	}
	
	public static void TCApproved(Player player, String name, String approvalType, String approved) {
		
		
		try {
			PreparedStatement stafflist = DBConnection.sql.getConnection().prepareStatement(Select + "tc_playersInfo WHERE player = ?");
			stafflist.setString(1, name);
			ResultSet rs = stafflist.executeQuery();
			
			if(approvalType.equalsIgnoreCase("approved")) {
				stafflist = DBConnection.sql.getConnection().prepareStatement("UPDATE tc_playersInfo SET approved = ? WHERE player = ?");
				stafflist.setString(2, name);
				
				stafflist.setString(1, approved);
				
				stafflist.execute();
			} else if(approvalType.equalsIgnoreCase("list")) {
				stafflist = DBConnection.sql.getConnection().prepareStatement(Select + "tc_playersInfo WHERE Approved = ?");
				stafflist.setString(1, "Pending");
				rs = stafflist.executeQuery();
				if(rs.next()) {
					player.sendMessage("Player " + rs.getString(1) + " needs approving.");
				}
				
			} else if(approvalType.equalsIgnoreCase("show")) {
				stafflist = DBConnection.sql.getConnection().prepareStatement(Select + "tc_playersInfo WHERE player = ?");
				stafflist.setString(1, name);
				rs = stafflist.executeQuery();
				
				if(rs.next()) {
					player.sendMessage("Player " + name + " is using " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " as their Toon name.");
					
					if(rs.getString(8).equalsIgnoreCase("Pending")) {
						player.sendMessage("To approve " + name + "'s name please type /tooncraft approve " + name + "Yes or No.");
					} else if(rs.getString(8).equalsIgnoreCase("No")) {
						player.sendMessage("This players name has been denied by a member of staff and is waiting to be changed.");
					} else if(rs.getString(8).equalsIgnoreCase("Yes")) {
						player.sendMessage("This players name has been approved by a member of staff and does not need any further action.");
					}
				}
					
			}
				
		} catch (SQLException c) {
			c.printStackTrace();
		}
		
	}
	
	public static boolean isOpen() {
		return isOpen;
	}

	public static void updatePlayerData(TCPlayer tcp) {
		
			Gag gag = tcp.getSelGag();
			System.out.println("Updating Player Stats!");
			PlayerGagData gd = tcp.getGagLevels().get(gag.getType());
			
			String sql = "Update tc_player_gags SET " + gd.getColumnId() + "='" + gd.getGagString() + "' WHERE uuid='" + tcp.getPlayer().getUniqueId().toString() + "';";
			System.out.println("SQL: " + sql);
			DBConnection.sql.modifyQuery(sql);
		
	}
}
