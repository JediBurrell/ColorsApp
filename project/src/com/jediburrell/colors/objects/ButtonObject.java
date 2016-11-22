package com.jediburrell.colors.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.frostbyte.neo.framework.ObjectID;
import com.frostbyte.neo.gui.TextObject;
import com.frostbyte.neo.objects.GameObject;
import com.jediburrell.colors.OnClickListener;

public class ButtonObject extends GameObject{

	private int state = 0;
	private boolean active = false;
	private OnClickListener listener;
	
	private TextObject text;
	
	public ButtonObject(float x, float y, float width, float height, OnClickListener listener) {
		super(x, y, ObjectID.dummy);
		
		this.listener = listener;
		this.width = width;
		this.height = height;
		
		text = new TextObject(0, 0);
		text.setText("Button");
		text.setFontColor(new Color(32, 32, 32));
		text.setFont(Font.decode(Font.DIALOG_INPUT));
		text.setFontSize(13);
		text.setX(x+width/2);
		text.setY(y+height/2);
	}

	@Override
	public void render(Graphics arg0) {
		
		arg0.setColor(new Color(224, 224, 224));
		
		if(state==1)
			arg0.fillRect((int) x, (int) y, (int) width, (int) height);
				
		arg0.setColor(new Color(189, 189, 189));
		
		if(state==2||active)
			arg0.fillRect((int) x, (int) y, (int) width, (int) height);
		
		arg0.drawRect((int) x, (int) y, (int) width, (int) height);
		
		text.render(arg0);
		
	}
	
	public void setText(String text){
		this.text = new TextObject(0, 0);
		this.text.setText(text);
		this.text.setFontColor(new Color(32, 32, 32));
		this.text.setFont(Font.decode(Font.DIALOG_INPUT));
		this.text.setFontSize(13);
		this.text.setX(x+width/2);
		this.text.setY(y+height/2);
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	@Override
	public boolean onHover(Rectangle r) {
		state = 0;
		
		if(!r.intersects(getBounds())) return false;
		
		state = 1;

		return false;
	}
	
	@Override
	public boolean onTouch(Rectangle r) {
		if(!r.intersects(getBounds())) return false;
		
		state = 2;
		
		return false;
	}
	
	@Override
	public boolean onClick(Rectangle r) {
		state = 0;
		
		if(!r.intersects(getBounds())) return false;
		
		state = 1;
		listener.onClick();
		
		return true;
	}
	
	@Override
	public Rectangle getBounds() {
		
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public void onCollision(GameObject arg0, ObjectID arg1) {
	}

}
