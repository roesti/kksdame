import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class DameServer implements Runnable
{
    private volatile boolean done = false;
    public static final int maxClients = 50;

    private ArrayList<DameServerThread> clients = new ArrayList<DameServerThread>();
    //private DameServerThread clients[] = new DameServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private ScheduledExecutorService scheduledExecutor;
    private ArrayList<int[]> games = new ArrayList<int[]>();

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

        System.out.println("Warten auf Clients ...");

        while (thread != null && !this.done)
        {
            try
            {
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
    public synchronized void checkIdleClients()
    {
        ArrayList<DameServerThread> removeClients = new ArrayList<DameServerThread>();

        for (DameServerThread client : this.clients)
        {

            if (this.clients.size() < 1)
            {
                System.out.println("passiert das?");
            }

            if (client != null)
            {
                long time_passed = (Calendar.getInstance().getTime().getTime() - client.getIdleTime()) / 1000;

                if (time_passed > 600)
                {
                    removeClients.add(client);
                }
            }
        }

        for (DameServerThread client : removeClients)
        {
            this.disconnectClient(client, true);
        }

    }

    public synchronized void disconnectClient(DameServerThread client, boolean send_back_to_client)
    {
        if (client != null)
        {
            if (send_back_to_client)
            {
                client.send("DISCONNECT");
            }

            this.remove(client.getID());
        }

    }

    // Broadcast an alle Clients mit allen offenen Verbindungen
    public void propagateUsers()
    {
        boolean first = true;
        String propagateUserString = "PROPAGATEUSERS|";

        if (this.clients.size() > 0)
        {
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

        System.out.println("RECV FROM " + ID + ":" + input);

        if (action.equals("CHAT"))
        {

            String name = this.findClient(ID).getUsername();
            String value = input_splitted[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String time = dateFormat.format(calendar.getTime());
            String color = this.findClient(ID).getColorString();

            System.out.println("BROADCAST SEND: " + "CHAT|" + time + "|" + color + "|" + name + "|" + value);
            for (DameServerThread client : this.clients)
            {
                client.send("CHAT|" + time + "|" + color + "|" + name + "|" + value);
            }
        }
        else if (input.equals("DISCONNECT"))
        {
            
            
            // Prüfen, ob Spiel offen ist, das somit verlassen wurde ...
            
            int id_opponent = 0;
            int remove_index = 0;
            int i = 0;

            for (int[] game : this.games)
            {
                if (game[0] == ID)
                {
                    remove_index = i;
                    id_opponent = game[1];
                }
                else if (game[1] == ID)
                {
                    id_opponent = game[0];
                }

                i++;
            }

            if (id_opponent != 0)
            {
                this.games.remove(remove_index);
                this.findClient(id_opponent).setIsPlaying(false);

                this.findClient(id_opponent).send("OPPONENT_QUIT");
                System.out.println("SEND TO " + id_opponent + ": " + "OPPONENT_QUIT");
            }
            
            this.disconnectClient(this.findClient(ID), false);
        }
        else if (action.equals("SETUSERNAME"))
        {
            String username = input_splitted[1];
            this.findClient(ID).setUsername(username);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String time = dateFormat.format(calendar.getTime());

            //this.findClient(ID).send("CHAT|" + time + "|#35FF2B|++DAME SERVER++|Willkommen auf DameServer 0.1 - have fun!");
            //System.out.println("SEND TO " + ID + ": " + "CHAT|" + time + "|#35FF2B|++DAME SERVER++|Willkommen auf DameServer 0.1 - have fun!");
        }
        else if (action.equals("GET_ID_SELF"))
        {
            this.findClient(ID).send("SEND_ID_SELF|" + ID);
            System.out.println("SEND TO " + ID + ": " + "SEND_ID_SELF|" + ID);
        }
        else if (action.equals("CHALLENGE_REQUEST"))
        {
            int id_opponent = Integer.parseInt(input_splitted[1]);
            this.findClient(ID).setIsPlaying(true);
            this.findClient(id_opponent).setIsPlaying(true);
            this.findClient(id_opponent).send("CHALLENGE_RECEIVED|" + ID);
            System.out.println("SEND TO " + id_opponent + ": " + "CHALLENGE_RECEIVED|" + ID);
        }
        else if (action.equals("CHALLENGE_REQUEST_CANCEL"))
        {
            int id_opponent = Integer.parseInt(input_splitted[1]);
            this.findClient(ID).setIsPlaying(false);
            this.findClient(id_opponent).setIsPlaying(false);       
            this.findClient(id_opponent).send("CHALLENGE_REQUEST_CANCELLED|" + ID);
            System.out.println("SEND TO " + id_opponent + ": " + "CHALLENGE_REQUEST_CANCELLED|" + ID);
        }
        else if (action.equals("CHALLENGE_REQUEST_DECLINE"))
        {
            int id_opponent = Integer.parseInt(input_splitted[1]);
            this.findClient(ID).setIsPlaying(false);
            this.findClient(id_opponent).setIsPlaying(false);
            this.findClient(id_opponent).send("CHALLENGE_REQUEST_DECLINED|" + ID);
            System.out.println("SEND TO " + id_opponent + ": " + "CHALLENGE_REQUEST_DECLINED|" + ID);
        }
        else if (action.equals("CHALLENGE_REQUEST_ACCEPT"))
        {
            int id_opponent = Integer.parseInt(input_splitted[1]);
            this.findClient(id_opponent).send("CHALLENGE_REQUEST_ACCEPTED|" + ID);
            System.out.println("SEND TO " + id_opponent + ": " + "CHALLENGE_REQUEST_ACCEPTED|" + ID);

            int get_random_player = Tools.getRandomInt(0, 1);

            String player1_color = "w";
            String player2_color = "s";

            if (get_random_player == 0)
            {
                player1_color = "s";
                player2_color = "w";
            }

            int game[] = new int[2];
            game[0] = id_opponent;
            game[1] = ID;

            this.games.add(game);

            for (DameServerThread client : this.clients)
            {
                if (client.getID() == id_opponent || client.getID() == ID)
                {
                    client.send("GAME_START|" + this.findClient(id_opponent).getUsername() + "|" + player1_color + "|" + this.findClient(ID).getUsername() + "|" + player2_color + "|" + id_opponent + "|" + ID);
                    System.out.println("SEND TO " + client.getID() + ": " + "GAME_START|" + this.findClient(id_opponent).getUsername() + "|" + player1_color + "|" + this.findClient(ID).getUsername() + "|" + player2_color + "|" + id_opponent + "|" + ID);
                } 
            }
        }
        else if (action.equals("MOVE"))
        {
            int id_opponent = 0;

            for (int[] game : this.games)
            {
                if (game[0] == ID)
                {
                    id_opponent = game[1];
                }
                else if (game[1] == ID)
                {
                    id_opponent = game[0];
                }
            }

            if (id_opponent != 0)
            {
                String stein_zeile = input_splitted[1];
                String stein_spalte = input_splitted[2];
                String pos_zeile =  input_splitted[3];
                String pos_spalte =  input_splitted[4];

                this.findClient(id_opponent).send("OPPONENT_MOVED|" + stein_zeile + "|" + stein_spalte + "|" + pos_zeile + "|" + pos_spalte);
                System.out.println("SEND TO " + id_opponent + ": " + "OPPONENT_MOVED|" + stein_zeile + "|" + stein_spalte + "|" + pos_zeile + "|" + pos_spalte);
            }
        }
        else if (action.equals("END_TURN"))
        {
            int id_opponent = 0;

            for (int[] game : this.games)
            {
                if (game[0] == ID)
                {
                    id_opponent = game[1];
                }
                else if (game[1] == ID)
                {
                    id_opponent = game[0];
                }
            }

            if (id_opponent != 0)
            {
                this.findClient(id_opponent).send("OPPONENT_ENDED_TURN");
                System.out.println("SEND TO " + id_opponent + ": " + "OPPONENT_ENDED_TURN");
            }
        }
        else if (action.equals("GAME_END"))
        {
            int id_opponent = 0;

            int remove_index = 0;

            int i = 0;

            for (int[] game : this.games)
            {
                if (game[0] == ID)
                {
                    remove_index = i;
                    id_opponent = game[1];
                }
                else if (game[1] == ID)
                {
                    id_opponent = game[0];
                }

                i++;
            }

            if (id_opponent != 0)
            {
                this.games.remove(remove_index);
                this.findClient(ID).setIsPlaying(false);
                this.findClient(id_opponent).setIsPlaying(false);

                this.findClient(id_opponent).send("GAME_ENDED");
                System.out.println("SEND TO " + id_opponent + ": " + "GAME_ENDED");
                this.findClient(ID).send("GAME_ENDED");
                System.out.println("SEND TO " + ID + ": " + "GAME_ENDED");
            }
        }
        else if (action.equals("GIVE_UP"))
        {
            int id_opponent = 0;

            int remove_index = 0;

            int i = 0;

            for (int[] game : this.games)
            {
                if (game[0] == ID)
                {
                    remove_index = i;
                    id_opponent = game[1];
                }
                else if (game[1] == ID)
                {
                    id_opponent = game[0];
                }

                i++;
            }

            if (id_opponent != 0)
            {
                this.games.remove(remove_index);
                this.findClient(ID).setIsPlaying(false);
                this.findClient(id_opponent).setIsPlaying(false);

                this.findClient(id_opponent).send("OPPONENT_QUIT");
                System.out.println("SEND TO " + id_opponent + ": " + "OPPONENT_QUIT");
            }
        }
    }

    public synchronized void remove(int ID)
    {
        try
        {
            DameServerThread clientToRemove = findClient(ID);

            if (clientToRemove != null)
            {
                DameServerThread toTerminate = clientToRemove;
                this.clients.remove(toTerminate);

                System.out.println("Entferne Client Thread " + ID);

                try
                {
                    toTerminate.close();
                }
                catch(IOException ioe)
                {
                    System.out.println("Fehler beim Schließen des Threads: " + ioe);
                }

            }
        }
        catch(Exception ioe)
        {
            System.out.println("Fehler beim Schließen des Threads: " + ioe);
        }

    }

    private void addThread(Socket socket)
    {
        if (this.clients.size() < DameServer.maxClients)
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