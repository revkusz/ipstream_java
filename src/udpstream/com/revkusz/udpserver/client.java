package udpstream.com.revkusz.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class client implements Runnable {
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	DatagramPacket receivePacket;
	byte[] img;
	boolean run = true;
	boolean done;
	@Override
	public void run() {
		System.out.println("Receive started...");
		byte[] receiveData = new byte[Server.WIDTH*3];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		/*try {
			clientSocket.receive(receivePacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		while (run){
			try {
				done = false;
				//System.out.println("received packet");
				//if ((receiveData[0] == 7) && (receiveData[1] == 13)) {
					int i = 0;
					while (( i<Server.HEIGHT) && ((receiveData[0] != 7) && (receiveData[1] != 13))){
						clientSocket.receive(receivePacket);
						//System.out.println("received packet");
						for (int j=0; j<Server.WIDTH*3; j++){
					    	img[j+i*(Server.WIDTH*3)] =(byte) (receiveData[j] & 0xFF);
					    	//System.out.println(receiveData[j]);  
						}
						i++;
					}
					done = true;
				/*}*/
				if (done) {
					//System.out.println("Frame Done.");
					Server.setImage(img);
				}
				
			} catch (IOException e) {
			
			}
		}
		clientSocket.close();
	}
	
	client() {
		try {
		    byte[] sendData = new byte[1];
		    img = new byte [Server.WIDTH*Server.HEIGHT*3];
			sendData[0] = 120;
			
			try {
				clientSocket = new DatagramSocket(60501);
			} catch (SocketException ex) {
				ex.printStackTrace();
			}
			//IPAddress = InetAddress.getByName("192.168.200.239");
			IPAddress = InetAddress.getByName("192.168.1.10");
		    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		    clientSocket.send(sendPacket);
		    System.out.println("Begin package sent");
		    
		} catch (IOException e) {
			
		}
	    
	}

}
