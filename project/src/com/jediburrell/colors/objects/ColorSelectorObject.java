package com.jediburrell.colors.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedList;

import com.frostbyte.neo.exceptions.ResourceNotFoundException;
import com.frostbyte.neo.framework.ObjectID;
import com.frostbyte.neo.framework.Resources;
import com.frostbyte.neo.objects.GameObject;

public class ColorSelectorObject extends GameObject{

	String title;
	Color color;
	
	private float offsetY = 0;
	
	private boolean hover = false;
	private boolean light = false;
	
	private String copy_to_clipboard = "Copy to clipboard.";
	
	public ColorSelectorObject(float x, float y, float width, float height, String title, Color color){
		super(x, y, ObjectID.dummy);
		
		this.width = width;
		this.height = height;
		this.title = title;
		this.color = color;
		
		light = color.getRed()+color.getGreen()+color.getBlue() >= 600;
	}
	
	@Override
	public void render(Graphics arg0) {
		
		arg0.setColor(hover ? color.darker() : color);
		arg0.fillRoundRect((int) x, (int) (y+offsetY), (int) width, (int) height, 5, 5);
		
		arg0.setColor(light ? hover ? color.darker().darker().darker() : color.darker().darker()
				: hover ? new Color(255, 255, 255, 200) : new Color(255, 255, 255, 150));
		arg0.setFont(Font.decode(Font.DIALOG_INPUT).deriveFont(10));
		arg0.drawString(hover ? copy_to_clipboard : title, (int) (x+title.length()), (int) (offsetY+y+(height/2)+4));
		
		arg0.setColor(color.darker());
		arg0.drawRoundRect((int) x, (int) (y+offsetY), (int) width, (int) height, 5, 5);
		
	}
	
	@Override
	public void tick(LinkedList<GameObject> objects) {
		if(!hover)
			copy_to_clipboard = "Copy to clipboard.";
	}
	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
	}

	@Override
	public boolean onHover(Rectangle r) {
		hover = false;
		if(!r.intersects(getBounds())) return false;
		hover = true;
		
		return false;
	}
	
	@Override
	public boolean onClick(Rectangle r) {
		if(!r.intersects(getBounds())) return false;
		
		String hex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
		
		try {
			if(Resources.getInt("copy_type")==0)
				hex = "#"+hex;
		} catch (ResourceNotFoundException e) {
		}
		
		StringSelection sS = new StringSelection(hex);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(sS, null);
		
		copy_to_clipboard = "Copied!";
		
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
