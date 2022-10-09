package lab1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client {

	public static void main(String[] args) {
	
	//GUI application
	JFrame frame = new JFrame();
	frame.setBounds(300, 100, 550, 550);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setTitle("Calculator assignment" );
	frame.getContentPane().setLayout(null);
	frame.setVisible(true);
	JLabel label_1 = new JLabel("");
	label_1.setBounds(20, 20, 100, 20);
	label_1.setFont(new Font("Times", Font.BOLD, 12));
	label_1.setHorizontalAlignment(SwingConstants.LEFT);
	label_1.setVerticalAlignment(SwingConstants.CENTER);
	frame.getContentPane().add(label_1);
	label_1.setText("First Number:");

	
	JLabel label_2 = new JLabel("");
	label_2.setBounds(20, 55, 100, 20);
	label_2.setFont(new Font("Times", Font.BOLD, 12));
	label_2.setHorizontalAlignment(SwingConstants.LEFT);
	label_2.setVerticalAlignment(SwingConstants.CENTER);
	frame.getContentPane().add(label_2);
	label_2.setText("Second Number:");
	
	JTextField textfield1= new JTextField("");
	textfield1.setFont(new Font("Times", Font.BOLD, 12));
	textfield1.setBounds(140, 20, 155, 20);
	frame.getContentPane().add(textfield1);
			
	JTextField textfield2= new JTextField("");
	textfield2.setFont(new Font("Times", Font.BOLD, 12));
	textfield2.setBounds(140, 55, 155, 20);
	frame.getContentPane().add(textfield2);
	
	JButton button_2 = new JButton("+");
	button_2.setBounds(20, 100, 60, 23);
	button_2.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_2);
	JButton button_3 = new JButton("-");
	button_3.setBounds(90, 100, 60, 23);
	button_3.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_3);
	JButton button_4 = new JButton("×");
	button_4.setBounds(160, 100, 60, 23);
	button_4.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_4);
	JButton button_5 = new JButton("÷");
	button_5.setBounds(230, 100, 60, 23);
	button_5.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_5);
	JButton button_6 = new JButton("Max");
	button_6.setBounds(300, 100, 60, 23);
	button_6.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_6);
	JButton button_7 = new JButton("Min");
	button_7.setBounds(370, 100, 60, 23);
	button_7.setFont(new Font("Times", Font.BOLD, 12));
	frame.getContentPane().add(button_7);
	
	JLabel label_3 = new JLabel();
	label_3.setBounds(20, 200, 500, 20);
	label_3.setFont(new Font("Times",Font.BOLD,16));
	label_3.setHorizontalAlignment(SwingConstants.LEFT);
	label_3.setVerticalAlignment(SwingConstants.CENTER);
	frame.getContentPane().add(label_3);
	
	
	JLabel label_4 = new JLabel();
	label_4.setBounds(350, 20, 300, 20);
	label_4.setFont(new Font("Times",Font.BOLD,12));
	label_4.setForeground(Color.red);
	label_4.setHorizontalAlignment(SwingConstants.LEFT);
	label_4.setVerticalAlignment(SwingConstants.CENTER);
	frame.getContentPane().add(label_4);
	
	JLabel label_5 = new JLabel();
	label_5.setBounds(350, 55, 300, 20);
	label_5.setFont(new Font("Times",Font.BOLD,12));
	label_5.setForeground(Color.red);
	label_5.setHorizontalAlignment(SwingConstants.LEFT);
	label_5.setVerticalAlignment(SwingConstants.CENTER);
	frame.getContentPane().add(label_5);
	
	
	try {
	//Set the connection
	DatagramSocket clientSocket = new DatagramSocket();
	
	//Send to server
	InetAddress IPAddress = InetAddress.getByName("localhost");
	int serverPort = 9999;
	
	
		button_2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		try {
			String opr="+";
			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);
			String answer=new String(data_2,0,fromServer.getLength());
			
			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+textfield1.getText()+" + "+textfield2.getText()+" = "+answer);
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		}

		});

		
	button_3.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
			String opr="-";

			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);	
			String answer=new String(data_2,0,fromServer.getLength());
			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+textfield1.getText()+" - "+textfield2.getText()+" = "+answer);
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}
			
			catch(IOException ex) {
			System.out.println(ex.getMessage());
			}
		}
	});
	
	button_4.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
			String opr="*";
			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);
			String answer=new String(data_2,0,fromServer.getLength());
			
			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+textfield1.getText()+" * "+textfield2.getText()+" = "+answer);
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}
			catch(IOException ex) {
			System.out.println(ex.getMessage());
			}
		}
	});
	
	button_5.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		try {
			String opr="/";
			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);
			String answer=new String(data_2,0,fromServer.getLength());
			
			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+textfield1.getText()+" / "+textfield2.getText()+" = "+answer);
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}
		catch(IOException ex) {
		System.out.println(ex.getMessage());
		}
		}
		});
	
	button_6.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		try {
			//A recognized as max
			String opr="A";
			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);
			String answer=new String(data_2,0,fromServer.getLength());
			
			
		
			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+answer+" is the max between "+textfield1.getText()+" is "+textfield2.getText());
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}		
		catch(IOException ex) {
		System.out.println(ex.getMessage());
		}	
		}
		});
	button_7.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
			// I recognized as min
			String opr="I";
			opr+=textfield1.getText()+'|'+textfield2.getText();
			byte[] data_1 = new byte[1024];
			data_1 = opr.getBytes();
			DatagramPacket fromClient = new DatagramPacket(data_1, data_1.length,IPAddress,serverPort);
			clientSocket.send(fromClient);
			
			byte[] data_2=new byte[1024];
			DatagramPacket fromServer=new DatagramPacket(data_2, data_2.length);
			clientSocket.receive(fromServer);
			String answer=new String(data_2,0,fromServer.getLength());

			if(answer.charAt(0)!='x' && answer.charAt(0)!='y' && answer.charAt(0)!='z') {
			label_3.setText("ANSWER: "+answer+" is the min between "+textfield1.getText()+" is "+textfield2.getText());
			label_4.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='x') {
			label_4.setText("ERROR 404: You did not enter a valid number!");
			label_3.setText("");
			label_5.setText("");
			}
			else if(answer.charAt(0)=='y') {
			label_4.setText("");
			label_3.setText("");
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			else if(answer.charAt(0)=='z') {
			label_3.setText("");
			label_4.setText("ERROR 404: You did not enter a valid number!");	
			label_5.setText("ERROR 404: You did not enter a valid number!");}
			}
		catch(IOException ex) {
		System.out.println(ex.getMessage());
		}	
		}
		});
	
	if(frame.isActive()==false)
	clientSocket.close();
	}
	
	catch(SocketException ex){
		System.out.println(ex.getMessage());
	}	
	catch(UnknownHostException ex){
		System.out.println(ex.getMessage());
	}
	}
	}
