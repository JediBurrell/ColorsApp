package com.jediburrell.colors.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.JFrame;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.exceptions.ResourceNotFoundException;
import com.frostbyte.neo.framework.BufferedImageLoader;
import com.frostbyte.neo.gui.TextObject;
import com.frostbyte.neo.scene.Scene;
import com.jediburrell.colors.Action;
import com.jediburrell.colors.WindowOverride;
import com.jediburrell.colors.objects.ActionableIconObject;

public class SettingsScene extends Scene{

	private Neo neo;
	private WindowOverride window;
	
	private static int ICON_SIZE = 23;
	private static int PADDING = 10;
	
	private TextObject text, save, discard;
	private ActionableIconObject close, minimize;
	
	private int hover = 0;
	private int click = 0;
	
	public SettingsScene(Neo neo, WindowOverride window) throws ResourceNotFoundException {
		super(neo);
		
		this.neo = neo;
		this.window = window;
		
		onLoad();
	}
	
	@Override
	public void onLoad() {
		
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
		text.setText("Settings");
		text.setFontColor(new Color(32, 32, 32));
		text.setFont(Font.decode(Font.DIALOG_INPUT));
		text.setFontSize(15);
		text.setX(("Settings").length()*6);
		text.setY(PADDING*2+1);
		
		TextObject label1 = new TextObject(0, 0);
		label1.setText("Copy Style");
		label1.setFontColor(new Color(32, 32, 32));
		label1.setFont(Font.decode(Font.DIALOG_INPUT));
		label1.setFontSize(10);
		label1.setX(("Copy Style").length()*4);
		label1.setY(PADDING*2+ICON_SIZE*2);
		
		save = new TextObject(0, 0);
		save.setText("Save");
		save.setFontColor(new Color(32, 32, 32));
		save.setFont(Font.decode(Font.DIALOG_INPUT));
		save.setFontSize(13);
		save.setX(neo.width()/1.35f);
		save.setY(neo.height()-ICON_SIZE);
		
		discard = new TextObject(0, 0);
		discard.setText("Discard");
		discard.setFontColor(new Color(32, 32, 32));
		discard.setFont(Font.decode(Font.DIALOG_INPUT));
		discard.setFontSize(13);
		discard.setX(neo.width()-neo.width()/1.35f);
		discard.setY(neo.height()-ICON_SIZE);
		
		handler.addObject(label1);
		handler.addObject(close);
		handler.addObject(minimize);
		
	}

	@Override
	public void tick() {
		
		handler.tick();
		
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
		arg0.fillRect(0, 0, neo.width(), PADDING*2+ICON_SIZE);
		
		if(hover==1)
			arg0.fillRect(0, neo.height()-(PADDING*2+ICON_SIZE), neo.width()/2, PADDING*2+ICON_SIZE);
		if(hover==2)
			arg0.fillRect(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE), neo.width()/2, PADDING*2+ICON_SIZE);
		
		arg0.setColor(new Color(189, 189, 189));
		
		if(click==1)
			arg0.fillRect(0, neo.height()-(PADDING*2+ICON_SIZE), neo.width()/2, PADDING*2+ICON_SIZE);
		if(click==2)
			arg0.fillRect(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE), neo.width()/2, PADDING*2+ICON_SIZE);
		
		arg0.drawRoundRect(0, 0, neo.width()-1, neo.height()-1, 10, 10);
		arg0.drawLine(0, PADDING*2+ICON_SIZE, neo.width(), PADDING*2+ICON_SIZE);
		arg0.drawLine(0, neo.height()-(PADDING*2+ICON_SIZE), neo.width(), neo.height()-(PADDING*2+ICON_SIZE));
		arg0.drawLine(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE), neo.width()/2, neo.height());
		
		text.render(arg0);
		save.render(arg0);
		discard.render(arg0);
		close.render(arg0);
		minimize.render(arg0);
	}
	
	@Override
	public boolean onHover(Rectangle r) {
		super.onHover(r);
		
		Rectangle saveRectangle = new Rectangle(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);
		Rectangle discardRectangle = new Rectangle(0, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);
		
		if(r.intersects(discardRectangle)) hover = 1;
		else if(r.intersects(saveRectangle)) hover = 2;
		else hover = 0;
		
		return false;
	}
	
	@Override
	public boolean onTouch(Rectangle r) {
		super.onTouch(r);
		
		Rectangle saveRectangle = new Rectangle(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);
		Rectangle discardRectangle = new Rectangle(0, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);
		
		if(r.intersects(discardRectangle)) click = 1;
		else if(r.intersects(saveRectangle)) click = 2;
		else click = 0;
		
		return false;
	}
	
	@Override
	public boolean onClick(Rectangle r) {
		super.onClick(r);
		
		Rectangle saveRectangle = new Rectangle(neo.width()/2, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);
		Rectangle discardRectangle = new Rectangle(0, neo.height()-(PADDING*2+ICON_SIZE),
				neo.width()/2, PADDING*2+ICON_SIZE);

		click = 0;
		
		if(r.intersects(discardRectangle)) click = 0;
		else if(r.intersects(saveRectangle)) click = 0;
		
		return false;
	}

}
