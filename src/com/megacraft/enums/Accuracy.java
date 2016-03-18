package com.megacraft.enums;

public enum Accuracy {
 
	LOW (50),
	MED (75),
	HIGH(95),
	Perfect(100);
	
    private int value;
    
    Accuracy(int value){
        this.setValue(value);
      }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}