import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.text.*;

/**
 * Write a description of class NetworkBrowser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NetzwerkLobby implements ActionListener, MouseListener
{
    // instance variables - replace the example below with your own
    private GUI gui;

    private JDialog mainWindow;
    //private JTextArea chatTextArea;
    private JTextPane chatScrollPane;
    private JLabel chatTitleLabel;
    private JLabel playerListTitleLabel;
    private JLabel chatTextLabel;
    private JList<String> playerList;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private JPopupMenu playerContextMenu;
    private JMenuItem menuItemChallengePlayer;

    private boolean disconnected;

    private DefaultListModel<String> clientListModel;

    private String spielerName;
    private DameClient networkClient;

    /**
     * Constructor for objects of class NetworkBrowser
     */
    public NetzwerkLobby(GUI gui)
    {
        // initialise instance variables
        this.gui = gui;

        this.disconnected = false;

        String spieler_name = "";
        String server_name = "";
        boolean no_valid_spieler_name = true;
        boolean no_valid_server_name = true;

        while (no_valid_spieler_name)
        {
            spieler_name = JOptionPane.showInputDialog(this.mainWindow, "Dein Name:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);

            if (spieler_name != null)
            {
                if (!spieler_name.trim().equals(""))
                {
                    no_valid_spieler_name = false;
                }
            }
        }

        this.spielerName = spieler_name;

        while (no_valid_server_name)
        {
            server_name = JOptionPane.showInputDialog(this.mainWindow, "Name/Adresse des Server:", "Server", JOptionPane.PLAIN_MESSAGE);

            if (server_name != null)
            {
                if (!server_name.trim().equals(""))
                {
                    no_valid_server_name = false;
                }
            }
        }

        try
        {
            this.networkClient = new DameClient(server_name, 41135, this);
            this.networkClient.sendMessageToServer("SETUSERNAME|" + this.spielerName);
            this.networkClient.sendMessageToServer("GET_ID_SELF");
            this.erzeugeBrowser();
        }
        catch (UnknownHostException uhe)
        {
            JOptionPane.showMessageDialog(null, "Server konnte nicht gefunden werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "Unbekannter Fehler beim Verbinden aufgetreten", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void setDisconnected(boolean disconnected)
    {
        this.disconnected = disconnected;
    }

    public JDialog getMainWindow()
    {
        return this.mainWindow;
    }

    public void erzeugeBrowser()
    {
        this.mainWindow = new JDialog(this.gui.getMainWindow(), true);
        this.mainWindow.setSize(690, 550);
        this.mainWindow.setLocationRelativeTo(this.gui.getMainWindow());
        this.mainWindow.getContentPane().setLayout(null);
        this.mainWindow.setResizable(false);
        this.mainWindow.setTitle("KKSDame - Multiplayer über TCP/IP - Lobby");

        this.playerContextMenu = new JPopupMenu();

        this.menuItemChallengePlayer = new JMenuItem();
        this.menuItemChallengePlayer.addActionListener(this);
        this.playerContextMenu.add(this.menuItemChallengePlayer);

        //this.chatTextArea = new JTextArea(10, 40);
        this.chatScrollPane = new JTextPane();
        this.chatScrollPane.setLocation(10, 40);
        this.chatScrollPane.setSize(500, 410);
        this.chatScrollPane.setEditable(false);

        this.chatTitleLabel = new JLabel("Chat:");
        this.chatTitleLabel.setSize(50, 20);
        this.chatTitleLabel.setLocation(10, 20);

        this.clientListModel = new DefaultListModel<String>();

        this.playerList = new JList<String>();
        this.playerList.setModel(this.clientListModel);
        this.playerList.setLocation(520, 40);
        this.playerList.setSize(150, 410);
        this.playerList.setCellRenderer(new ClientCellRenderer());
        this.playerList.addMouseListener(this);
        this.playerList.setSelectionBackground(Color.blue);
        this.playerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        this.playerListTitleLabel = new JLabel("Spieler:");
        this.playerListTitleLabel.setSize(50, 20);
        this.playerListTitleLabel.setLocation(520, 20);

        this.chatTextField = new JTextField(30);
        this.chatTextField.setLocation(10, 480);
        this.chatTextField.setSize(500, 25);

        this.chatTextLabel = new JLabel("Nachricht:");
        this.chatTextLabel.setSize(70, 20);
        this.chatTextLabel.setLocation(10, 460);

        this.chatSendButton = new JButton("Senden");
        this.chatSendButton.setSize(150, 25);
        this.chatSendButton.setLocation(520, 480);
        this.chatSendButton.addActionListener(this);

        this.mainWindow.getContentPane().add(this.chatTitleLabel);
        this.mainWindow.getContentPane().add(this.chatScrollPane);
        this.mainWindow.getContentPane().add(this.playerList);
        this.mainWindow.getContentPane().add(this.chatTextField);
        this.mainWindow.getContentPane().add(this.chatTextLabel);
        this.mainWindow.getContentPane().add(this.playerListTitleLabel);
        this.mainWindow.getContentPane().add(this.chatSendButton);
        this.mainWindow.getRootPane().setDefaultButton(this.chatSendButton);

        this.mainWindow.addWindowListener(new java.awt.event.WindowAdapter()
            {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent)
                {
                    if (!disconnected)
                    {
                        if (JOptionPane.showConfirmDialog(mainWindow, "Verbindung zum Server trennen?", "Beenden?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                        {
                            networkClient.sendMessageToServer("DISCONNECT");
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(mainWindow, "Verbindung zum Server aufgrund Inaktivität getrennt.", "Verbindungsabbruch", JOptionPane.INFORMATION_MESSAGE);
                    }

                }
            });

        this.mainWindow.setVisible(true);

    }

    public synchronized void writeToChat(String time, String color, String name, String message)
    {
        StyledDocument doc = this.chatScrollPane.getStyledDocument();

        Style style = this.chatScrollPane.addStyle("Chatstil", null);

        StyleConstants.setForeground(style, Color.red);

        try
        {
            doc.insertString(doc.getLength(), "[" + time + "] ", style);
        }
        catch (BadLocationException e)
        {

        }

        StyleConstants.setForeground(style, Color.decode(color));

        try
        {
            doc.insertString(doc.getLength(), "[" + name + "]: ", style);
        }
        catch (BadLocationException e)
        {

        }

        StyleConstants.setForeground(style, Color.black);

        try
        {
            doc.insertString(doc.getLength(), message + "\n", style);
        }
        catch (BadLocationException e)
        {

        }

        //this.chatTextArea.append(message + "\n");
    }

    public synchronized void updateClientList(String client_list_string)
    {
        //this.clientListModel.removeAllElements();

        String clients[] = client_list_string.split("%%%");

        for (int i = 0; i < this.clientListModel.getSize(); i++)
        {
            boolean found = false;

            for (int k = 0; k < clients.length; k++)
            {
                if (clients[k].equals(this.clientListModel.getElementAt(i)))
                {
                    found = true;
                }
            }

            if (!found)
            {
                clientListModel.removeElementAt(i);
            }
        }

        for (int i = 0; i < clients.length; i++)
        {
            boolean found = false;

            for (int k = 0; k < this.clientListModel.getSize(); k++)
            {
                if (clients[i].equals(this.clientListModel.getElementAt(k)))
                {
                    found = true;
                }
            }

            if (!found)
            {
                this.clientListModel.addElement(clients[i]);
            }
        }
    }
    
    public void moveToBackgroundAndStartGame()
    {
        
    }

    public void challengeRequestCanceledBy(int ID)
    {
        String player = null;

        for (int i = 0; i < this.clientListModel.getSize(); i++)
        {
            String player_string = this.clientListModel.getElementAt(i);
            int id_player = Integer.parseInt(player_string.split(";;;")[0]);
            
            if (id_player == ID)
            {
                player = player_string.split(";;;")[1];
            }
            
        }
        
        // Den offenen Dialog finden ...
        
        Window[] windows = Window.getWindows();

        JDialog opened_request_window = null;

        for (Window window : windows)
        {
            if (window instanceof JDialog)
            {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponent(0) instanceof JOptionPane)
                {
                    opened_request_window = dialog;

                }
            }
        }

        if (opened_request_window != null)
        {
            opened_request_window.dispose();
            JOptionPane.showMessageDialog(this.mainWindow, player + " hat seine Herausforderung zurückgezogen", "Herausforderung zurückgezogen", JOptionPane.INFORMATION_MESSAGE);
        }

    }
    
    public void challengeRequestDeclinedBy(int ID)
    {
        String player = null;

        for (int i = 0; i < this.clientListModel.getSize(); i++)
        {
            String player_string = this.clientListModel.getElementAt(i);
            int id_player = Integer.parseInt(player_string.split(";;;")[0]);
            
            if (id_player == ID)
            {
                player = player_string.split(";;;")[1];
            }
            
        }
        
        // Den offenen Dialog finden ...
        
        Window[] windows = Window.getWindows();

        JDialog opened_request_window = null;

        for (Window window : windows)
        {
            if (window instanceof JDialog)
            {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1 && dialog.getContentPane().getComponent(0) instanceof JOptionPane)
                {
                    opened_request_window = dialog;

                }
            }
        }

        if (opened_request_window != null)
        {
            opened_request_window.dispose();
            JOptionPane.showMessageDialog(this.mainWindow, player + " hat deine Herausforderung abgelehnt!", "Herausforderung abgelehnt", JOptionPane.INFORMATION_MESSAGE);
        }

    }
    
    public void challengeRequestAcceptedBy(int ID)
    {
        String player = null;

     for (int i = 0; i < this.clientListModel.getSize(); i++)
        {
            String player_string = this.clientListModel.getElementAt(i);
            int id_player = Integer.parseInt(player_string.split(";;;")[0]);
            
            if (id_player == ID)
            {
                player = player_string.split(";;;")[1];
            }
            
        }
        
        // Den offenen Dialog finden ...
        
        Window[] windows = Window.getWindows();

        JDialog opened_request_window = null;

        for (Window window : windows)
        {
            if (window instanceof JDialog)
            {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1 && dialog.getContentPane().getComponent(0) instanceof JOptionPane)
                {
                    opened_request_window = dialog;

                }
            }
        }

        if (opened_request_window != null)
        {
            opened_request_window.dispose();
            
            this.moveToBackgroundAndStartGame();
        }

    }

    public void challengeRequestedBy(int ID)
    {
        String player = null;

        for (int i = 0; i < this.clientListModel.getSize(); i++)
        {
            String player_string = this.clientListModel.getElementAt(i);
            int id_player = Integer.parseInt(player_string.split(";;;")[0]);
            
            if (id_player == ID)
            {
                player = player_string.split(";;;")[1];
            }
            
        }
        
        int antwort = JOptionPane.showConfirmDialog(this.mainWindow, player + " hat Dich herausgefordert! Annehmen?", "Herausforderung", JOptionPane.YES_NO_OPTION);
        
        if (antwort == JOptionPane.YES_OPTION)
        {
           this.networkClient.sendMessageToServer("CHALLENGE_REQUEST_ACCEPT|" + ID);
           this.moveToBackgroundAndStartGame();
        }
        else
        {
           this.networkClient.sendMessageToServer("CHALLENGE_REQUEST_DECLINE|" + ID);
        }
    }

    public void actionPerformed (A
           System.exit(0);ctionEvent ae)
    {
        if(ae.getSource() == this.chatSendButton)
        {
            if (!this.chatTextField.getText().trim().equals(""))
            {
                this.networkClient.sendMessageToServer("CHAT|" + this.chatTextField.getText().replaceAll("\\|", "PIPE_CHARACTER"));
                this.chatTextField.setText("");
            }

        }
        else if (ae.getSource() == this.menuItemChallengePlayer)
        {
            String player[] = this.clientListModel.getElementAt(this.playerList.getSelectedIndex()).split(";;;");
            String id_player = player[0];
            String player_name = player[1];

            this.networkClient.sendMessageToServer("CHALLENGE_REQUEST|" + id_player);

            Object[] options = {"Abbrechen"};
            int answer = JOptionPane.showOptionDialog(this.mainWindow,
                    "Fordere " + player_name + " heraus, warte auf Antwort ...", "Warte auf Verbindung ...",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (answer == 0)
            {
                this.networkClient.sendMessageToServer("CHALLENGE_REQUEST_CANCEL|" + id_player);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mousePressed(MouseEvent e)
    {
        this.checkPopup(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        this.checkPopup(e);
    }

    public void checkPopup(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            String player[] = this.clientListModel.getElementAt(this.playerList.locationToIndex(e.getPoint())).split(";;;");
            boolean isPlaying = Boolean.parseBoolean(player[3]);
            int id_player = Integer.parseInt(player[0]);

            if (!isPlaying && (id_player != this.networkClient.getID()))
            {
                String player_name = player[1];
                this.menuItemChallengePlayer.setText(player_name + " herausfordern...");
                this.playerList.setSelectedIndex(this.playerList.locationToIndex(e.getPoint())); //select the item
                this.playerContextMenu.show(this.playerList, e.getX(), e.getY()); //and show the menu
            }
        }
    }
}
