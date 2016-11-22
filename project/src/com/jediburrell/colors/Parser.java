package com.jediburrell.colors;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jto.colorscheme.Color;
import jto.colorscheme.ColorSet;
import jto.colorscheme.Palette;

class Parser {

    public static final List<String> IMAGE_FILE_TYPES = new ArrayList<String>();
    private static Map<String, Map<String, java.awt.Color>> map = new HashMap<String, Map<String, java.awt.Color>>();
    private static Map<String, java.awt.Color> colors = new HashMap<String, java.awt.Color>();
    private static String current_no_break = ")(*";
    
    private static String currentName;

    static {
        IMAGE_FILE_TYPES.add(".jpg");
        IMAGE_FILE_TYPES.add(".jpeg");
        IMAGE_FILE_TYPES.add(".tiff");
        IMAGE_FILE_TYPES.add(".png");
        IMAGE_FILE_TYPES.add(".gif");
        IMAGE_FILE_TYPES.add(".bmp");
    }

    static Palette readFile(final String fileName) throws IOException {
        if (fileName.contains(".xml")) {
            return readXmlFile(fileName);
        } else if (fileName.contains(".ase")) {
            return readAseFile(fileName);
        } else {
            throw new RuntimeException("The file must be either .xml or .ase or an image filetype (.jpg, .jpeg, .tiff, .png, .gif, .bmp).");
        }
    }

    static boolean isAnInternetImageFile(String fileName) {
        for (String type : IMAGE_FILE_TYPES) {
            if (fileName.toLowerCase().endsWith(type) && fileName.startsWith("http")) {
                return true;
            }
        }
        return false;
    }

    private static Palette readXmlFile(final String fileName) {

        File file = new File(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = db.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Palette palette = new Palette();

        Node paletteNode = document.getElementsByTagName("palette").item(0);
        NodeList colorSetNodes = paletteNode.getChildNodes();


        for (int i = 0; i < colorSetNodes.getLength(); i++) {
            if ("colorset".equals(colorSetNodes.item(i).getNodeName())) {

                Node colorSetNode = colorSetNodes.item(i);

                ColorSet colorSet = new ColorSet();
                palette.addColorSet(colorSet);

                NodeList colors = colorSetNode.getChildNodes();

                for (int j = 0; j < colors.getLength(); j++) {
                    NamedNodeMap attr = colors.item(j).getAttributes();
                    if (null != attr && attr.getLength() > 0) {
                        Color color = new Color();

                        color.blue = Integer.parseInt(attr.getNamedItem("b").getNodeValue());
                        color.green = Integer.parseInt(attr.getNamedItem("g").getNodeValue());
                        color.red = Integer.parseInt(attr.getNamedItem("r").getNodeValue());
                        int base = Integer.parseInt(attr.getNamedItem("rgb").getNodeValue(), 16);
                        int value = base | (0xFF << 24);
                        color.setRgb(value);

                        colorSet.addColor(color);

                        System.out.println("\t( " + color.red + ", " + color.green + ", " + color.blue + " )");
                    }
                }
            }
        }
        System.out.println("Done creating palette.");
        return palette;
    }
    
    @SuppressWarnings({"resource"})
    private static Palette readAseFile(final String fileName)
            throws IOException {
        File ase = new File(fileName);
        FileInputStream inStream = new FileInputStream(ase);
        ByteBuffer buf = ByteBuffer.allocate((int) inStream.getChannel().size() * 2);
        buf.order(ByteOrder.BIG_ENDIAN);
        inStream.getChannel().read(buf);

        buf.rewind();

        byte[] signature = new byte[4];
        byte[] expectedSignature = new byte[]{'A', 'S', 'E', 'F'};
        buf.get(signature);
        if (!Arrays.equals(expectedSignature, signature)) {
            throw new IOException("'" + ase.getAbsolutePath()
                    + "' is not an .ase file. Signature does not match!");
        }
        buf.getShort(); // int major
        buf.getShort(); // int minor
        int noBlocks = buf.getInt();
        Palette palette = new Palette();
        int colorSetIndex = 0;
        for (int i = 0; i < noBlocks; ++i) {

            readBlock(buf, palette, colorSetIndex);
        }
        
        if(currentName == null) return palette;
        
        if(currentName.contains(" ")){
        		if(current_no_break!=""){
        			HashMap<String, java.awt.Color> cCopy =
        					new HashMap<String, java.awt.Color>();
        			for(Map.Entry<String, java.awt.Color> entry : colors.entrySet()) {
        			    cCopy.put(new String(entry.getKey()), new java.awt.Color(entry.getValue().getRGB()));
        			}
        			map.put(currentName, cCopy);
        			colors = new HashMap<String, java.awt.Color>();
        			
        			System.out.println(map.get(currentName).toString());
        			
        			System.out.println("   New Color Set: " + current_no_break);
        		}
        }else{
        	if(!currentName.startsWith(current_no_break)){
        			HashMap<String, java.awt.Color> cCopy =
        					new HashMap<String, java.awt.Color>();
        			for(Map.Entry<String, java.awt.Color> entry : colors.entrySet()) {
        			    cCopy.put(new String(entry.getKey()), new java.awt.Color(entry.getValue().getRGB()));
        			}
        			map.put(currentName, cCopy);
        			colors = new HashMap<String, java.awt.Color>();
        			
        			System.out.println("   New Color Set: " + current_no_break);
        		}
        }
        
        return palette;
    }

	private static void readBlock(ByteBuffer buf, Palette palette, int colorSetIndex) {    	
        buf.mark();
        short blockType = buf.getShort();
            /* int blockLength = */
        buf.getInt();
        short nameLen = buf.getShort();
        switch (blockType) {
            case (short) 0xc001:
                // group start
                ColorSet colorSet = new ColorSet();
                palette.addColorSet(colorSet);
                colorSetIndex++;
                break;
            case (short) 0xc002:
                // group end
            	try{
                buf.position(buf.position() + nameLen);
            	}catch(IllegalArgumentException e){
            	}
                break;
            case 0x0001:
                // color entry
                String colorName = readString(buf);
                // 4 byte color model
                byte first = buf.get();
                buf.get();
                buf.get();
                buf.get();
                if ('R' == first) {
                    Color color = new Color();
                    color.red = (int) (buf.getFloat() * 255);
                    color.green = (int) (buf.getFloat() * 255);
                    color.blue = (int) (buf.getFloat() * 255);

                    int rgb = ((0xFF << 24) | (color.red & 0xFF) << 16) | ((color.green & 0xFF) << 8) | ((color.blue) & 0xFF);

                    color.setRgb(rgb);

                    short type = buf.getShort();
                    String types[] = new String[]{"Global", "Spot", "Normal"};
                    System.out.printf("   %s ( %d, %d, %d ) / %s", colorName, color.red, color.green,
                            color.blue, types[type]);
                    System.out.println();
                    palette.getColorSets().get(colorSetIndex).addColor(color);
                    
                    currentName = colorName;
                    
                    if(colorName.contains(" ")){
                    	if(!colorName.startsWith(current_no_break)){
                    		if(current_no_break!=""){
                    			HashMap<String, java.awt.Color> cCopy =
                    					new HashMap<String, java.awt.Color>();
                    			for(Map.Entry<String, java.awt.Color> entry : colors.entrySet()) {
                    			    cCopy.put(new String(entry.getKey()), new java.awt.Color(entry.getValue().getRGB()));
                    			}
                    			map.put(colorName, cCopy);
                    			colors = new HashMap<String, java.awt.Color>();
                    			
                    			System.out.println(map.get(colorName).toString());
                    			
                    			System.out.println("   New Color Set: " + current_no_break);
                    		}
                    	    
                    		current_no_break = colorName.split(" ")[0];
                    	}
                    }else{
                    	if(!colorName.startsWith(current_no_break)){
                    		if(current_no_break!=""){
                    			HashMap<String, java.awt.Color> cCopy =
                    					new HashMap<String, java.awt.Color>();
                    			for(Map.Entry<String, java.awt.Color> entry : colors.entrySet()) {
                    			    cCopy.put(new String(entry.getKey()), new java.awt.Color(entry.getValue().getRGB()));
                    			}
                    			map.put(colorName, cCopy);
                    			colors = new HashMap<String, java.awt.Color>();
                    			
                    			System.out.println("   New Color Set: " + current_no_break);
                    		}
                    		
                    		current_no_break = colorName;
                    	}
                    }
                    
            		colors.put(colorName, new java.awt.Color(color.red, color.green, color.blue));
                } else if ('C' == first) {
                    throw new RuntimeException("Unable to handle CMYK colors");
                } else if ('L' == first) {
                    throw new RuntimeException("Unable to handle LAB colors");
                } else if ('G' == first) {
                    throw new RuntimeException("Unable to handle Gray colors");
                }

                break;
        }
    }

    private static String readString(ByteBuffer buf) {
        final StringBuilder ret = new StringBuilder(10);
        char next = buf.getChar();
        while (next != (char) 0) {
            ret.append(next);
            next = buf.getChar();
        }
        return ret.toString();
    }
    
    public static Map<String, Map<String, java.awt.Color>> getMap(){
    	
    	return map;
    }
    
}