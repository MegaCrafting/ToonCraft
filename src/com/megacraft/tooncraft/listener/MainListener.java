package com.megacraft.tooncraft.listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.megacraft.tooncraft.ToonCraft;
import com.megacraft.tooncraft.storage.DBConnection;
import com.megacraft.tooncraft.tutorial.TutorialLoader;
import com.megacraft.tooncraft.utilities.PlayerManager;

public class MainListener implements Listener {

	public ToonCraft plugin;
	
	public MainListener(final ToonCraft plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		new BukkitRunnable() {
			public void run() {				
				if(DBConnection.isOpen == true) {
					try {
						PreparedStatement updateName = DBConnection.sql.getConnection().prepareStatement(DBConnection.Select + "tc_playersInfo WHERE uuid = ?");
						updateName.setString(1, player.getUniqueId().toString());
						ResultSet NameRS = updateName.executeQuery();
						if (!NameRS.next()) {
							if(ToonCraft.InventoryAPIEnabled == true) {
								TutorialLoader.GenderInventory(player);
							} else {
								
							}
						} else {
							PlayerManager.NameChecker(player);
						}
					
						List<String> GagTypes = Arrays.asList("Throw", "Squirt");
						
						for(int i = 0; i < GagTypes.size(); i++) {
							PlayerManager.AddPlayerToGagTables(player, GagTypes.get(i));
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
					
				}
			}
		}.runTaskLater(plugin, 5);
	}
	
}
