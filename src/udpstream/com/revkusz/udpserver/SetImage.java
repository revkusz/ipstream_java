package udpstream.com.revkusz.udpserver;

import java.awt.Point;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

public class SetImage implements Runnable {
	Server szerver;
	byte[] kep;
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setImage();

	}
	public SetImage(Server server,byte[] image) {
		szerver = server;
		kep=image;
	}
	
	public  void setImage(){
		if (client.done){
		client.done =false;
		int y;
		int cr;
		int cb;
		if (szerver.getStreamType() == Server.YCRCB_MODE) {
			int red;
			int green;
			int blue;
			for (int i = 0; i < (Server.WIDTH*Server.HEIGHT*4); i+=4 ) {	
		    	y = kep[i] & 0xFF;
		    	cr = kep[i+1] & 0xFF;
		    	cb = kep[i+2] & 0xFF;
		    	
		    	red = (int)( (y) + 1.402 * (cr-128));
		    	green =(int) ( (y) - 0.34414 *(cb-128)-0.71414 * (cr-128));
		    	blue =(int) ( (y) + 1.772 * (cb-128));
		    	if (blue >255) {
		    		blue=255;
		    	}
		    	if (green >255) {
		    		green=255;
		    	}
		    	if (red >255) {
		    		red=255;
		    	}
		    	
		    	Server.kep[i/4] = (((int)blue & 0xFF) <<16) | (((int)green & 0xFF)<<8) | (int)red & 0xFF;
			}
		} else if (szerver.getStreamType()== Server.RGB_MODE ) {
			for (int i = 0; i < (Server.WIDTH*Server.HEIGHT*4); i+=4 ) {	
				Server.kep[i/4] = (((int)kep[i+2] & 0xFF) <<16) | (((int)kep[i] & 0xFF)<<8) | (int)kep[i+1] & 0xFF;
			}
		} else if (szerver.getStreamType() == Server.MJPEG_MODE ) {
			//NOT DONE JET
			//cbcrimg = player.getframe();
		} else {
			
		}
		
		
		Server.cbcrimg.setData(Raster.createRaster(Server.cbcrimg.getSampleModel(), new DataBufferInt(Server.kep, Server.kep.length), new Point() ) );
	    //System.out.println(elapsedTime);
	    client.done =true;
		}

	}
	
	public static double[] multiply(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }



}
