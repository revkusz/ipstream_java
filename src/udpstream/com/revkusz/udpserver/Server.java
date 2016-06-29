package udpstream.com.revkusz.udpserver;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.VolatileImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Server {
	//final változok beállitása
	public static final int WIDTH = 320;
	public static final int HEIGHT = 200;
	
static boolean fut = true;
static byte[] img;
static BufferedImage bfimg;
static boolean setRequest = false;
static int count = 0;
public static double start,diff,wait;


	public static void setImage(byte[] image){
			img = image;

	}
	
	public static void main(String[] args)
	{
		img = new byte[WIDTH*HEIGHT*3];
	    final String title = "Test Window";
	    //final int width = 320;
	    //final int height = 200;
	    
	    boolean running = true;
	    
	    BufferStrategy bufferStrategy;
	    Graphics graphics;

	    //Creating the frame.
	    Thread thread = new Thread(new client());
	    thread.start();
	    JFrame frame = new JFrame(title);

	    frame.setSize(WIDTH, HEIGHT);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);

	    Canvas canvas = new Canvas();
	    canvas.setSize(WIDTH, HEIGHT);
	    canvas.setBackground(Color.BLACK);
	    canvas.setVisible(true);
	    canvas.setFocusable(false);

	    frame.add(canvas);
	    
	    canvas.createBufferStrategy(3);
	    bfimg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
	    
	    while (running) {	
	    	
	        bufferStrategy = canvas.getBufferStrategy();
	        graphics = bufferStrategy.getDrawGraphics();
			bfimg.setData(Raster.createRaster(bfimg.getSampleModel(), new DataBufferByte(img, img.length), new Point() ) );
		    graphics.drawImage(bfimg, 0, 0, null);


	        
	        /*
	        for (int j = 0; j<200;j++){
	        	for (int i = 0; i<960;i+=3){
	        	graphics.setColor(new Color(Server.img[i],Server.img[i+1],Server.img[i+2]));
		        graphics.drawRect(i/3, j, 1, 1);
	        	}
	        }*/
	        
	        bufferStrategy.show();
	        graphics.dispose();
	        if (count==0) {
	        	count++;
	        } else {
	        	count =0;
	        }
	        
	    }
	}
	

	
}
