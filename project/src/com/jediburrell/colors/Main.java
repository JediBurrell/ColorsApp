package com.jediburrell.colors;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.frostbyte.neo.Neo;
import com.frostbyte.neo.exceptions.EncryptionFailureException;
import com.frostbyte.neo.exceptions.ResourceNotFoundException;
import com.frostbyte.neo.exceptions.UnchangableException;
import com.frostbyte.neo.framework.FileManager;
import com.frostbyte.neo.framework.Resources;
import com.jediburrell.colors.scene.PaletteScene;

@SuppressWarnings("serial")
public class Main extends Neo{

	private static int PADDING = 10;
	private static WindowOverride window;
	
	public Main(WindowOverride window) {
		super(window);
		
		init();
		getResources();
		try {
			scene = new PaletteScene(this, window);
		} catch (ResourceNotFoundException e) {
		}
	}
	
	@Override
	protected void init() {
		FileManager.createFolder(getDir()+"/.colorsapp");
		try {
			exportResource("/material.ase", getDir()+"/.colorsapp/material.ase");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	/*
	 * 
	 * Author: Ordiel
	 * Thank you Ordiel!
	 * 
	 */
	
	public static String exportResource(String resourceName, String location) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = Main.class.getResourceAsStream(resourceName);
            if(stream == null) {
                throw new Exception("Didn't work.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(location);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return location;
    }
	
	public void getResources(){
		try {
			Resources.loadResources(getDir()+"/.colorsapp/", "os");
		} catch (Exception e) {
			try {
				createResources();
			} catch (EncryptionFailureException e1) {
			}
		}
	}
	
	// This exception won't be thrown.
	public void createResources() throws EncryptionFailureException{
		Resources.putInteger("copy_type", 0);
		Resources.putString("ase_location", getDir()+"\\.colorsapp\\material.ase");
	}
	
	public static String getDir()
	{
	    String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application "
	                + "Support";
	    else if (OS.contains("NUX"))
	        return System.getProperty("user.home");
	    return System.getProperty("user.dir");
	}
	
}
