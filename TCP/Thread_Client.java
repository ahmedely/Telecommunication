import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Thread_Client extends Thread {
	
	public String name;
	public int number;
	public Socket clientSocket;
	ArrayList<Thread_Client> Clients;
	
	public Thread_Client(int number, Socket clientSocket, ArrayList<Thread_Client> Clients) {

        this.number = number;
        this.clientSocket = clientSocket;
        this.Clients = Clients;
        this.name = "";

    }
	
	public void run() {

        try {

            
            BufferedReader FromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String Sentence_FromClient;
            DataOutputStream ToClient;

            
            while (true) {

            	Sentence_FromClient = FromClient.readLine();

                // check the start of the message

                if (Sentence_FromClient.startsWith("-Remove")) 
                { 
                    for (int i = 0; i < Clients.size(); i++) 
                    {
                        if (Clients.get(i).number == number) 
                        {
                            Clients.remove(i);
                        }
                    }
                }

                else if (Sentence_FromClient.startsWith("-Name")) 
                {
                    String []client_array = Sentence_FromClient.split(",");
                    name = client_array[1];

                    Server.ClientJoinedMessage(name);
                }

                else if (Sentence_FromClient.startsWith("-Message")) 
                {
                    String []string_array = Sentence_FromClient.split(",");

                    Server.sendMessageToOtherClients(string_array[1], string_array[2], string_array[3]);
                }

                else if (Sentence_FromClient.startsWith("-Disconnect")) 
                {
                    String []client_array = Sentence_FromClient.split(",");

                    Server.ClientDisconnectMessage(client_array[1]);
                }

            }

        } 
        catch (Exception ex) {
        	System.out.println("Error in Thread_Client Class");
        }

    }

	

}
