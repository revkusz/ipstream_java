package udpstream.com.revkusz.udpserver;

import java.awt.Checkbox;
import java.awt.Label;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

public class client implements Runnable {
	
	DatagramSocket clientSocket;
	static InetAddress IPAddress;
	DatagramPacket receivePacket;
	Server server;
	byte[] img;
	boolean run = true;
	boolean isRun = true;
	static boolean done = true;
	static byte[][] receiveData;
	static int[] receive_data_lenght;
	static String ipaddress;
	
	InetSocketAddress serverAddress;
	
	Checkbox csekbox;
	Label cim;
	
	
	@Override
	public synchronized  void run() {	
		
		byte[] img = new byte[(Server.WIDTH*Server.HEIGHT*4*2)];
		//byte[] img = new byte[921600*2];
		System.out.println("Receive started...");
		 
		//receivePacket[1] = new DatagramPacket(receiveData[1], receiveData.length);
		try {
			IPAddress = InetAddress.getByName("192.168.200.33");
			ipaddress = "192.168.200.33";
			//IPAddress = InetAddress.getByName("192.168.200.202");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		String data = "get w 640 h 480 x 0 y 0";
		byte[] sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 52000);
		//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9999);
		try { 
		    
		    while(true) {  
				if (isRun) {
					clientSocket.send(sendPacket);
			    	run=true;
			    	int i = 0;
			    	int k = 0 ;
			    	long startTime = System.currentTimeMillis();
			    	while(run){
			    		try {
			    			receivePacket = new DatagramPacket(receiveData[k], receiveData[k].length);
				            clientSocket.receive(receivePacket);
				            receive_data_lenght[k]=receivePacket.getLength();

				        } catch (SocketTimeoutException e) {
				           run = false;
				        }
			    		if (run ) {
			    			k++;
			    		}
			    		
			    			    		
			    	} 
			    	for (int l = 0; l<=k; l++){
			    		for (int p = 0; p<receive_data_lenght[l];p++) {
			    			img[i] = receiveData[l][p];
			    			i++;
			    		}
			    	}
			    	/*cim.setText(String.valueOf(i));
			    	if (i<Server.WIDTH*Server.HEIGHT*4) {
			    		while (i!=Server.WIDTH*Server.HEIGHT*4) {
			    			img[i]=0;
			    			i++;
			    		}
			    	}*/
			    	//System.out.println("time out");
			    	//server.setImage(img);
			    	 Thread thread = new Thread(new SetImage(server,img));  
			    	 thread.start();
					
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
	
	client(Server stream,String ip,Checkbox checkbox,Label label) {
		receiveData = new byte[2000][1518];
		receive_data_lenght = new int[2000];
		server = stream;
		cim=label;
		csekbox = checkbox;
		done = true;
		ipaddress =ip;
		try {
			IPAddress = InetAddress.getByName(ip);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//img = new byte [Server.WIDTH*Server.HEIGHT*3];
		try {
			
			clientSocket = new DatagramSocket(null);
			//JOptionPane.showMessageDialog(null, clientSocket.getReceiveBufferSize(), "InfoBox:ReceiveBufferSize ", JOptionPane.INFORMATION_MESSAGE);
			clientSocket.setReceiveBufferSize(1228800);
			//JOptionPane.showMessageDialog(null, clientSocket.getReceiveBufferSize(), "InfoBox:ReceiveBufferSize ", JOptionPane.INFORMATION_MESSAGE);
			clientSocket.bind(new InetSocketAddress(52002));		
			clientSocket.setSoTimeout(100);//30
			//clientSocket.connect(new InetSocketAddress(IPAddress, 52002));
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
	
	public static void generate_frame(byte[] frame,int frame1) {
		int col;
		int cbar;
		int pixel= 0;
		   for ( col = 0; col < Server.WIDTH*4*Server.HEIGHT; col+=4)
			{
			   cbar = ((col * 3)+frame1) / Server.WIDTH; // color bar  0..7
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

			   frame[col]=(byte)  (pixel >> 16 & 0xFF);
			   frame[col+1]=(byte) (pixel >> 8 & 0xFF);
			   frame[col+2]=(byte) (pixel & 0xFF);
			   frame[col+3]=0;
			   //System.out.println(Integer.toHexString(pixel));
			
			}
	}
	
	
	public static void TCPSendString(){
		String data =  JOptionPane.showInputDialog("Add meg a küldeni kivánt parancsot");
		SocketChannel client;
		 ByteBuffer bytebuf = ByteBuffer.allocate(1024);
		try {
			client = SocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(ipaddress, 7);
	        client.connect(isa);
	        client.configureBlocking(false);
	        bytebuf = ByteBuffer.wrap(data.getBytes());
	        client.write(bytebuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
