package com.jediburrell.colors;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.Window;
import com.frostbyte.neo.exceptions.UnchangableException;
import com.jediburrell.colors.scene.PaletteScene;

public class Main extends Neo{

	private static int PADDING = 10;
	private static WindowOverride window;
	
	public Main(WindowOverride window) {
		super(window);
		
		scene = new PaletteScene(this, window);
	}
	
	// It is impossible for this error to be thrown.
	public static void main(String[] args) throws UnchangableException{
		window = new WindowOverride("ColorsApp");
		window.setSize(400, 500);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setPosition(screenSize.width - window.getWidth() - PADDING,
				screenSize.height - window.getHeight() - PADDING * 5);
		

		Main main = new Main(window);
		window.setContent(main);
		
		window.start();
		main.start();
	}
	
}
