import java.net.*;
import java.io.*;

// Wrapper Klasse - kommuniziert mit dem ClientThread, der im Hintergrund Daten vom Server empfängt
// bzw. diese an den Server sendet

public class DameClient
{
    private Socket socket                    = null;
    private Thread thread                    = null;
    private DataInputStream  clientInput     = null;
    private DataOutputStream serverStream    = null;
    private DameClientThread client          = null;
    private NetzwerkLobby lobby              = null;

    public DameClient(String serverName, int serverPort, NetzwerkLobby lobby) throws UnknownHostException, IOException
    {
        try
        {
            socket = new Socket(serverName, serverPort);
            this.clientInput = new DataInputStream(System.in);
            this.serverStream = new DataOutputStream(socket.getOutputStream());
            this.lobby = lobby;

            this.client = new DameClientThread(this, socket);
        }
        catch(UnknownHostException uhe)
        {
            throw uhe;
        }
        catch(IOException ioe)
        {
            throw ioe;
        }
    }

    public void sendMessageToServer(String message)
    {
        try
        {
            serverStream.writeUTF(message);
            serverStream.flush();
        }
        catch(IOException ioe)
        { 
            System.out.println("Sending error: " + ioe.getMessage());
        }
    }

    

    // Wird ausgeführt, wenn vom Server bzw. vom ClientThread etwas zurückkommt..
    public synchronized void handle(String msg)
    {
        String action = msg.split("\\|")[0];
        
        if (action.equals("CHAT"))
        {
            String time = msg.split("\\|")[1];
            String color = msg.split("\\|")[2];
            String name = msg.split("\\|")[3];
            String message = msg.split("\\|")[4];
            
            this.receiveChatMessage(time, color, name, message);
        }
        else if (action.equals("PROPAGATEUSERS"))
        {
            String client_string = msg.split("\\|")[1];
            this.receiveClientList(client_string);
        }
    }
    
    public synchronized void receiveClientList(String client_list)
    {
        this.lobby.updateClientList(client_list);
    }
    
    public synchronized void receiveChatMessage(String time, String color, String name, String message)
    {
        this.lobby.writeToChat(time, color, name, message.replaceAll("PIPE_CHARACTER", "\\|"));
    }
}