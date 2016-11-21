package com.jediburrell.colors;

import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.Window;
import com.frostbyte.neo.exceptions.UnchangableException;

public class WindowOverride extends Window{
	
	public WindowOverride(String title) {
		super(title);
		
		frame = new JFrame(this.title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
	}
	
	@Override
	public Window setSize(int width, int height) {
		frame.setShape(new RoundRectangle2D.Double(0, 0, width, height, 10, 10));
		
		return super.setSize(width, height);
	}
	
	public void setPosition(int x, int y){
	
		
		frame.setLocation(x, y);
		
	}
	
	public JFrame getFrame(){
		
		return frame;
	}
	
	@Override
	public Window setContent(Neo neo) throws UnchangableException{
		if(this.neo == null)
			this.neo = neo;
		else
			throw new UnchangableException();
		
		frame.add(neo);
		
		return this;
	}

}
