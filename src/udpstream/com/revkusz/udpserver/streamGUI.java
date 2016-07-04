package udpstream.com.revkusz.udpserver;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;

public class streamGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static Canvas canvas;
	
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
	static boolean running = false;
	
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
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					streamGUI frame = new streamGUI();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		BufferedImage cbcrimg;
	    BufferStrategy bufferStrategy;
	    Graphics graphics;

	    cbcrimg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	    while (running) {	
	    	//long nanoTimeAtStartOfUpdate = System.nanoTime();
	    	
	        bufferStrategy = canvas.getBufferStrategy();
	        graphics = bufferStrategy.getDrawGraphics();
	        
	        cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferInt(kep, kep.length), new Point() ) );
	        
			graphics.drawImage(cbcrimg, 0, 0, null);

	        
	        bufferStrategy.show();
	        graphics.dispose();
	        
	        //waitUntilNextUpdate(nanoTimeAtStartOfUpdate);

	    }
	}

	/**
	 * Create the frame.
	 */
	public streamGUI() {
		final String title = "ip stream";
		setTitle(title);
		
	   
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setBounds(10, 10, 640, 480);
		canvas.setVisible(true);
		contentPane.add(canvas);
		
		JButton RGBbtn = new JButton("RGB Mode");
		RGBbtn.setBounds(656, 10, 118, 23);
		contentPane.add(RGBbtn);
		
		JButton YCrCbbtn = new JButton("YCrCb Mode");
		YCrCbbtn.setBounds(656, 44, 118, 23);
		contentPane.add(YCrCbbtn);
		
		JButton stopbtn = new JButton("Stop stream");
		stopbtn.setBounds(656, 214, 118, 23);
		contentPane.add(stopbtn);
		
		JButton startbtn = new JButton("Start stream");
		startbtn.setBounds(656, 146, 118, 23);
		contentPane.add(startbtn);
		
		JButton MJPEGbtn = new JButton("MJPEG Mode");
		MJPEGbtn.setBounds(656, 78, 118, 23);
		contentPane.add(MJPEGbtn);
		
		JButton savImgbtn = new JButton("Save image");
		savImgbtn.setBounds(656, 112, 118, 23);
		contentPane.add(savImgbtn);
		
		JButton restartbtn = new JButton("Restart stream");
		restartbtn.setBounds(656, 180, 118, 23);
		contentPane.add(restartbtn);
		
	    
	    
	    
	    
	    canvas.createBufferStrategy(3);
	    running = true;
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
