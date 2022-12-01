import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	
	private static Socket clientSocket;
	private static String clientName = "";
	private static JTextArea receivetextarea;
	private static JTextArea sendtextarea;

	public static void main(String[] args) /*throws Exception*/ {
		// TODO Auto-generated method stub
		
		// Create GUI Frame
        JFrame frame = new JFrame ("Chatting Client");
        frame.setLayout(null);
        frame.setBounds(100, 100, 550, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        // Client Name label
        JLabel ClientNameLabel = new JLabel("Client Name");
        ClientNameLabel.setBounds(20, 40, 150, 30);
        frame.getContentPane().add(ClientNameLabel);

        // Input Field for Client Name
        JTextField clientnameTextField = new JTextField();
        clientnameTextField.setBounds(130, 40, 150, 30);
        frame.getContentPane().add(clientnameTextField);

        // Connect/Disconnect button
        JButton connect_disconnect_Button = new JButton("Connect");
        connect_disconnect_Button.setBounds(330, 40, 100, 30);
        frame.getContentPane().add(connect_disconnect_Button);
        
        // TextArea for Messages Received from Other Clients
        receivetextarea = new JTextArea();
        receivetextarea.setBounds(20, 100, 450, 330);
        receivetextarea.setEditable(false);
        frame.getContentPane().add(receivetextarea);
        receivetextarea.setVisible(false);
        
        // Send to label
        JLabel SendToLabel = new JLabel("Send to");
        SendToLabel.setBounds(20, 450, 150, 30);
        frame.getContentPane().add(SendToLabel);
        SendToLabel.setVisible(false);
        
        // Input Field for sending message to specific client
        JTextField sendto_specificclient_TextField = new JTextField();
        sendto_specificclient_TextField.setBounds(130, 450, 150, 30);
        frame.getContentPane().add(sendto_specificclient_TextField);
        sendto_specificclient_TextField.setVisible(false);
        
        // Send button
        JButton Send_Button = new JButton("Send");
        Send_Button.setBounds(400, 515, 100, 30);
        frame.getContentPane().add(Send_Button);
        Send_Button.setVisible(false);
        
       // TextArea for Messages Sent from Other Clients
        sendtextarea = new JTextArea();
        sendtextarea.setBounds(20, 500, 320, 45);
        sendtextarea.setEditable(true);
        frame.getContentPane().add(sendtextarea);
        sendtextarea.setVisible(false);
        
        frame.setVisible(true);
        
        
        //BUTTONS
        
        connect_disconnect_Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        	 try
        	 {
        		if(connect_disconnect_Button.getText().equals("Connect"))
        		{
        			clientSocket = new Socket("localhost",6789);
        			clientName = clientnameTextField.getText();
   
        			StartThread(); 
        			
        			clientnameTextField.setEditable(false);
        			receivetextarea.setVisible(true);
        			SendToLabel.setVisible(true);
        			sendtextarea.setVisible(true);
        			Send_Button.setVisible(true);
        			sendto_specificclient_TextField.setVisible(true);
        			
        			connect_disconnect_Button.setText("Disconnect");
            		
            		
        		}
        		else
        		{
        			DataOutputStream ToServer = new DataOutputStream (clientSocket.getOutputStream());

        			ToServer.writeBytes("-Disconnect," + clientName + "\n");

        			ToServer.writeBytes("-Remove\n");

                    
                    clientSocket.close();
                    clientName = "";

                    
                    clientnameTextField.setEditable(true);
                    receivetextarea.setVisible(false);
                    SendToLabel.setVisible(false);
                    sendtextarea.setVisible(false);
                    Send_Button.setVisible(false);
                    sendto_specificclient_TextField.setVisible(false);

                    connect_disconnect_Button.setText("Connect");
        		}
        	 }
        	 catch(Exception ex)
        	 {
        			System.out.println("Error in Connect Button");
        	 }
        		
        	
        }});
        
        Send_Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		try
        		{
        			String ToClient;
        			if(sendto_specificclient_TextField.getText().equals(""))
        			{
        				ToClient = "EVERY_CLIENT";
        			}
        			else
        			{
        				ToClient = sendto_specificclient_TextField.getText();
        			}
        			
        			DataOutputStream To_Client = new DataOutputStream (clientSocket.getOutputStream());
        			To_Client.writeBytes("-Message," + sendtextarea.getText() + "," + ToClient + "," + clientName + "\n");
        			
        			sendtextarea.setText("");
        			
        		}
        		catch(Exception s)
        		{
        			System.out.println("Error from Send Button");
        		}
        		
        	
        	}});
        
        frame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		
        		try
        		{
        			DataOutputStream ToServer = new DataOutputStream (clientSocket.getOutputStream());
                    ToServer.writeBytes("-Remove\n");
                    
                    clientName = "";
                    
                    clientnameTextField.setEditable(true);
                    receivetextarea.setVisible(false);
                    SendToLabel.setVisible(false);
                    sendtextarea.setVisible(false);
                    Send_Button.setVisible(false);
                    sendto_specificclient_TextField.setVisible(false);
                    connect_disconnect_Button.setText("Connect");
                    
                    System.exit(0);
                    
                    
        			
        		}
        		catch(Exception w)
        		{
        			System.out.println("Error in AddWindowListner");
        		}
         
        		
        		
        	}});
        

	}
	
	// Thread to read messages from server and print them in textArea
    private static void StartThread() {
        new Thread (new Runnable(){ @Override
        public void run() {
        	
        	try {
        		
        		
                DataOutputStream ToServer = new DataOutputStream (clientSocket.getOutputStream());
        		
        		
                BufferedReader FromServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
                String receivedSentence;
                
             
                while (true) {

                    receivedSentence = FromServer.readLine();

                    if (receivedSentence.startsWith("-Connected")) {
                        ToServer.writeBytes("-Name," + clientName + "\n");
                    }

                    else if (receivedSentence.startsWith("-Joined")) {
                        String []strings_array = receivedSentence.split(",");

                        if (strings_array[1].equals(clientName)) {
                        	receivetextarea.setText("You are connected." + "\n");
                        }

                        else {
                        	receivetextarea.append(strings_array[1] + " is connected." + "\n");
                        }
                    }

                    else if (receivedSentence.startsWith("-Message")) {
                        String []strings_array = receivedSentence.split(",");

                        if (strings_array[2].equals(clientName)) {
                        	receivetextarea.append("You: " + strings_array[1] + "\n");
                        }
                        else
                        	receivetextarea.append(strings_array[2] + ": " + strings_array[1] + "\n");
                    }

                    else if (receivedSentence.startsWith("-Disconnect")) {
                        String []strings_array = receivedSentence.split(",");

                        receivetextarea.append(strings_array[1] + " disconnected." + "\n");
                    }
                    else if(receivedSentence.startsWith("-Duplicate"))
              		{
              			String []strings_array = receivedSentence.split(",");
              			receivetextarea.setVisible(true);
              			receivetextarea.append(strings_array[1] + " already regietsered as a Client" + "\n");
              		}

                }

        	}
        	catch(Exception t)
        	{
        		System.out.println("Error from StartThread");
        	}
        	
        	
        }}).start();
    }
    
    




}
