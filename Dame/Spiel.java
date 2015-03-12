import java.awt.*;
import javax.swing.*;

/**
 * Write a description of class Spiel here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */
public class Spiel
{
    private Spielbrett spielbrett;
    private UI ui;
    private Spieler spieler1;
    private Spieler spieler2;
    private Spieler networkPlayerSelf;
    private Spieler current_spieler;

    public Spiel(boolean GUI)
    {
        if (GUI)
        {
            this.ui = new GUI(this);
        }
        else
        {
            this.ui = new KonsoleUI(this);
        }

        this.spielbrett = new Spielbrett();
        this.spieler1 = new Spieler(this);
        this.spieler2 = new Spieler(this);

        this.ui.displayMainMenu();
    }
    
    public Spieler getNetworkPlayerSelf()
    {
        return this.networkPlayerSelf;
    }
    
    public void setNetworkPlayerSelf(Spieler spieler)
    {
        this.networkPlayerSelf = spieler;
    }
    

    public Spieler getCurrentSpieler()
    {
        return this.current_spieler;
    }

    public void setCurrentSpieler(Spieler spieler)
    {
        this.current_spieler = spieler;
    }

    public void startGame()
    {
        this.spielbrett.resetBrett();
        this.resetSteinWahl();
        this.ui.setIsNetworkGame(false);
        this.ui.displayStartGameMenu();

        // Zufällige Bestimmung, welcher Spieler welche Farbe erhält
        int get_random_player = Tools.getRandomInt(0, 1);

        if (get_random_player == 0)
        {
            this.spieler1.setColor('s');
            this.spieler2.setColor('w');
            this.current_spieler = this.spieler1;
        }
        else
        {
            this.spieler1.setColor('w');
            this.spieler2.setColor('s');
            this.current_spieler = this.spieler2;
        }

        this.ui.displayMainGameMenu();

    }
    
    public void startNetworkGame(String player1_name, char player1_color, String player2_name, char player2_color)
    {
        this.spielbrett.resetBrett();
        this.resetSteinWahl();
        
        this.ui.setIsNetworkGame(true);
        
        this.spieler1.setName(player1_name);
        this.spieler1.setColor(player1_color);
        this.spieler2.setName(player2_name);
        this.spieler2.setColor(player2_color);
        
        this.ui.displayStartGameMenu();

        // schwarz beginnt ...
        
        if (this.spieler1.getColor() == 's')
        {
            this.current_spieler = this.spieler1;
        }
        else
        {
            this.current_spieler = this.spieler2;
        }

        this.ui.displayMainGameMenu();

    }

    public int[] getSteinCoordsGewaehlt()
    {
        int coords[] = new int[2];

        coords[0] = 0;
        coords[1] = 0;

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spielbrett.getBrett()[i][k] != null)
                {
                    if (this.spielbrett.getBrett()[i][k].getAusgewaehlt())
                    {
                        coords[0] = i;
                        coords[1] = k;
                    }
                }
            }
        }

        return coords;
    }

    public boolean checkSteinGewaehlt()
    {
        boolean steinGewaehlt = false;

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spielbrett.getBrett()[i][k] != null)
                {
                    if (this.spielbrett.getBrett()[i][k].getAusgewaehlt())
                    {
                        steinGewaehlt = true;
                    }
                }
            }
        }

        return steinGewaehlt;
    }

    public void resetSteinWahl()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spielbrett.getBrett()[i][k] != null)
                {
                    this.spielbrett.getBrett()[i][k].setAusgewaehlt(false);
                }
            }
        }
    }

    public Spielbrett getSpielbrett()
    {
        return this.spielbrett;
    }

    // instance variables - replace the example below with your own
    public static void main(String args[])
    {

        Object[] options = {"Konsole", "GUI"};
        
        int start_version = JOptionPane.showOptionDialog(null, "Welche Version willst Du starten?", "KKSDame 0.1", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (start_version == 0)
        {
            Spiel spiel = new Spiel(false);
        }
        else
        {
            Spiel spiel = new Spiel(true);
        }

    }

    public Spieler getSpieler1()
    {
        return this.spieler1;
    }

    public Spieler getSpieler2()
    {
        return this.spieler2;
    }
}
