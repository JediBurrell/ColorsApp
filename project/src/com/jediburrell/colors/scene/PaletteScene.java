package com.jediburrell.colors.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import jto.colorscheme.ColorSet;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.framework.BufferedImageLoader;
import com.frostbyte.neo.framework.FileManager;
import com.frostbyte.neo.gui.TextObject;
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
		} catch (IOException e) {
		}
		
		TextObject text = new TextObject(0, 0);
		text.setText("ColorsApp");
		text.setFontColor(new Color(32, 32, 32));
		text.setFont(Font.decode(Font.DIALOG_INPUT));
		text.setFontSize(15);
		text.setX(("ColorsApp").length()*6);
		text.setY(PADDING*2+1);
		
		listener.selected = map.keySet().iterator().next();
		
		float posY = PADDING*2+ICON_SIZE+PADDING*2;
		
		boolean first = true;
		
		for(Map.Entry<String, Map<String, Color>> entry : map.entrySet()){
			if(first){
				first = false;
				continue;
			}
			
			String key = entry.getKey();
			Map<String, Color> value = entry.getValue();
			
			java.util.Iterator<Entry<String, Color>> it = value.entrySet().iterator();
			Map.Entry<String, Color> ent = it.next();
			String name = ent.getKey();
			Color val = ent.getValue();
			
			if(!name.toLowerCase().contains("primary")&&!name.toLowerCase().contains("500")){
				String current = "0";
				while(it.hasNext()){
					String k = it.next().getKey().toLowerCase();
					if(k.contains("400")&&Integer.parseInt(current)<400)
						{ name = ent.getKey(); val = ent.getValue(); current = "400"; }
					else if(k.contains("300")&&Integer.parseInt(current)<300)
						{ name = ent.getKey(); val = ent.getValue(); current = "300"; }
					else if(k.contains("200")&&Integer.parseInt(current)<200)
						{ name = ent.getKey(); val = ent.getValue(); current = "200"; }
					else if(k.contains("100")&&Integer.parseInt(current)<100)
						{ name = ent.getKey(); val = ent.getValue(); current = "100"; }
					
					if(k.contains("100")&&Integer.parseInt(current)<100)
					{ name = ent.getKey(); val = ent.getValue(); current = "100"; }
						if(k.contains("primary")||k.contains("500")){
						name = ent.getKey();
						val = ent.getValue();
						break;
					}
				}
			}
			
			ColorObject color = new ColorObject(PADDING+ICON_SIZE*0.25f, posY,
					ICON_SIZE*1.5f, name+"", val, listener);
			
			handler.addObject(color);
			posY+=PADDING*4+ICON_SIZE*2;
		}
		
		handler.addObject(close);
		handler.addObject(text);
		
	}

	@Override
	public void tick() {
		handler.tick();
		
		if(opacity<1)
			opacity+=0.075f;
		
		window.getFrame().setOpacity(Math.min(opacity, 1));
	}
	
	@Override
	public void render(Graphics arg0) {
		((Graphics2D)arg0).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		arg0.setColor(new Color(238, 238, 238));
		arg0.fillRect(0, 0, neo.width(), neo.height());
		
		arg0.setColor(new Color(224, 224, 224));
		arg0.fillRect(0, 0, neo.width(), PADDING*2+ICON_SIZE);
		
		arg0.setColor(new Color(189, 189, 189));
		arg0.drawRoundRect(0, 0, neo.width()-1, neo.height()-1, 10, 10);
		arg0.drawLine(0, PADDING*2+ICON_SIZE, neo.width(), PADDING*2+ICON_SIZE);
		arg0.drawLine(PADDING*2+ICON_SIZE*2, PADDING*2+ICON_SIZE, PADDING*2+ICON_SIZE*2, neo.height());
		
		handler.render(arg0);
	}

}
