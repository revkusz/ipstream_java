package udpstream.com.revkusz.udpserver;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;

public class Server {
	//final változok beállitása
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
static boolean fut = true;
static byte[] img;
static int[] kep = new int[640*480];
static BufferedImage bfimg,cbcrimg;
static boolean setRequest = false;
static int count = 0;
public static double start,diff,wait;
static byte[] asd = new byte[WIDTH*HEIGHT*4];
static byte[] ycrbr = new byte[WIDTH*HEIGHT*3];
static byte[] rgbimg = new byte[WIDTH*HEIGHT*3];
static int red;
static int green;
static int blue;


	public static void setImage(byte[] image){
		
		byte[] asd2 = new byte[WIDTH*HEIGHT*3];
		for (int i = 0; i < (WIDTH*HEIGHT*3); i+=4 ) {
			//System.out.println(image[0]+" "+image[1]+" "+image[2]+" "+image[3]+" "+image[4]+" "+image[5]+" "+image[6]+" "+image[7]);
			ycrbr[i]=image[((i/3)*4)];
			ycrbr[i+1]=image[((i/3)*4)+1];
			ycrbr[i+2]=image[((i/3)*4)+2];
	    	int y = ycrbr[i]& 0xFF;
	    	int cb = ycrbr[i+1] & 0xFF;
	    	int cr = ycrbr[i+2]& 0xFF;
	    	//System.out.println(ycrbr[i]+" "+y+" ");
	    	//System.out.println();
	    	//System.out.format("\n %x   %x", ycrbr[i],y);
	    	rgbimg[i] = (byte)( y + 1.402 * (cr-128));
	    	rgbimg[i+1] =(byte) ( y - 0.34414 *(cb-128)-0.71414 * (cr-128));
	    	rgbimg[i+2] =(byte) ( y + 1.772 * (cb-128));
			//kep[i/3] = (((int)y & 0xFF) <<16) | (((int)y & 0xFF)<<8) | (int)y & 0xFF;
	    	//System.out.println(red+" "+green+" "+blue);
	    	//kep[i/3] = (((int)cb & 0xFF) <<16) | (((int)cb & 0xFF)<<8) | (int)cb & 0xFF;
	    	//System.out.println(y+" "+cb+" "+cr+"     ycbcr");
		}
		for (int k = 0; k<(WIDTH*HEIGHT*3); k+=3) {
			kep[k/3] = (((int)rgbimg[k] & 0xFF) <<16) | (((int)rgbimg[k+1] & 0xFF)<<8) | (int)rgbimg[k+2] & 0xFF;
		}

	}
	
	
	public static void main(String[] args)
	{
		img = new byte[WIDTH*HEIGHT*3];
	    final String title = "Test Window";
	    
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
	    cbcrimg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	    BufferedImage image;
	    
	    
	    /*byte[] asd2 = new byte[WIDTH*HEIGHT*3];
	    for (int i = 0; i < (WIDTH*HEIGHT*3); i+=3 ) {	    	
	    	asd2[i]=asd[((i/3)*4)+0];
	    	asd2[i+1]=asd[((i/3)*4)+1];
	    	asd2[i+2]=asd[((i/3)*4)+2];
	    	double y = asd2[i] & 0xFF;
	    	//double cr = asd2[i+1]& 0xFF;
	    	//double cb = asd2[i+2]& 0xFF;
	    	//System.out.println(y+" "+cr+" "+cb);
	    	byte red =(byte)  ((float)cr*((float)2-(float)2*(float)0.299)+(float)y);
	    	byte blue = (byte) ((float)cb*((float)2-(float)2*(float)0.114)+(float)y);
	    	byte green = (byte) (((float)y-(float)0.114*(float)blue-(float)0.229*(float)red)/(float)0.587);
	    	//red=(byte)y;
	    	//blue=green=50;
	    	//System.out.println(red+" "+green+" "+blue);
	    	//System.out.format("%x %x %x \n\r", red,green,blue);
	    	//kep[i/3] = (red & 0xFF <<16) | (green & 0xFF<<8) | blue & 0xFF;
	    	kep[i/3] = (((int)y & 0xFF) <<16) | (((int)y & 0xFF)<<8) | (int)y & 0xFF;

	    }*/
	
	    //cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferByte(asd2, asd2.length), new Point() ) );
	    //cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferInt(kep, kep.length), new Point() ) );
	    
	    
	    while (running) {	
	    	
	        bufferStrategy = canvas.getBufferStrategy();
	        graphics = bufferStrategy.getDrawGraphics();
	        //System.out.println(image.getHeight());
			//bfimg.setData(Raster.createRaster(bfimg.getSampleModel(), new DataBufferByte(img, img.length), new Point() ) );
		    //graphics.drawImage(bfimg, 0, 0, null);
	        
	        cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferInt(kep, kep.length), new Point() ) );
			graphics.drawImage(cbcrimg, 0, 0, null);

	        
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
