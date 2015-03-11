import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class DameServerThread extends Thread
{
    private DameServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
    private String color_string = null;
    private long lastIdleTimestamp = 0;
    private String userName;
    private boolean isPlaying;
    private volatile boolean done;

    public void shutdown()
    {
        done = true;
    }

    public DameServerThread(DameServer server, Socket socket)
    {
        super();
        this.done = false;
        this.server = server;
        this.socket = socket;
        this.ID = this.socket.getPort();

        this.isPlaying = false;

        boolean color_found = true;

        while (color_found == true)
        {
            String color = Tools.getRandomColor();

            boolean not_found = true;

            for (DameServerThread client : server.getClients())
            {
                if (client.getColorString().equals(color))
                {
                    not_found = false;
                }
            }

            if (not_found)
            {
                color_found = false;
                this.color_string = color;
            }
        }
    }

    public void setIsPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPlaying()
    {
        return this.isPlaying;
    }

    public void setUsername(String username)
    {
        this.userName = username;
    }

    public String getUsername()
    {
        return this.userName;
    }

    public void updateIdleTime()
    {
        this.lastIdleTimestamp = Calendar.getInstance().getTime().getTime();
    }

    public long getIdleTime()
    {
        return this.lastIdleTimestamp;
    }

    public String getColorString()
    {
        return this.color_string;
    }

    public void send(String msg)
    {
        if (this.done == false)
        {
            try
            {  
                this.streamOut.writeUTF(msg);
                this.streamOut.flush();
            }
            catch(IOException ioe)
            {  
                System.out.println(ID + " ERROR sending: " + ioe.getMessage());
                this.server.remove(ID);
                this.shutdown();
            }
        }

    }

    public int getID()
    { 
        return this.ID;
    }

    public void run()
    { 
        System.out.println("Server Thread für Client " + ID + " läuft.");

        while (!this.done)
        {
            try
            { 
                this.server.handle(this.ID, this.streamIn.readUTF());
            }
            catch(IOException ioe)
            {  
                System.out.println(ID + " ERROR reading: " + ioe.getMessage() + socket + streamIn + done);
                this.server.remove(ID);
                this.shutdown();
            }

        }
    }

    public void open() throws IOException
    {

        this.done = false;

        this.streamIn = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        this.streamOut = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));

    }

    public void close() throws IOException
    {
        this.stop();
        
        if (this.socket != null)
        {
            this.socket.close();
        }
    }
}