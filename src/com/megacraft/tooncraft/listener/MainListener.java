package com.megacraft.tooncraft.listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;




import net.minecraft.server.v1_9_R1.Material;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
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
	public void onCogDamage(EntityDamageEvent event) {
		
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity e = (LivingEntity) event.getEntity();
		
			if(ToonCraft.interactables.contains(e))
			{
				String[] name = e.getCustomName().split("\\(");
				long health = Math.round(e.getHealth() * 100) / 100;
				
				
				e.setCustomName(name[0] + "(" + health + ")");
			}
		}
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
