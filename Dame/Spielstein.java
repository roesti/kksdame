import java.util.*;

/**
 * Write a description of class Spielstein here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */
public class Spielstein
{
    // instance variables - replace the example below with your own
    private char color;
    private boolean isDame;
    private Spielbrett spielbrett;
    private boolean ausgewaehlt;

    /**
     * Constructor for objects of class Spielstein
     */
    public Spielstein(char color, Spielbrett spielbrett)
    {
        this.color = color;
        this.isDame = false;
        this.spielbrett = spielbrett;
        this.ausgewaehlt = false;
    }

    public boolean getIsDame()
    {
        return this.isDame;
    }

    public void setIsDame(boolean isDame)
    {
        this.isDame = isDame;
    }

    public boolean getAusgewaehlt()
    {
        return this.ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt)
    {
        this.ausgewaehlt = ausgewaehlt;
    }

    public int[] getPosition()
    {
        int position[] = new int[2];

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                if (this.spielbrett.getBrett()[i][k] == this)
                {
                    position[0] = i;
                    position[1] = k;
                }
            }
        }
        return position;
    }

    public ArrayList<int[]> getPossibleMoves()
    {
        ArrayList<int[]> positions = new ArrayList<int[]>();

        int zeile = this.getPosition()[0];
        int spalte = this.getPosition()[1];

        // Einfache Spielesteine
        if (!this.getIsDame())
        {
            // Einfache schwarze Steine können sich nur nach unten bewegen
            if (this.getColor() == 's')
            {
                boolean kannSchlagen = false;

                // Prüfen, ob der Stein diagonal einen Stein links unter ihm schlagen könnte
                // Ist mindestens 2 Felder vom Rand entfernt..
                if (spalte > 1)
                {
                    if (zeile > 1)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile - 2)][(spalte - 2)] == null)
                        {
                            // Zwischenfeld besetzt?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)] != null)
                            {
                                // Weißer Stein?
                                if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)].getColor() == 'w')
                                {
                                    kannSchlagen = true;

                                    int pos[] = new int[3];
                                    pos[0] = zeile - 2;
                                    pos[1] = spalte - 2;
                                    pos[2] = 1; // Schlag-Move

                                    positions.add(pos);
                                }
                            }
                        }
                    }
                }

                // Prüfen, ob der Stein diagonal einen Stein rechts unter ihm schlagen könnte
                // Ist mindestens 2 Felder vom Rand entfernt..
                if (spalte < 6)
                {
                    if (zeile > 1)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile - 2)][(spalte + 2)] == null)
                        {
                            // Zwischenfeld besetzt?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)] != null)
                            {
                                // Weißer Stein?
                                if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)].getColor() == 'w')
                                {
                                    kannSchlagen = true;

                                    int pos[] = new int[3];
                                    pos[0] = zeile - 2;
                                    pos[1] = spalte + 2;
                                    pos[2] = 1; // Schlag-Move

                                    positions.add(pos);
                                }
                            }
                        }
                    }
                }

                // Nur einfachen Move erlauben, wenn nicht geschlagen werden kann.
                if (!kannSchlagen)
                {
                    // Prüfen, ob der Stein diagonal ein Feld nach unten links bewegt werden kann
                    // Klebt nicht am linken Rand..
                    if (spalte > 0)
                    {
                        if (zeile > 0)
                        {
                            // Zielfeld leer?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)] == null)
                            {
                                int pos[] = new int[3];
                                pos[0] = zeile - 1;
                                pos[1] = spalte - 1;
                                pos[2] = 0; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }

                    // Prüfen, ob der Stein diagonal ein Feld nach unten rechts bewegt werden kann
                    // Klebt nicht am rechten Rand..

                    if (spalte < 7)
                    {
                        if (zeile > 0)
                        {
                            // Zielfeld leer?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)] == null)
                            {
                                int pos[] = new int[3];
                                pos[0] = zeile - 1;
                                pos[1] = spalte + 1;
                                pos[2] = 0; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }
            else // Moves für einfache weiße Steine..
            {
                boolean kannSchlagen = false;

                // Prüfen, ob der Stein diagonal einen Stein links über ihm schlagen könnte
                // Ist mindestens 2 Felder vom Rand entfernt..
                if (spalte > 1)
                {
                    if (zeile < 6)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile + 2)][(spalte - 2)] == null)
                        {
                            // Zwischenfeld besetzt?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)] != null)
                            {
                                // Schwarzer Stein?
                                if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)].getColor() == 's')
                                {
                                    kannSchlagen = true;

                                    int pos[] = new int[3];
                                    pos[0] = zeile + 2;
                                    pos[1] = spalte - 2;
                                    pos[2] = 1; // Schlag-Move

                                    positions.add(pos);
                                }
                            }
                        }
                    }
                }

                // Prüfen, ob der Stein diagonal einen Stein rechts über ihm schlagen könnte
                // Ist mindestens 2 Felder vom Rand entfernt..
                if (spalte < 6)
                {
                    if (zeile < 6)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile + 2)][(spalte + 2)] == null)
                        {
                            // Zwischenfeld besetzt?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)] != null)
                            {
                                // Schwarzer Stein?
                                if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)].getColor() == 's')
                                {
                                    kannSchlagen = true;

                                    int pos[] = new int[3];
                                    pos[0] = zeile + 2;
                                    pos[1] = spalte + 2;
                                    pos[2] = 1; // Schlag-Move

                                    positions.add(pos);
                                }
                            }
                        }
                    }
                }

                // Nur einfachen Move erlauben, wenn nicht geschlagen werden kann.
                if (!kannSchlagen)
                {
                    // Prüfen, ob der Stein diagonal ein Feld nach oben links bewegt werden kann
                    // Klebt nicht am linken Rand..
                    if (spalte > 0)
                    {
                        if (zeile < 7)
                        {
                            // Zielfeld leer?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)] == null)
                            {
                                int pos[] = new int[3];
                                pos[0] = zeile + 1;
                                pos[1] = spalte - 1;
                                pos[2] = 0; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }

                    // Prüfen, ob der Stein diagonal ein Feld nach oben rechts bewegt werden kann
                    // Klebt nicht am rechten Rand..

                    if (spalte < 7)
                    {
                        if (zeile < 7)
                        {
                            // Zielfeld leer?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)] == null)
                            {
                                int pos[] = new int[3];
                                pos[0] = zeile + 1;
                                pos[1] = spalte + 1;
                                pos[2] = 0; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            // Code für Damen
            boolean kannSchlagen = false;
            char gegner_color = 's';

            if (this.getColor() == 's')
            {
                gegner_color = 'w';
            }

            // Prüfen, ob der Stein diagonal einen Stein links unter ihm schlagen könnte
            // Ist mindestens 2 Felder vom Rand entfernt..
            if (spalte > 1)
            {
                if (zeile > 1)
                {
                    // Zielfeld leer?
                    if (this.spielbrett.getBrett()[(zeile - 2)][(spalte - 2)] == null)
                    {
                        // Zwischenfeld besetzt?
                        if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)] != null)
                        {
                            // Gegnerischer Stein?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)].getColor() == gegner_color)
                            {
                                kannSchlagen = true;

                                int pos[] = new int[3];
                                pos[0] = zeile - 2;
                                pos[1] = spalte - 2;
                                pos[2] = 1; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }

            // Prüfen, ob der Stein diagonal einen Stein rechts unter ihm schlagen könnte
            // Ist mindestens 2 Felder vom Rand entfernt..
            if (spalte < 6)
            {
                if (zeile > 1)
                {
                    // Zielfeld leer?
                    if (this.spielbrett.getBrett()[(zeile - 2)][(spalte + 2)] == null)
                    {
                        // Zwischenfeld besetzt?
                        if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)] != null)
                        {
                            // Gegnerischer Stein?
                            if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)].getColor() == gegner_color)
                            {
                                kannSchlagen = true;

                                int pos[] = new int[3];
                                pos[0] = zeile - 2;
                                pos[1] = spalte + 2;
                                pos[2] = 1; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }

            if (spalte > 1)
            {
                if (zeile < 6)
                {
                    // Zielfeld leer?
                    if (this.spielbrett.getBrett()[(zeile + 2)][(spalte - 2)] == null)
                    {
                        // Zwischenfeld besetzt?
                        if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)] != null)
                        {
                            // Gegnerischer Stein?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)].getColor() == gegner_color)
                            {
                                kannSchlagen = true;

                                int pos[] = new int[3];
                                pos[0] = zeile + 2;
                                pos[1] = spalte - 2;
                                pos[2] = 1; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }

            // Prüfen, ob der Stein diagonal einen Stein rechts über ihm schlagen könnte
            // Ist mindestens 2 Felder vom Rand entfernt..
            if (spalte < 6)
            {
                if (zeile < 6)
                {
                    // Zielfeld leer?
                    if (this.spielbrett.getBrett()[(zeile + 2)][(spalte + 2)] == null)
                    {
                        // Zwischenfeld besetzt?
                        if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)] != null)
                        {
                            // Gegnerischer Stein?
                            if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)].getColor() == gegner_color)
                            {
                                kannSchlagen = true;

                                int pos[] = new int[3];
                                pos[0] = zeile + 2;
                                pos[1] = spalte + 2;
                                pos[2] = 1; // Schlag-Move

                                positions.add(pos);
                            }
                        }
                    }
                }
            }

            if (!kannSchlagen)
            {
                // Prüfen, ob der Stein diagonal ein Feld nach unten links bewegt werden kann
                // Klebt nicht am linken Rand..
                if (spalte > 0)
                {
                    if (zeile > 0)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile - 1)][(spalte - 1)] == null)
                        {
                            int pos[] = new int[3];
                            pos[0] = zeile - 1;
                            pos[1] = spalte - 1;
                            pos[2] = 0; // Schlag-Move

                            positions.add(pos);
                        }
                    }
                }

                // Prüfen, ob der Stein diagonal ein Feld nach unten rechts bewegt werden kann
                // Klebt nicht am rechten Rand..

                if (spalte < 7)
                {
                    if (zeile > 0)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile - 1)][(spalte + 1)] == null)
                        {
                            int pos[] = new int[3];
                            pos[0] = zeile - 1;
                            pos[1] = spalte + 1;
                            pos[2] = 0; // Schlag-Move

                            positions.add(pos);
                        }
                    }
                }

                // Prüfen, ob der Stein diagonal ein Feld nach oben links bewegt werden kann
                // Klebt nicht am linken Rand..
                if (spalte > 0)
                {
                    if (zeile < 7)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile + 1)][(spalte - 1)] == null)
                        {
                            int pos[] = new int[3];
                            pos[0] = zeile + 1;
                            pos[1] = spalte - 1;
                            pos[2] = 0; // Schlag-Move

                            positions.add(pos);
                        }
                    }
                }

                // Prüfen, ob der Stein diagonal ein Feld nach oben rechts bewegt werden kann
                // Klebt nicht am rechten Rand..

                if (spalte < 7)
                {
                    if (zeile < 7)
                    {
                        // Zielfeld leer?
                        if (this.spielbrett.getBrett()[(zeile + 1)][(spalte + 1)] == null)
                        {
                            int pos[] = new int[3];
                            pos[0] = zeile + 1;
                            pos[1] = spalte + 1;
                            pos[2] = 0; // Schlag-Move

                            positions.add(pos);
                        }
                    }
                }
            }
        }

        return positions;
    }

    public ArrayList<int[]> getPossibleSchlagMoves()
    {
        boolean kann_schlagen = false;

        ArrayList<int[]> possible_moves = this.getPossibleMoves();
        ArrayList<int[]> possible_schlag_moves = new ArrayList<int[]>();

        for (int[] move : possible_moves)
        {
            // Wenn Schlag-Move möglich
            if (move[2] != 0)
            {
                possible_schlag_moves.add(move);
            }
        }

        return possible_schlag_moves;

    }

    /**
     * moveTo - bewegt den Stein an die angegebene Position, gibt weiterhin zurück, ob mit diesem Zug ein gegnerischer Stein geschlagen wurde.
     *
     * @param   zeile - Zeile der Zielposition
     * @param   spalte - Spalte der Zielposition
     * @return  boolean - gibt 'true' zurück, wenn Stein geschlagen wurde
     */

    public boolean moveTo(int zeile, int spalte)
    {
        boolean geschlagen = false;

        int alte_zeile = this.getPosition()[0];
        int alte_spalte = this.getPosition()[1];

        this.spielbrett.getBrett()[zeile][spalte] = this;
        this.spielbrett.getBrett()[alte_zeile][alte_spalte] = null;

        // Falls Damen irgendwann mal mehrere Felder springen können sollen, else-Zweig hinzufügen...
        //if (!this.isDame)
        if (true)
        {
            if (Math.abs(alte_zeile - zeile) == 2)
            {
                int zwischen_zeile = (alte_zeile + zeile) / 2;
                int zwischen_spalte = (alte_spalte + spalte) / 2;

                this.spielbrett.getBrett()[zwischen_zeile][zwischen_spalte] = null;
                geschlagen = true;
            }

            // Umwandlung zu einer Dame, wenn Stein am oberen bzw. unteren Spielfeldrand (je nachdem ob schwarz oder weiß) angekommen ist.
            if (((this.getColor() == 'w') && (zeile == 7)) || ((this.getColor() == 's') && (zeile == 0)))
            {
                // Nur, wenn Stein noch keine Dame ist ausführen.. (da zusätzliche Schlagmöglichkeiten sonst weggenommen werden würden)
                if (!this.getIsDame())
                {
                    this.setIsDame(true);
                    geschlagen = false;
                }
            }
        }

        return geschlagen;
    }

    public char getColor()
    {
        return this.color;
    }
}
