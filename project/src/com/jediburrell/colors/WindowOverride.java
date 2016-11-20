package com.jediburrell.colors;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.Window;
import com.frostbyte.neo.exceptions.UnchangableException;
import com.jhlabs.image.GaussianFilter;

public class WindowOverride extends Window{
	
	public WindowOverride(String title) {
		super(title);
		
		frame = new JFrame(this.title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		
		//frame.setOpacity(0f);
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
