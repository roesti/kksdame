
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
    private Spieler current_spieler;

    public Spiel()
    {
        this.ui = new KonsoleUI(this);
        this.spielbrett = new Spielbrett();
        this.spieler1 = new Spieler(this);
        this.spieler2 = new Spieler(this);

        this.ui.displayMainMenu();
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
        Spiel spiel = new Spiel();
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
