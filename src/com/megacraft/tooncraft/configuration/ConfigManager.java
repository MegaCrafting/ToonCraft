package com.megacraft.tooncraft.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import com.megacraft.tooncraft.ToonCraft;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

	public static Config defaultConfig;
	public static Config ToonNames;
	
	public ConfigManager() {
		defaultConfig = new Config(new File("config.yml"));
		ToonNames = new Config(new File("tcNames.yml"));
		configCheck();
		nameCheck();
	}

	private static void nameCheck() {
		FileConfiguration Names;
		Names = ToonNames.get();
		
		final List<String> NameList = Arrays.asList("Change me", "Change me too");		
		
		Names.addDefault("ToonCraft.Names.First", NameList);
		Names.addDefault("ToonCraft.Names.Middle", NameList);
		Names.addDefault("ToonCraft.Names.Last", NameList);
				
		ToonNames.save();
	}

	public static void configCheck() {
		FileConfiguration config;
		config = defaultConfig.get();
		//Version
		config.addDefault("Version: ", ToonCraft.plugin.getDescription().getVersion());
		
		config.addDefault("ToonCraft.locations.Tutorial.x", 470);
		config.addDefault("ToonCraft.locations.Tutorial.y", 25);
		config.addDefault("ToonCraft.locations.Tutorial.z", 1025);
		config.addDefault("ToonCraft.locations.Tutorial.World", "RiseWorld");
		
		defaultConfig.save();
	}
}