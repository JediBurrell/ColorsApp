package com.jediburrell.colors.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.text.html.HTMLDocument.Iterator;

import jto.colorscheme.ColorSet;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.framework.BufferedImageLoader;
import com.frostbyte.neo.framework.FileManager;
import com.frostbyte.neo.gui.TextObject;
import com.frostbyte.neo.objects.GameObject;
import com.frostbyte.neo.scene.Scene;
import com.jediburrell.colors.*;
import com.jediburrell.colors.objects.*;

public class PaletteScene extends Scene{

	private Neo neo;
	private WindowOverride window;
	private float opacity = 0f;
	
	private static int ICON_SIZE = 23;
	private static int PADDING = 10;
	
	private ColorScheme colors;
	
	private ColorListener listener;
	private Map<String, Map<String, java.awt.Color>> map;
	
	private TextObject text;
	
	private float scrollY = 0;
	private int maxScroll = 0;
	
	private int hover = 0;
	private float velocity = 0;
	
	public PaletteScene(Neo neo, WindowOverride window) {
		super(neo);
		
		this.neo = neo;
		this.window = window;
		colors = new ColorScheme(FileManager.getAppdata()+"/.colorsapp/material.ase");
		map = colors.getMap();
		
		listener = new ColorListener() {
		};
		
		onLoad();
	}
	
	@Override
	public void onLoad() {

		ActionableIconObject close = null;
		ActionableIconObject minimize = null;
		
		BufferedImageLoader iLoader = new BufferedImageLoader();
		try {
			close = new ActionableIconObject(
					neo.width()-ICON_SIZE-PADDING, PADDING, ICON_SIZE, iLoader.loadImage("/ic_close_black_24dp.png"),
					new Action() {
						
						@Override
						public void perform() {
							
							System.exit(0);
							
						}
					});
			close.setHoverImage(iLoader.loadImage("/ic_close_black_24dp_hover.png"));
			close.setClickImage(iLoader.loadImage("/ic_close_black_24dp_click.png"));
			
			minimize = new ActionableIconObject(
					neo.width()-ICON_SIZE*2-PADDING*2, PADDING, ICON_SIZE, iLoader.loadImage("/ic_minimize_black_24dp.png"),
					new Action() {
						
						@Override
						public void perform() {
							
							window.getFrame().setState(JFrame.ICONIFIED);
							
						}
					});
			minimize.setHoverImage(iLoader.loadImage("/ic_minimize_black_24dp_hover.png"));
			minimize.setClickImage(iLoader.loadImage("/ic_minimize_black_24dp_click.png"));
		} catch (IOException e) {
		}
		
		text = new TextObject(0, 0);
		text.setText("ColorsApp");
		text.setFontColor(new Color(32, 32, 32));
		text.setFont(Font.decode(Font.DIALOG_INPUT));
		text.setFontSize(15);
		text.setX(("ColorsApp").length()*6);
		text.setY(PADDING*2+1);
		
		listener.selected = map.keySet().iterator().next();
		
		float posY = PADDING*2+ICON_SIZE+PADDING*2;
		
		boolean first = true;
		boolean second = false;
		
		for(Map.Entry<String, Map<String, Color>> entry : map.entrySet()){
			if(first){
				first = false;
				second = true;
				continue;
			}
			
			String key = entry.getKey();
			Map<String, Color> value = entry.getValue();
			
			java.util.Iterator<Entry<String, Color>> it = value.entrySet().iterator();
			Map.Entry<String, Color> ent = it.next();
			String name = ent.getKey();
			Color val = ent.getValue();
			
			for(int i = 0; i < name.length(); i++){
				try{
					Integer.parseInt(name.substring(i, i+1));
				}catch(Exception e){
					continue;
				}
				
				if(value.containsKey(name.substring(0, i)+"500")){
					name = name.substring(0, i)+"500";
					val = value.get(name.substring(0, i)+"500");
				}
				
				break;
			}
			
			if(!name.contains("500")){
				for(int i = 0; i < name.length(); i++){
					try{
						Integer.parseInt(name.substring(i, i+1));
					}catch(Exception e){
						continue;
					}
					
					if(value.containsKey(name.substring(0, i)+"400")){
						name = name.substring(0, i)+"400";
						val = value.get(name.substring(0, i)+"400");
					}
					
					break;
				}
			}
			
			if(!name.contains("500")&&!name.contains("400")&&name.contains("50")){
				ent = it.next();
				name = ent.getKey();
				val = ent.getValue();
			}
			
			if(second){
				second = false;
				listener.selected = name;
			}
			
			ColorObject color = new ColorObject(PADDING+ICON_SIZE*0.25f, posY,
					ICON_SIZE*1.5f, name, val, listener);
			
			handler.addObject(color);
			posY+=PADDING*4+ICON_SIZE*2;
		}
		
		handler.addObject(close);
		handler.addObject(minimize);
		
	}

	@Override
	public void tick() {
		handler.tick();
		
		if(maxScroll==0){
			for(GameObject o : handler.object){
				if(o.getY()>maxScroll)
					maxScroll = (int) o.getY();
			}
			
			maxScroll = Math.max(0, maxScroll-neo.height());
		}
		
		for(GameObject o : handler.object){
			if(o instanceof ColorObject)
				((ColorObject)o).setOffsetY(-scrollY);
		}
		
		if(hover<0)
			scrollY = Math.max(0, scrollY - velocity);
		else if(hover>0)
			scrollY = Math.min(maxScroll, scrollY + velocity);
		
		if(opacity<1)
			opacity+=0.075f;
		
		window.getFrame().setOpacity(Math.min(opacity, 1));
	}
	
	@Override
	public void render(Graphics arg0) {
		((Graphics2D)arg0).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		arg0.setColor(new Color(238, 238, 238));
		arg0.fillRect(0, 0, neo.width(), neo.height());
		
		arg0.setColor(new Color(224, 224, 224));
		arg0.fillRect(0, 0, neo.width(), PADDING*2+ICON_SIZE);
		
		handler.render(arg0);
		
		arg0.setColor(new Color(224, 224, 224));
		arg0.fillRect(0, 0, PADDING*2+ICON_SIZE*2, PADDING*2+ICON_SIZE);
		
		arg0.setColor(new Color(189, 189, 189));
		arg0.drawRoundRect(0, 0, neo.width()-1, neo.height()-1, 10, 10);
		arg0.drawLine(0, PADDING*2+ICON_SIZE, neo.width(), PADDING*2+ICON_SIZE);
		arg0.drawLine(PADDING*2+ICON_SIZE*2, PADDING*2+ICON_SIZE, PADDING*2+ICON_SIZE*2, neo.height());
		
		text.render(arg0);
	}
	
	@Override
	public boolean onHover(Rectangle r) {
		super.onHover(r);
		
		Rectangle upperRectangle = new Rectangle(0, (int) (PADDING*2+ICON_SIZE),
				PADDING*2+ICON_SIZE*2, PADDING*2+ICON_SIZE*2);
		Rectangle lowerRectangle = new Rectangle(0, (int) neo.height()-(PADDING*2+ICON_SIZE*2),
				PADDING*2+ICON_SIZE*2, PADDING*2+ICON_SIZE*2);
		
		if(r.intersects(upperRectangle)){
			hover = -1;
			velocity = (float) ((Math.max(0, upperRectangle.getHeight() - (r.getY() - (PADDING*2+ICON_SIZE))) * 5.0f) / upperRectangle.getHeight());
		}else if(r.intersects(lowerRectangle)){
			hover = 1;
			velocity = (float) (((r.getY()-lowerRectangle.getY()) * 5.0f) / lowerRectangle.getHeight());
		}else{
			hover = 0;
		}
		
		return false;
	}

}
