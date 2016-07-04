package udpstream.com.revkusz.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class client implements Runnable {
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	DatagramPacket receivePacket;
	byte[] img;
	boolean run = true;
	boolean done;
	
	
	@Override
	public void run() {	
		byte[] img = new byte[(Server.WIDTH*Server.HEIGHT*4)];
		System.out.println("Receive started...");
		byte[] receiveData = new byte[1514];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			IPAddress = InetAddress.getByName("192.168.200.33");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		String data = "get w 640 h 480 x 0 y 0";
		byte[] sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000);
		try { 
		    
		    while(true) {  	
				clientSocket.send(sendPacket);
		    	run=true;
		    	int i = 0;
		    	while(run){
		    		try {
			            clientSocket.receive(receivePacket);
			            /*if (i ==	0)
		    				clientSocket.setSoTimeout(1);*/
			        } catch (SocketTimeoutException e) {
			           run = false;
			        }
		    		if (run) {
		    			int lenght = receivePacket.getLength();
		    			for (int j = 0; j<lenght; j++) {
			    			img[i]=receiveData[j];		   
			    			i++;
			    			if (i%1000 == 0 ) {
			    				//System.out.println(i);
			    			}
		    			}
		    		}
		    			    		
		    	}  
		    	//System.out.println("time out");
				Server.setImage(img);
		    }
		} catch (IOException e) {
			
		}
		clientSocket.close();
	}
	
	client() {
		
		//img = new byte [Server.WIDTH*Server.HEIGHT*3];
		try {
			clientSocket = new DatagramSocket(4002);
			clientSocket.setSoTimeout(30);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}

		//IPAddress = InetAddress.getByName("192.168.200.239");
		
		System.out.println("Begin package sent");
	    
	}

}
