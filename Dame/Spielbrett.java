
/**
 * Write a description of class Spielbrett here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */
public class Spielbrett
{
    // instance variables - replace the example below with your own
    private Spielstein field_array[][];

    /**
     * Constructor for objects of class Spielbrett
     */
    public Spielbrett()
    {
        this.resetBrett();
    }

    public Spielstein[][] getBrett()
    {
        return this.field_array;
    }

    public void resetBrett()
    {
        this.field_array = new Spielstein[8][8];

        // weiße Spielsteine setzen (unten)
        for (int i = 0; i < 3; i++)
        {
            // Gerade Zeilen
            if (((i + 1) % 2) == 0)
            {
                for (int k = 1; k < 8; k+=2)
                {
                    this.field_array[i][k] = new Spielstein('w', this);
                }
            }
            else
            {
                for (int k = 0; k < 8; k+=2)
                {
                    this.field_array[i][k] = new Spielstein('w', this);
                }
            }
        }

        // schwarze Spielsteine setzen (oben)
        for (int i = 7; i > 4; i--)
        {
            // Gerade Zeilen
            if (((i + 1) % 2) == 0)
            {
                for (int k = 1; k < 8; k+=2)
                {
                    this.field_array[i][k] = new Spielstein('s', this);
                }
            }
            else
            {
                for (int k = 0; k < 8; k+=2)
                {
                    this.field_array[i][k] = new Spielstein('s', this);
                }
            }
        }
    }
    
    
}
