package udpstream.com.revkusz.udpserver;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class Server implements ActionListener{
	

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public final static String TITLE = "IP stream";
	public final static int RGB_MODE = 0;
	public final static int YCRCB_MODE = 1;
	public final static int MJPEG_MODE = 2;
	
	
	private int streamType; 
	private Thread client_thread;
	private boolean fut = true;
	
	
	int[] kep = new int[WIDTH*HEIGHT];
	byte[] rgbimg = new byte[WIDTH*HEIGHT*3];
	BufferedImage cbcrimg;
	BufferStrategy bufferStrategy;
	Graphics graphics;
	client threadclient;
	
	
	public Server() {
		setStreamType(RGB_MODE); //alapértelmezett mód rgb
		boolean running = true;
		
		threadclient = new client(this);
		client_thread = new Thread(threadclient);
	    client_thread.start();
	    JFrame frame = new JFrame(TITLE);
	    Canvas canvas = frameInit(frame);
	    
	    
	    canvas.createBufferStrategy(3);
	    cbcrimg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	   
	    while (running) {
	    	if (fut) {
	    		
	    		long nanoTimeAtStartOfUpdate = System.nanoTime();
		    	
		        bufferStrategy = canvas.getBufferStrategy();
		        graphics = bufferStrategy.getDrawGraphics();
		        
		        cbcrimg.setData(Raster.createRaster(cbcrimg.getSampleModel(), new DataBufferInt(kep, kep.length), new Point() ) );
				graphics.drawImage(cbcrimg, 0, 0, null);
	 
		        bufferStrategy.show();
		        graphics.dispose();
		        
		        waitUntilNextUpdate(nanoTimeAtStartOfUpdate);
	    	} else {
	    		try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	}

	public  void setImage(byte[] image){
		
		int y;
		int cr;
		int cb;
		
		if (streamType == YCRCB_MODE) {
			int red;
			int green;
			int blue;
			for (int i = 0; i < (WIDTH*HEIGHT*4); i+=4 ) {	
		    	y = image[i] & 0xFF;
		    	cr = image[i+1] & 0xFF;
		    	cb = image[i+2]& 0xFF;
		    	red = (int)( y + 1.402 * (cr-128));
		    	green =(int) ( y - 0.34414 *(cb-128)-0.71414 * (cr-128));
		    	blue =(int) ( y + 1.772 * (cb-128));
				kep[i/4] = (((int)blue & 0xFF) <<16) | (((int)green & 0xFF)<<8) | (int)red & 0xFF;
			}
		} else if (streamType == RGB_MODE ) {
			for (int i = 0; i < (WIDTH*HEIGHT*4); i+=4 ) {	
		    	y = image[i] & 0xFF;
		    	cr = image[i+1] & 0xFF;
		    	cb = image[i+2]& 0xFF;
		    	kep[i/4] = (((int)cb & 0xFF) <<16) | (((int)y & 0xFF)<<8) | (int)cr & 0xFF;
			}
		} else if (streamType == MJPEG_MODE ) {
			//NOT DONE JET
		} else {
			System.err.println("Nincs ilyen mód a program most kilép");
			System.exit(-1);
		}

	}
	
	
	public static void main(String[] args)
	{
	    
	    @SuppressWarnings("unused")
		Server server = new Server();

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
	    
	}
	
	
	private Canvas frameInit(JFrame frame) {
		
		frame.setSize(WIDTH, HEIGHT);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(100, 100, 800, 600);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);
	    
	    
	    JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

	    Canvas canvas = new Canvas();
	    canvas.setSize(WIDTH, HEIGHT);
	    canvas.setBackground(Color.BLACK);
	    canvas.setBounds(10, 10, 640, 480);
	    canvas.setVisible(true);
	    canvas.setFocusable(false);
	    contentPane.add(canvas);
		
		JButton RGBbtn = new JButton("RGB Mode");
		RGBbtn.setBounds(656, 10, 118, 23);
		RGBbtn.addActionListener(this);
		contentPane.add(RGBbtn);
		
		JButton YCrCbbtn = new JButton("YCrCb Mode");
		YCrCbbtn.setBounds(656, 44, 118, 23);
		YCrCbbtn.addActionListener(this);
		contentPane.add(YCrCbbtn);
		
		JButton stopbtn = new JButton("Stop stream");
		stopbtn.setBounds(656, 180, 118, 23);
		stopbtn.addActionListener(this);
		contentPane.add(stopbtn);
		
		JButton startbtn = new JButton("Start stream");
		startbtn.setBounds(656, 146, 118, 23);
		startbtn.addActionListener(this);
		contentPane.add(startbtn);
		
		JButton MJPEGbtn = new JButton("MJPEG Mode");
		MJPEGbtn.setBounds(656, 78, 118, 23);
		MJPEGbtn.addActionListener(this);
		contentPane.add(MJPEGbtn);
		
		JButton savImgbtn = new JButton("Save image");
		savImgbtn.setBounds(656, 112, 118, 23);
		savImgbtn.addActionListener(this);
		contentPane.add(savImgbtn);
		

		return canvas;
	}
	
	private void waitUntilNextUpdate(long nanoTimeCurrentUpdateStartedOn) {
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

	public int getStreamType() {
		return streamType;
	}

	public void setStreamType(int streamType) {
		this.streamType = streamType;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch (e.getActionCommand()) {
			case "RGB Mode" : {
				setStreamType(RGB_MODE);
				break;
			}
			case "YCrCb Mode" : {
				setStreamType(YCRCB_MODE);
				break;
			}
			case "Stop stream" : {
				threadclient.stop();
				fut = false;
				break;
			}
			case "Start stream" : {
				threadclient.start();
				fut = true;
				break;
			}
			case "MJPEG Mode" : {
				setStreamType(MJPEG_MODE);
				break;
			}
			case "Save image" : {
				saveImg();
				break;
			}
			
		}
	}
	    
	public void saveImg() {
		try {
		    // retrieve image
			SimpleDateFormat sdfDate = new SimpleDateFormat("HH_mm_ss");
		    Date now = new Date();
		   // String filename = "images/";
		    String filename = sdfDate.format(now);
		    filename +=filename + ".png";
		    File outputfile = new File("images/"+ filename);
		    ImageIO.write(cbcrimg, "png", outputfile);
		} catch (IOException e) {
		}
	}
	
}
