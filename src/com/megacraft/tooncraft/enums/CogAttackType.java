package com.megacraft.tooncraft.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CogAttackType {

	CLIP_ON_TIE("Clip on Tie", new String[] {"Better dress for our meeting.","No tie, no service","This is going to choke you up.","You should dress for success.","Do you need help putting this on?"}),
	SHRED("Shred", new String[] {    "Easy come, easy go.", "This will get rid of evidence.", "This should cut you down to size.", "See if you can put this back together.",  "We don't want this to fall in the wrong hands."} ),
	
	;
	
	
	private String name;
	private String[] phrases = new String[5];
    
    
    

    
	CogAttackType(String name, String[] phrases){
	        this.setName(name);
	        this.setPhrases(phrases);
	        
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String[] getPhrases() {
		return phrases;
	}



	public void setPhrases(String[] phrases) {
		this.phrases = phrases;
	}



	

	
}
