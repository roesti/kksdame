import java.net.*;
import java.io.*;

public class DameClientThread extends Thread
{
    private Socket socket = null;
    private DameClient client = null;
    private DataInputStream streamIn = null;

    public DameClientThread(DameClient client, Socket socket)
    {
        this.client   = client;
        this.socket   = socket;
        this.open();  
        this.start();
    }

    public void open()
    {
        try
        {
            this.streamIn  = new DataInputStream(socket.getInputStream());
        }
        catch(IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            //client.stop();
        }
    }

    public void close()
    { 
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch(IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {  
                client.handle(streamIn.readUTF());
            }
            catch(IOException ioe)
            {
                //System.out.println("Listening error: " + ioe.getMessage());
                //client.stop();
            }
        }
    }
}