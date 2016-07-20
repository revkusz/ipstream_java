package udpstream.com.revkusz.udpserver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MJPEGplayer {
	int frame = 0;
	public MJPEGplayer() {
	}
	
	public BufferedImage getframe() {
		File file = new File("images/"+frame+".jpeg");
		frame++;
		if (frame == 32) {
			frame=0;
		}
		try {
			
			return ImageIO.read(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
