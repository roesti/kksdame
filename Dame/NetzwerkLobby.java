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
public class NetzwerkLobby implements ActionListener
{
    // instance variables - replace the example below with your own
    private GUI gui;

    private JFrame mainWindow;
    //private JTextArea chatTextArea;
    private JTextPane chatScrollPane;
    private JLabel chatTitleLabel;
    private JLabel playerListTitleLabel;
    private JLabel chatTextLabel;
    private JList<String> playerList;
    private JTextField chatTextField;
    private JButton chatSendButton;
    
    private DefaultListModel<String> clientListModel;
    
    private String spielerName;
    private DameClient networkClient;
    

    /**
     * Constructor for objects of class NetworkBrowser
     */
    public NetzwerkLobby()
    {
        // initialise instance variables
        //this.gui = gui;

        String spieler_name = "";
        String server_name = "";

        while (spieler_name.trim().equals(""))
        {
            spieler_name = JOptionPane.showInputDialog(this.mainWindow, "Dein Name:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);
        }

        this.spielerName = spieler_name;

        while (server_name.trim().equals(""))
        {
            server_name = JOptionPane.showInputDialog(this.mainWindow, "Name/Adresse des Server:", "Server", JOptionPane.PLAIN_MESSAGE);
        }

        try
        {
            this.networkClient = new DameClient(server_name, 41135, this);
            this.networkClient.sendMessageToServer("SETUSERNAME|" + this.spielerName);
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

    public void erzeugeBrowser()
    {
        this.mainWindow = new JFrame();
        this.mainWindow.setSize(690, 550);
        this.mainWindow.getContentPane().setLayout(null);
        this.mainWindow.setResizable(false);
        this.mainWindow.setTitle("KKSDame - Multiplayer über TCP/IP - Lobby");

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
        this.clientListModel.removeAllElements();
        
        String clients[] = client_list_string.split("%%%");
        
        for (int i = 0; i < clients.length; i++)
        {
            this.clientListModel.addElement(clients[i]);
        }
    }

    public void actionPerformed (ActionEvent ae)
    {
        if(ae.getSource() == this.chatSendButton)
        {
            if (!this.chatTextField.getText().trim().equals(""))
            {

                this.networkClient.sendMessageToServer("CHAT|" + this.chatTextField.getText().replaceAll("\\|", "PIPE_CHARACTER"));
                this.chatTextField.setText("");
            }

        }
    }
}
