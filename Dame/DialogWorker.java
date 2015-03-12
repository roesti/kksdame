import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.text.*;

public class DialogWorker extends SwingWorker<Integer, Integer>
{
    private String player_name;
    private NetzwerkLobby lobby;
    private JDialog dialog;
    private int player_id;

    public DialogWorker(JDialog dialog, NetzwerkLobby lobby, String player_name, int player_id)
    {
        this.player_name = player_name;
        this.lobby = lobby;
        this.dialog = dialog;
        this.player_id = player_id;
    }

    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.

        Object[] options = {"Ja", "Nein"};
        int answer = JOptionPane.showOptionDialog(this.lobby.getMainWindow(),
                player_name + " hat Dich herausgefordert! Annehmen?", "Herausforderung",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (answer == 0)
        {
            this.lobby.getNetworkClient().sendMessageToServer("CHALLENGE_REQUEST_ACCEPT|" + this.player_id);
        }
        else
        {
            this.lobby.getNetworkClient().sendMessageToServer("CHALLENGE_REQUEST_DECLINE|" + this.player_id);
        }
        

        return 0;

    }
}