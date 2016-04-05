package com.megacraft.tooncraft.enums;

public enum CogCorp {

	SELL_BOT ("Sellbot", new CogType[] {CogType.COLD_CALLER,CogType.GLAD_HANDLER,CogType.HOLLYWOOD, CogType.MINGLER,CogType.NAME_DROPPER,CogType.TELEMARKETER,CogType.TWO_FACE}),
	CASH_BOT ("Cashbot", new CogType[] {}),
	LAW_BOT ("Lawbot", new CogType[] {}),
	BOSS_BOT("Bossbot", new CogType[] {CogType.BIGCHEESE,CogType.FLUNKY,CogType.CORPRAIDER,CogType.HEADHUNTER,CogType.MICROMAN,CogType.PPUSHER,CogType.DOWNSISER});
	
	
	
	
	private String name;
	private CogType[] variant;
	 CogCorp(String name, CogType[] variant){
	        this.setName(name);
	        
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CogType[] getVariant() {
		return variant;
	}

	public void setVariant(CogType[] variant) {
		this.variant = variant;
	}
}


