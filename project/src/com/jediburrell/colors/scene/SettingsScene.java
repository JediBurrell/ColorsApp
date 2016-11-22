package com.jediburrell.colors.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.exceptions.EncryptionFailureException;
import com.frostbyte.neo.exceptions.ResourceNotFoundException;
import com.frostbyte.neo.framework.BufferedImageLoader;
import com.frostbyte.neo.framework.Resources;
import com.frostbyte.neo.gui.TextObject;
import com.frostbyte.neo.scene.Scene;
import com.jediburrell.colors.Action;
import com.jediburrell.colors.OnClickListener;
import com.jediburrell.colors.WindowOverride;
import com.jediburrell.colors.objects.ActionableIconObject;
import com.jediburrell.colors.objects.ButtonObject;

public class SettingsScene extends Scene{

	private Neo neo;
	private WindowOverride window;
	
	private static int ICON_SIZE = 23;
	private static int PADDING = 10;
	
	private TextObject text, save, discard;
	private ActionableIconObject close, minimize;
	private ButtonObject with, without;
	
	private int hover = 0;
	private int click = 0;
	
	private int copy;
	private String ase;
	
	public SettingsScene(Neo neo, WindowOverride window) throws ResourceNotFoundException {
		super(neo);
		
		this.neo = neo;
		this.window = window;
		
		onLoad();
	}
	
	@Override
	public void onLoad() {
		
		try {
			copy = Resources.getInt("copy_type");
			ase = Resources.getString("ase_location");
		} catch (ResourceNotFoundException e1) {
		}
		
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
		
		with = new ButtonObject(0, PADDING*3+ICON_SIZE*2, neo.width()/2, PADDING*2+ICON_SIZE,
				new OnClickListener() {
					
					@Override
					public void onClick() {
						
						copy = 0;
						with.setActive(true);
						without.setActive(false);
						
					}
					
				});
		with.setText("#FFFFFF");
		without = new ButtonObject(neo.width()/2, PADDING*3+ICON_SIZE*2, neo.width()/2, PADDING*2+ICON_SIZE,
				new OnClickListener() {
					
					@Override
					public void onClick() {
						
						copy = 1;
						with.setActive(false);
						without.setActive(true);
						
					}
					
				});
		without.setText("FFFFFF");
		
		if(copy==0)
			with.setActive(true);
		else
			without.setActive(true);
		
		TextObject label2 = new TextObject(0, 0);
		label2.setText("Adobe Colors");
		label2.setFontColor(new Color(32, 32, 32));
		label2.setFont(Font.decode(Font.DIALOG_INPUT));
		label2.setFontSize(10);
		label2.setX("Adobe Colors".length()*4);
		label2.setY(with.getY()+with.getHeight()+PADDING*2);
		
		TextObject ase_location = new TextObject(0, 0);
		ase_location.setText(ase);
		ase_location.setFontColor(new Color(32, 32, 32));
		ase_location.setFont(Font.decode(Font.DIALOG_INPUT));
		ase_location.setFontSize(12);
		ase_location.setX(neo.width()/2);
		ase_location.setY(label2.getY()+PADDING*2);
		
		ButtonObject change_location = new ButtonObject(0, ase_location.getY()+PADDING*2,
				neo.width(), PADDING*2+ICON_SIZE,
				new OnClickListener() {
					
					@Override
					public void onClick() {
						
						JFileChooser fc = new JFileChooser();
						FileNameExtensionFilter filter =
								new FileNameExtensionFilter("Adobe Color Palette", "ase");
						fc.setFileFilter(filter);
						int value = fc.showOpenDialog(window.getFrame());

						File file = null;
						if (value == JFileChooser.APPROVE_OPTION) {
							file = fc.getSelectedFile();
							if(file.getAbsolutePath().endsWith(".ase")){
								ase = file.getAbsolutePath();
								ase_location.setText(ase);
							}
						} else {
						}
						
					}
					
				});
		change_location.setText("Change Palette File");
		
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
		handler.addObject(with);
		handler.addObject(without);
		handler.addObject(label2);
		handler.addObject(ase_location);
		handler.addObject(change_location);
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
		
		if(r.intersects(discardRectangle))
			try {
				click = 0;
				toScene(new PaletteScene(neo, window));
			} catch (ResourceNotFoundException e) {
			}
		else if(r.intersects(saveRectangle)){
			click = 0;
			
			try {
				Resources.putInteger("copy_type", copy);
				Resources.putString("ase_location", ase);
			} catch (EncryptionFailureException e1) {
			}
			
			try {
				toScene(new PaletteScene(neo, window));
			} catch (ResourceNotFoundException e) {
				// Yeah, we kind know that the resources are found now... Cause we're here.
			}
		}
		
		return false;
	}

}
