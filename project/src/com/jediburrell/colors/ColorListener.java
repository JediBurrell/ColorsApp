package com.jediburrell.colors;

import java.awt.Color;
import java.util.Map;

public abstract class ColorListener {

	public String selected;
	public Map<String, Color> colors;
	
	public void onColorChanged(String name){
		this.selected = name;
	}
	
}
