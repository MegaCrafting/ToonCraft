package com.megacraft.tooncraft.playerGagData;

public class PlayerGagData {
	
	private int[] unlocked = new int[8];
	private int[] playerAmmo = new int[8];
	private int[] xpEarned = new int[8];
	private String columnId ="";

	public PlayerGagData(String col, String gagString) {
		this.setColumnId(col);
		parseGagString(gagString);
	}

	public static boolean isUnlocked(PlayerGagData pd, int level)
	{
		if(pd.unlocked[level] == 1)
			return true;
		return false;
	}
	public int plrAmmo(int level)
	{
		return this.playerAmmo[level];
	}

	public int getHighestTrack()
	{
		int highestUnlocked = 0;
		for(int i = 0; i < 7 ; i++)
		{
			if(unlocked[i] == 1)
				highestUnlocked ++;
		}
		
		
		return highestUnlocked;
	}
	public void parseGagString(String str)
	{
		
		System.out.println("GAG String: " + str);
			String[] gagSplit = str.split(",");
			int lvl = 1;
			
			
			for(String s:gagSplit)
			{
				System.out.println("GagLine: " + s);
				String[] lvlAmmo = s.split("~");
				
				this.unlocked[lvl] = Integer.parseInt(lvlAmmo[0]);
				this.playerAmmo[lvl] = Integer.parseInt(lvlAmmo[1]);
				this.getXpEarned()[lvl] = Integer.parseInt(lvlAmmo[2]);
				lvl++;
				if(lvl >=8)
					break;
					
				
					
					
		//		System.out.println("Updating " + this.gagType + " Level: " + lvl + " unlocked " + lvlAmmo[0] + " ammo " + lvlAmmo[1] + " string was ");
				
			}
			
		

	}
	public String getGagString() {
		String retVal = "";
		for(int i = 0; i < 8;i++)
		{
			retVal = retVal + i + "~" + this.playerAmmo[i] + "~" + this.xpEarned[i];
			if(i < 7)
				retVal = retVal + ",";
		}
		System.out.println("Gag String Rebuilt : " + retVal);
		return retVal;
	}
	public int[] getXpEarned() {
		return xpEarned;
	}

	public void setXpEarned(int[] xpEarned) {
		this.xpEarned = xpEarned;
	}

	public void updateXp(int level) {
		
		this.xpEarned[0] = this.xpEarned[0] + level;
		//xp for the entire gag track is stored in slot 0 of the array.
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}




}
