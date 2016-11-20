package com.jediburrell.colors.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.framework.BufferedImageLoader;
import com.frostbyte.neo.scene.Scene;
import com.jediburrell.colors.Action;
import com.jediburrell.colors.WindowOverride;
import com.jediburrell.colors.objects.ActionableIconObject;

public class PaletteScene extends Scene{

	private Neo neo;
	private WindowOverride window;
	private float opacity = 0f;
	
	private static int ICON_SIZE = 23;
	private static int PADDING = 10;
	
	public PaletteScene(Neo neo, WindowOverride window) {
		super(neo);
		
		this.neo = neo;
		this.window = window;
		
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
		
		handler.addObject(close);
		
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
		
		arg0.setColor(new Color(189, 189, 189));
		arg0.drawRoundRect(0, 0, neo.width()-1, neo.height()-1, 10, 10);
		
		handler.render(arg0);
	}

}
