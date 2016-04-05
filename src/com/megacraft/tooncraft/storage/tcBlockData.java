package com.megacraft.tooncraft.storage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public class tcBlockData {

	private Block block;
	private Material type;
	private BlockState state;
	
	
	
	public tcBlockData(Block b) {
		this.block = b;
		this.type = b.getType();
		this.state = b.getState();
		
		
		
	}

	public void restore()
	{
		this.block.setType(this.type);
		this.block.getState().setData(this.state.getData());
		
	
	}
}
