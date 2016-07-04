package udpstream.com.revkusz.udpserver;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

public class Server {
	//constansok beállitása
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	static boolean fut = true;
	static int[] kep = new int[WIDTH*HEIGHT];
	static byte[] rgbimg = new byte[WIDTH*HEIGHT*3];
	static BufferedImage cbcrimg;
	static int count = 0;
	static int y;
	static int cr;
	static int cb;
	static int red;
	static int green;
	static int blue;
	


	public static void setImage(byte[] image){
		count = 0;
		for (int i = 0; i < (WIDTH*HEIGHT*4); i+=4 ) {	
	    	y = image[i] & 0xFF;
	    	cr = image[i+1] & 0xFF;
	    	cb = image[i+2]& 0xFF;
	    	red = (int)( y + 1.402 * (cr-128));
	    	green =(int) ( y - 0.34414 *(cb-128)-0.71414 * (cr-128));
	    	blue =(int) ( y + 1.772 * (cb-128));
			//kep[i/4] = (((int)blue & 0xFF) <<16) | (((int)green & 0xFF)<<8) | (int)red & 0xFF;
	    	kep[i/4] = (((int)cb & 0xFF) <<16) | (((int)y & 0xFF)<<8) | (int)cr & 0xFF;
			count+=3;
		}

	}
	
	
	public static void main(String[] args)
	{
	    final String title = "Test Window";
	    
	    boolean running = true;
	    
	    BufferStrategy bufferStrategy;
	    Graphics graphics;

	    /*Path path = Paths.get("a.img");
	    byte[] data;
	    try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data = null;
		}
	    setImage(data);*/
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
	    cbcrimg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	    
	    
	    
	    while (running) {	
	    	long nanoTimeAtStartOfUpdate = System.nanoTime();
	    	
	        bufferStrategy = canvas.getBufferStrategy();
	        graphics = bufferStrategy.getDrawGraphics();
	        
	        cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferInt(kep, kep.length), new Point() ) );
	        
			graphics.drawImage(cbcrimg, 0, 0, null);

	        
	        bufferStrategy.show();
	        graphics.dispose();
	        
	        waitUntilNextUpdate(nanoTimeAtStartOfUpdate);

	    }
	}
	
	
	private static void waitUntilNextUpdate(long nanoTimeCurrentUpdateStartedOn) {
	    long currentUpdateSpeed = 60;
	    if (currentUpdateSpeed > 0) {

	        long timeToSleep = currentUpdateSpeed -((System.nanoTime() -nanoTimeCurrentUpdateStartedOn)/ 10000000);

	        timeToSleep = Math.max(timeToSleep, 0);
	        if (timeToSleep > 0) {
	            try {
	                Thread.sleep(timeToSleep);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	}
	    

	
}
