package lab1;


import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;



public class Server {

	public static void main(String[] args) {
	try {
		DatagramSocket serverSocket = new DatagramSocket(9999);
			
		while(true) {
			
			//receiving
		byte[] data_1 = new byte[1024];
		DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length);	
		serverSocket.receive(fromClient);
		String opReceived = new String(data_1,0,fromClient.getLength());	
		
		int index=0;
		for (int i = 0; i < fromClient.getLength(); i++) {
			if(opReceived.charAt(i)=='|')
				index=i;
		}
		String error=" ";
		String nmbr1=opReceived.substring(1,index);
		
		for (int i = 0; i < nmbr1.length(); i++) {
			if(Character.isDigit(nmbr1.charAt(i))==false) {
				error="x";
			}
		}
		String nmbr2=opReceived.substring(index+1,opReceived.length());
		for (int i = 0; i < nmbr2.length(); i++) {
			if(Character.isDigit(nmbr2.charAt(i))==false) {
				if(error=="x")
				error="z";
				else
					error="y";
			}
		}
		
		char operation=opReceived.charAt(0);

		if(error=="x"||error=="y"||error=="z") {
			byte[] data_2= new byte[1024];
			String sendMessage=String.valueOf(error);
			data_2=sendMessage.getBytes();
			InetAddress IPAddress = fromClient.getAddress();
			int clientPort = fromClient.getPort();
			DatagramPacket fromServer= new DatagramPacket(data_2,data_2.length, IPAddress,clientPort);
			serverSocket.send(fromServer);
			}
		else {
		float n1=Float.parseFloat(nmbr1);
		
		float n2=Float.parseFloat(nmbr2);
		float result=0;
		
		switch(operation) {
		case '-':
			result=n1-n2;
			break;
		case '*':
			result=n1*n2;
			break;
		case '/':
			result=n1/n2;
			break;
		case'A':
			if(n1>n2)
				result=n1;
			else
				result=n2;
			break;
		case 'I':
			if(n1<n2)
				result=n1;
			else
				result=n2;
			break;
		default: result=n1+n2;
		}
		byte[] data_2= new byte[1024];
		String sendMessage=String.valueOf(result);
		data_2=sendMessage.getBytes();
		InetAddress IPAddress = fromClient.getAddress();
		int clientPort = fromClient.getPort();
		DatagramPacket fromServer= new DatagramPacket(data_2,data_2.length, IPAddress,clientPort);
		serverSocket.send(fromServer);
		}	
		}
	}
	catch(Exception ex){
		System.out.println(ex.getMessage());
	}
}
	}
