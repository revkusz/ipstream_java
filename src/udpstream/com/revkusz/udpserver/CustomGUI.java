package udpstream.com.revkusz.udpserver;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.image.VolatileImage;

public class CustomGUI extends Canvas {
	 
	 private VolatileImage volatileImg;
	 
	 // ...
	 
	 public void paint(Graphics g) {
	  // create the hardware accelerated image.
	  createBackBuffer();
	 
	  // Main rendering loop. Volatile images may lose their contents. 
	  // This loop will continually render to (and produce if neccessary) volatile images
	  // until the rendering was completed successfully.
	  do {
	   
	   // Validate the volatile image for the graphics configuration of this 
	   // component. If the volatile image doesn't apply for this graphics configuration 
	   // (in other words, the hardware acceleration doesn't apply for the new device)
	   // then we need to re-create it.
	   GraphicsConfiguration gc = this.getGraphicsConfiguration();
	   int valCode = volatileImg.validate(gc);
	   
	   // This means the device doesn't match up to this hardware accelerated image.
	   if(valCode==VolatileImage.IMAGE_INCOMPATIBLE){
	    createBackBuffer(); // recreate the hardware accelerated image.
	   }
	  
	   Graphics offscreenGraphics = volatileImg.getGraphics();   
//	   offscreenGraphics.setColor(Color.WHITE);
//	   offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
//	   offscreenGraphics.setColor(Color.BLACK);
//	   offscreenGraphics.drawLine(0, 0, 10, 10); // arbitrary rendering logic
	   
	   for (int j = 0; j<200;j++){
       	for (int i = 0; i<960;i+=3){
       		offscreenGraphics.setColor(new Color(Server.img[i],Server.img[i+1],Server.img[i+2]));
       		offscreenGraphics.drawRect(i/3, j, 1, 1);
       	}
       }
	   
	   // paint back buffer to main graphics
	   g.drawImage(volatileImg, 0, 0, this);
	   // Test if content is lost   
	  } while(volatileImg.contentsLost());
	 }
	 
	 // This method produces a new volatile image.
	 private void createBackBuffer() {
	   GraphicsConfiguration gc = getGraphicsConfiguration();
	   volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
	 }
	 
	 public void update(Graphics g) {
	  paint(g);
	 }
	}