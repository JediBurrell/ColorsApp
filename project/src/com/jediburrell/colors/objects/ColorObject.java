package com.jediburrell.colors.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.frostbyte.neo.framework.ObjectID;
import com.frostbyte.neo.objects.GameObject;
import com.jediburrell.colors.ColorListener;

public class ColorObject extends GameObject{

	private ColorListener listener;
	private Color color;
	private String name;
	
	private boolean hover = false;
	private float offsetSize = 0;
	private float offsetY = 0;
	
	public ColorObject(float x, float y, float size, String name, Color color, ColorListener listener) {
		super(x, y, ObjectID.dummy);
		
		this.width = this.height = size;
		this.color = color;
		this.name = name;
		this.listener = listener;
	}
	
	@Override
	public void render(Graphics arg0) {
		
		arg0.setColor(color);
		
		if(listener.selected==name){
			((Graphics2D)arg0).setStroke(new BasicStroke(3));
			arg0.drawOval((int)x-5, (int) (y-5+offsetY), (int)width+10, (int)height+10);
		}
		
		arg0.fillOval((int) (x - offsetSize/2f), (int) (y - offsetSize/2f + offsetY), (int) (width+offsetSize), (int) (height+offsetSize));
		
		if(hover){
			arg0.setColor(color);
			arg0.fillRoundRect((int) (x + width*1.5f), (int) (y+offsetY), name.length()*10, (int) height, 2, 2);
			arg0.setColor(new Color(250, 250, 250));
			arg0.drawString(name, (int) (x + width*1.75f), (int) (y+height/2+offsetY)+3);
		}
		
		((Graphics2D)arg0).setStroke(new BasicStroke(1));
		
	}
	
	@Override
	public void tick(LinkedList<GameObject> objects) {
		
		if(hover&&offsetSize!=3)
			offsetSize+=0.5f;
		else if(offsetSize!=0)
			offsetSize-=0.5f;
		
	}
	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
	}

	@Override
	public boolean onClick(Rectangle r) {
		if(r.intersects(getBounds()))
			listener.onColorChanged(name);
		
		return true;
	}
	
	@Override
	public boolean onHover(Rectangle r) {
		hover = r.intersects(getBounds());
		
		return true;
	}
	
	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle((int) x, (int) (y+offsetY), (int) width, (int) height);
	}
	
	@Override
	public void onCollision(GameObject arg0, ObjectID arg1) {
	}
	
}
