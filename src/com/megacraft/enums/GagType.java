package com.megacraft.enums;

public enum GagType {
	 
	SQUIRT(6,"Squirt", "Squirt is a Gag Track that has a high accuracy and is a primary gag. Squirt gags are the sixth gag used in a Cog battle. Each gag (except Geyser) is only able to target one Cog. "),
	THROW(5,"Throw","Throw is a Gag Track that has a medium accuracy and is a primary gag. This gag is the fifth gag used in a Cog battle. Each gag (except level 7) is only able to target one Cog at a time.  When your Throw is maxed, the experience bar will be replaced by a '500 to go' mark; earning those 500 experience points will reward you the Wedding Cake. If you already have the wedding cake, and you finish the 500 Exp mark, you will still keep the wedding cake until you use your current one. "),
	SOUND(4,"Sound","Sound is a gag track that has a high accuracy and is available once a Toon finishes the ToonTasks in Toontown Central. Later as you progress through the game, it is available again when a Toon finishes the ToonTasks in Minnie's Melodyland, then a final time in The Brrrgh. This gag is the fourth gag used in a Cog battle. This gag track is the only track to have all gags target all Cogs in a battle. Sound is mainly used in conjunction with other toons to deal devastating damage."),
	LURE(3,"Lure","Lure is a gag track that has a low accuracy and is available once a Toon finishes the ToonTasks when he chooses to learn Lure. These gags are the third gag track used in a Cog battle.  When a Lure gag is used, the Cogs will be stunned for a set amount of rounds. During this period of time, the lured Cogs will be unable to attack. If a Cog is in a lured state, any Throw or Squirt gags targeting the Cog will receive a 1.5x (50%) damage bonus. While Sound, Throw and Squirt gags are all guaranteed to hit lured Cogs, Drop gags are guaranteed to miss. "),
	TRAP(2,"Trap","Trap is a Gag Track that has perfect accuracy. However, Lure is required for it to work. First, a Trap Gag is placed on the ground in front of the targeted Cog. Then, Lure must be used to bring the Cog into the Trap. Trap is second in the Gag lineup during a battle round, following Toon-Up and preceding Lure. Toons cannot place Trap Gags on Cogs that have already been Lured. Trap Gags increase the accuracy of Lure Gags when used in the same round on the same Cog(s). A Toon can obtain this Gag Track from ToonTasks. It is first available in Minnie's Melodyland, and if not chosen, is then available in The Brrrgh. "),
	TOONUP(1,"Toon-up","Toon-Up is a gag track that has medium accuracy and becomes available once a Toon finishes the ToonTasks in Toontown Central, Daisy Gardens, Minnie's Melodyland, or The Brrrgh if not chosen in the previous playgrounds. Toon-Up gags are the first gags that are used by Toons in a Cog battle. Toon-Up gags heal the targeted Toon(s) by the Toon-Up amount set for each gag. If a Toon-Up gag heals multiple Toons at the same time, the value is split evenly between them. Unlike other tracks, if a Toon-Up gag misses, it will not completely fail to heal the targeted Toons. Instead, the gag will only heal 20% of the original value, rounded up. "),
	DROP(0,"Drop","Drop is a Gag Track that has a low accuracy and is available once a Toon finishes the ToonTasks in Donald's Dock. If chosen here, Drop would be that Toon's fourth Gag Track. This Gag is the final Gag used in a Cog battle. It can target only one cog, except for Toontanic, which targets all cogs. ");
	
	
	
	
	
	private String name;
	private String descr;
    private String perm;
    private int row;
    
	GagType(int row, String name,String desc){
	        this.setName(name);
	        this.setDescr(desc);
	        
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

		
}

	