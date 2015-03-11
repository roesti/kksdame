import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class DameServer implements Runnable
{
    private volatile boolean done = false;

    private ArrayList<DameServerThread> clients = new ArrayList<DameServerThread>();
    //private DameServerThread clients[] = new DameServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private int clientCount = 0;
    private ScheduledExecutorService scheduledExecutor;

    public void shutdown()
    {
        this.done = true;
    }

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

    public ArrayList<DameServerThread> getClients()
    {
        return this.clients;
    }

    public void run()
    {
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new DameServerIdleHelper(this);

        // HintergrundTask starten, der in einem Zeitintervall sowohl die aktiven User an alle Clients sendet, als auch prüft, welche Clients zu lange geidlet haben (über 10 Minuten)
        this.scheduledExecutor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);

        while (thread != null && !this.done)
        {
            try
            {
                System.out.println("Warten auf Clients ..."); 
                this.addThread(this.server.accept());
            }
            catch(IOException ioe)
            {
                System.out.println("Client konnte nicht akzeptiert werden: " + ioe);
                this.shutdown();
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

    // Prüfung, welche Clients länger als 10 Minuten idlen -> Disconnect
    public void checkIdleClients()
    {
        for (DameServerThread client : this.clients)
        {
            
            if (this.clients.size() < 1)
            {
                System.out.println("passiert das?");
            }
            
            if (client != null)
            {
                long time_passed = (Calendar.getInstance().getTime().getTime() - client.getIdleTime()) / 1000;

                if (time_passed > 5)
                {
                    this.disconnectClient(client);
                }
            }

        }
    }

    public void disconnectClient(DameServerThread client)
    {
        if (client != null)
        {
            client.send("DISCONNECT");
            this.remove(client.getID());
        }

    }

    // Broadcast an alle Clients mit allen offenen Verbindungen
    public void propagateUsers()
    {
        boolean first = true;
        String propagateUserString = "PROPAGATEUSERS|";

        for (DameServerThread client: this.clients)
        {
            if (first)
            {
                first = false;
                propagateUserString += client.getID() + ";;;" + client.getUsername() + ";;;" + client.getColorString() + ";;;" + client.getIsPlaying();
            }
            else
            {
                propagateUserString += "%%%" + client.getID() + ";;;" + client.getUsername() + ";;;" + client.getColorString() + ";;;" + client.getIsPlaying();
            }
        }

        for (DameServerThread client: this.clients)
        {
            client.send(propagateUserString);
        }
    }

    private DameServerThread findClient(int ID)
    {
        for (DameServerThread client : this.clients)
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

        this.findClient(ID).updateIdleTime();

        String input_splitted[] = input.split("\\|");
        String action = input_splitted[0];

        System.out.println(input);

        if (action.equals("CHAT"))
        {

            String name = this.findClient(ID).getUsername();
            String value = input_splitted[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String time = dateFormat.format(calendar.getTime());
            String color = this.findClient(ID).getColorString();

            for (DameServerThread client : this.clients)
            {
                client.send("CHAT|" + time + "|" + color + "|" + name + "|" + value);
            }
        }
        else if (input.equals("DISCONNECT"))
        {
            this.disconnectClient(this.findClient(ID));
        }
        else if (action.equals("SETUSERNAME"))
        {
            String username = input_splitted[1];
            this.findClient(ID).setUsername(username);
        }
    }

    public void remove(int ID)
    {
        DameServerThread clientToRemove = findClient(ID);

        if (clientToRemove != null)
        {
            DameServerThread toTerminate = clientToRemove;
            this.clients.remove(toTerminate);
            
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
            
            

            toTerminate.shutdown();

            

            toTerminate = null;
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
            }
            catch(IOException ioe)
            {
                System.out.println("Fehler beim Erstellen des Threads: " + ioe);
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