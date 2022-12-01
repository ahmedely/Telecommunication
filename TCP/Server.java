import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server {
	
	private static ArrayList<Thread_Client> Clients = new ArrayList<Thread_Client>();
    private static int count = 0;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame ("Chatting Server");
        frame.setLayout(null);
        frame.setBounds(100, 100, 350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel connectionsLabel = new JLabel("No Clients Connected");
        connectionsLabel.setBounds(100, 30, 200, 30);
        connectionsLabel.setForeground(Color.red);
        frame.getContentPane().add(connectionsLabel);
        
        frame.setVisible(true);
        
        ServerSocket welcomeSocket = new ServerSocket(6789);
        
        // For new Client Connections
        new Thread (new Runnable(){ @Override
            public void run() {
        	
        	Socket connectionSocket;
            DataOutputStream ToClient;
            
            while(!welcomeSocket.isClosed())
            {
        	
            	try
            	{
            		connectionSocket = welcomeSocket.accept();

            		ToClient = new DataOutputStream(connectionSocket.getOutputStream());
            		ToClient.writeBytes("-Connected\n");
            		
            		count++;
            		
            		Clients.add(new Thread_Client(count, connectionSocket, Clients));
            		Clients.get(Clients.size() - 1).start();
        		
            	}
            	catch(Exception t1)
            	{
            		System.out.println("Error from first thread in Server");
            	}
            	
            }
        	
        }}).start();
        
        // Get the total number of clients and show it on the Server GUI label
        new Thread (new Runnable(){ @Override
            public void run() {
        	
        	try
        	{
        		while(true)
        		{
        			if (Clients.size() > 0)
                    {
                        if (Clients.size() == 1)
                        {
                        	connectionsLabel.setText("1 Client Connected");
                        	connectionsLabel.setForeground(Color.blue);
                        }
                        else
                        {
                        	connectionsLabel.setText(Clients.size() + " Clients Connected");
                            connectionsLabel.setForeground(Color.blue);
                        }
                    }
                    else 
                    { 
                    	connectionsLabel.setText("No Clients Connected");
                    	connectionsLabel.setForeground(Color.red);
                    }
        			
        			Thread.sleep(1000);
        		}
        		
        	}
        	catch (Exception t2)
        	{
        		System.out.println("Error from second thread in Server");
        	}
        	
        }}).start();


	}
	
	//Send message that a Client has Joined the chat room
	public static void ClientJoinedMessage(String name) throws IOException
	{
		DataOutputStream ToClient;
		
        for (int i=0; i < Clients.size(); i++)
        {
        	ToClient = new DataOutputStream(Clients.get(i).clientSocket.getOutputStream());
        	ToClient.writeBytes("-Joined," + name + "\n");
        }
	}
	
	//Send Message to Specific Client or All Clients
	public static void sendMessageToOtherClients(String message, String To_NameofClient,String fromClient) throws IOException 
	{
        DataOutputStream ToClient;
        
        if (To_NameofClient.equals("EVERY_CLIENT")) 
        {
            for (int i=0; i < Clients.size(); i++) 
            {
            	ToClient = new DataOutputStream(Clients.get(i).clientSocket.getOutputStream());
            	ToClient.writeBytes("-Message," + message + "," + fromClient + "\n");
            }
        }

        else 
        {
            for (int i=0; i < Clients.size(); i++) 
            {
                if (Clients.get(i).name.equals(To_NameofClient)) {
                	ToClient = new DataOutputStream(Clients.get(i).clientSocket.getOutputStream());
                	ToClient.writeBytes("-Message," + message + "," + fromClient + "\n");
                }
            }
        }

    }
	
	//Send message that a Client has disconnected from the chat room
	public static void ClientDisconnectMessage(String name) throws IOException 
	{
        DataOutputStream ToClient;
        
        for (int i=0; i < Clients.size(); i++) 
        {
        	ToClient = new DataOutputStream(Clients.get(i).clientSocket.getOutputStream());
        	ToClient.writeBytes("-Disconnect," + name + "\n");
        }
    }

}
