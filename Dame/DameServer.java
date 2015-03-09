import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class DameServer implements Runnable
{
    private ArrayList<DameServerThread> clients = new ArrayList<DameServerThread>();
    //private DameServerThread clients[] = new DameServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private int clientCount = 0;

    public DameServer(int port)
    {
        try
        {
            System.out.println("Starte DameServer für KKSDame 0.1");
            System.out.println("Binde an Port " + port + ", bitte warten  ...");
            server = new ServerSocket(port);  
            System.out.println("Server gestarted: " + server);
            start();
        }
        catch(IOException ioe)
        {
            System.out.println("Portbindung fehlgeschlagen " + port + ": " + ioe.getMessage());
        }
    }
    
    public synchronized ArrayList<DameServerThread> getClients()
    {
        return this.clients;
    }

    public void run()
    {
        while (thread != null)
        {
            try
            {
                System.out.println("Warten auf Clients ..."); 
                this.addThread(this.server.accept());
            }
            catch(IOException ioe)
            {
                System.out.println("Client konnte nicht akzeptiert werden: " + ioe); stop();
            }
        }
    }

    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this); 
            thread.start();
        }
    }

    public void stop()
    {
        if (thread != null)
        {
            thread.stop(); 
            thread = null;
        }
    }

    private DameServerThread findClient(int ID)
    {
        for (DameServerThread client : clients)
        {
            if (client.getID() == ID)
            {
                return client;
            }
        }
        
        return null;
    }
    
    // Abhandlung der Server-Kommandos, die an den Client gesendet werden.
    public synchronized void handle(int ID, String input)
    {
        String input_splitted[] = input.split("\\|");
        String action = input_splitted[0];
        String name = input_splitted[1];
        String value = input_splitted[2];
        
        if (action.equals("CHAT"))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String time = dateFormat.format(calendar.getTime());
            String color = this.findClient(ID).getColorString();
            
            for (DameServerThread client : clients)
            {
                client.send("CHAT|" + time + "|" + color + "|" + name + "|" + value);
            }
        }
        else if (input.equals(".bye"))
        {
            findClient(ID).send(".bye");
            remove(ID);
        }
        else if (action.equals("MOVE"))
        {
            /*for (int i = 0; i < clientCount; i++)
            {
                clients[i].send(ID + ": " + input);
            }*/    
        }
    }

    public synchronized void remove(int ID)
    {
        DameServerThread clientToRemove = findClient(ID);

        if (clientToRemove != null)
        {
            DameServerThread toTerminate = clientToRemove;
            System.out.println("Entferne Client Thread " + ID);

           
            clientCount--;

            try
            {
                toTerminate.close();
            }
            catch(IOException ioe)
            {
                System.out.println("Fehler beim Schließen des Threads: " + ioe);
            }

            toTerminate.stop();
        }
    }

    private void addThread(Socket socket)
    {
        if (clientCount < 50)
        {
            System.out.println("Client akzeptiert: " + socket);
            DameServerThread client = new DameServerThread(this, socket);
            clients.add(client);

            try
            {
                client.open(); 
                client.start();  
                clientCount++;
            }
            catch(IOException ioe)
            {
                System.out.println("Fehler beim Öffnen des Threads: " + ioe);
            }
        }
        else
        {
            System.out.println("Verbindung zurückgesetzt: Maximum von 50 erreicht.");
        }

    }

    public static void main(String args[])
    {
        DameServer server = null;
        if (args.length != 1)
        {
            System.out.println("Benutzung: java DameServer port");
        }
        else
        {
            server = new DameServer(Integer.parseInt(args[0]));
        }
    }
}