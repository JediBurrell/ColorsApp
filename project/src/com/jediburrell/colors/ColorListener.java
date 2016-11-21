package com.jediburrell.colors;

public abstract class ColorListener {

	public String selected;
	
	public void onColorChanged(String name){
		this.selected = name;
	}
	
}
