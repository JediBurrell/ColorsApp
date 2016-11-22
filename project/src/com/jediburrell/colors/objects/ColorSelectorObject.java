package com.jediburrell.colors.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.frostbyte.neo.framework.ObjectID;
import com.frostbyte.neo.objects.GameObject;

public class ColorSelectorObject extends GameObject{

	String title;
	Color color;
	
	private float offsetY = 0;
	
	private boolean hover = false;
	
	public ColorSelectorObject(float x, float y, float width, float height, String title, Color color){
		super(x, y, ObjectID.dummy);
		
		this.width = width;
		this.height = height;
		this.title = title;
		this.color = color;
	}
	
	@Override
	public void render(Graphics arg0) {
		
		arg0.setColor(color);
		arg0.fillRoundRect((int) x, (int) (y+offsetY), (int) width, (int) height, 5, 5);
		
		arg0.setColor(Color.WHITE);
		arg0.setFont(Font.decode(Font.DIALOG_INPUT).deriveFont(10));
		arg0.drawString(hover ? "Copy to clipboard." : title, (int) (x+title.length()), (int) (offsetY+y+(height/2)+4));
		
	}

	@Override
	public boolean onHover(Rectangle r) {
		hover = false;
		if(!r.intersects(getBounds())) return false;
		hover = true;
		
		return false;
	}
	
	@Override
	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) (y+offsetY), (int) width, (int) height);
	}

	@Override
	public void onCollision(GameObject arg0, ObjectID arg1) {
	}
	
}
