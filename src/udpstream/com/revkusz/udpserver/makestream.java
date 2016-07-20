package udpstream.com.revkusz.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class makestream {
	public static final int WIDTH = 320;
	public static final int HEIGHT = 200;

	public static void main(String[] args) {
		try {
		      String host = "localhost";
		      int port = 60501;
		      int[] frame = new int[WIDTH];    
		      int framecount = 0;
		      byte[] message = int_to_byte(frame);
		      // Get the internet address of the specified host
		      InetAddress address = InetAddress.getByName(host);

		      // Initialize a datagram packet with data and address
		      DatagramPacket packet = new DatagramPacket(message, message.length,
		          address, port);
		      DatagramSocket dsocket = new DatagramSocket(60502);
		      dsocket.send(packet);
		      
		      // Create a datagram socket, send the packet through it, close it.
		      while (framecount<=2000000) {
			      for (int i = 0 ; i<HEIGHT; i++) {
			    	  generate_frame(frame,framecount);
				      message = int_to_byte(frame);
				      packet.setData(message);
				      dsocket.send(packet);

			      }
			      //System.out.println("frame done");
			      framecount+=10;
		      }

		      //dsocket.send(packet);
		      System.out.println("sajt");
		      dsocket.close();
		    } catch (Exception e) {
		      System.err.println(e);
		    }
	}
	
	public static void generate_frame(int[] frame,int frame1) {
		int col;
		int cbar;
		int pixel= 0;
		   for ( col = 0; col < WIDTH; col++ )
			{
			   cbar = ((col * 3)+frame1) / WIDTH; // color bar  0..7
			   cbar = (cbar) % 3;

			   switch ( cbar )
			   {
				   case 0:
				   {
					   pixel = 0x00FF0000;
					  break; // Red
				   }
				   case 1:
				   {
					   pixel = 0x0000FF00;
					   break; // green
				   }
				   case 2:
				   {
					  pixel = 0x000000FF; // blue
					  break;
				   }
			   }
			   if (col==0){
				   pixel = 0x00112233;
			   }
			   frame[col]= pixel;
				//sSystem.out.println(Integer.toHexString(pixel));
			
			}
	}
	
	public static byte[] int_to_byte(int[] int_array) {
		byte[] byte_array = new byte[int_array.length*3];
		for (int i = 0; i<int_array.length*3; i+=3) {
			byte_array[i] =(byte) (int_array[i/3] >> 16 & 0xFF);
			byte_array[i+1] =(byte) (int_array[i/3] >> 8 & 0xFF);  
			byte_array[i+2] =(byte) (int_array[i/3] & 0xFF);  
		}
		return byte_array;
	}
	

}
