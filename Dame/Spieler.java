
/**
 * Write a description of class Spieler here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */
public class Spieler
{
    // instance variables - replace the example below with your own
    private String name;
    private char color;
    private Spiel spiel;

    /**
     * Constructor for objects of class Spieler
     */
    public Spieler(Spiel spiel)
    {
        this.spiel = spiel;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setColor(char color)
    {
        this.color = color;
    }
    
    public char getColor()
    {
        return this.color;
    }
    
    public int countDamen()
    {
        int damen_count = 0;
        
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spiel.getSpielbrett().getBrett()[i][k] != null)
                {
                    if (this.spiel.getSpielbrett().getBrett()[i][k].getIsDame() && (this.spiel.getSpielbrett().getBrett()[i][k].getColor() == this.getColor()))
                    {
                        damen_count++;
                    }
                }
            }
        }
        
        return damen_count;
    }
    
    public int countNormaleSteine()
    {
        int steine_count = 0;
        
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spiel.getSpielbrett().getBrett()[i][k] != null)
                {
                    if (!this.spiel.getSpielbrett().getBrett()[i][k].getIsDame() && (this.spiel.getSpielbrett().getBrett()[i][k].getColor() == this.getColor()))
                    {
                        steine_count++;
                    }
                }
            }
        }
        
        return steine_count;
    }
    
    public int countSteineGesamt()
    {
        return (this.countDamen() + this.countNormaleSteine());
    }
}
