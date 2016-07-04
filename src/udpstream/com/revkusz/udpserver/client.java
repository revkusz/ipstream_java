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
	Server server;
	byte[] img;
	boolean run = true;
	boolean isRun = true;
	boolean done;
	
	
	@Override
	public void run() {	
		byte[] img = new byte[(Server.WIDTH*Server.HEIGHT*4*2)];
		System.out.println("Receive started...");
		byte[] receiveData = new byte[1518];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			IPAddress = InetAddress.getByName("192.168.200.33");
			//IPAddress = InetAddress.getByName("192.168.200.202");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		String data = "get w 640 h 480 x 0 y 0";
		byte[] sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000);
		try { 
		    
		    while(true) {  
		    	
				if (isRun) {
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
					server.setImage(img);
				} else {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
		    }
		} catch (IOException e) {
		
		}
		//clientSocket.close();
	}
	
	client(Server stream) {
		server = stream;
		//img = new byte [Server.WIDTH*Server.HEIGHT*3];
		try {
			clientSocket = new DatagramSocket(4002);
			clientSocket.setSoTimeout(100);//30
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	    
	}
	
	public void stop() {
		isRun = false;
	}
	public void start() {
		isRun = true;
	}

}
