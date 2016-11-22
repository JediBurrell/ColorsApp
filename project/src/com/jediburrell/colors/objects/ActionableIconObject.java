package com.jediburrell.colors.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.frostbyte.neo.framework.ObjectID;
import com.frostbyte.neo.objects.GameObject;
import com.jediburrell.colors.Action;

public class ActionableIconObject extends GameObject{

	private Action action;
	private BufferedImage icon, hover, click;
	private int state = 0;
	private BufferedImage[] icons = {icon, hover, click};
	
	public ActionableIconObject(float x, float y, int size, BufferedImage icon, Action action) {
		super(x, y, ObjectID.dummy);
		
		this.width = this.height = size;
		this.action = action;
		this.icon = icon;
		this.hover = icon;
		this.click = icon;
	}
	
	public void setHoverImage(BufferedImage hover){
		this.hover = hover;
		icons = new BufferedImage[]{icon, this.hover, click};
	}
	
	public void setClickImage(BufferedImage click){
		this.click = click;
		icons = new BufferedImage[]{icon, hover, this.click};
	}
	
	@Override
	public void render(Graphics arg0) {
		
		arg0.drawImage(icons[state], (int) x, (int) y, (int) width, (int) height, null);
		
	}	
	
	@Override
	public boolean onClick(Rectangle r) {
		if(r.intersects(getBounds()))
			action.perform();
		
		state = 0;
		
		return false;
	}
	
	@Override
	public boolean onHover(Rectangle r) {
		if(r.intersects(getBounds()))
			state = 1;
		else
			state = 0;
		
		return false;
	}
	
	@Override
	public boolean onTouch(Rectangle r) {
		if(r.intersects(getBounds()))
			state = 2;
		
		return false;
	}

	@Override
	public Rectangle getBounds() {

		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public void onCollision(GameObject arg0, ObjectID arg1) {
	}

	
	
}
