import java.util.*;

/**
 * Write a description of class KonsoleUI here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */

public class KonsoleUI implements UI
{
    // instance variables - replace the example below with your own

    private Spiel spiel;

    /**
     * Constructor for objects of class KonsoleUI
     */
    public KonsoleUI(Spiel spiel)
    {
        this.spiel = spiel;
    }

    public void displayStartGameMenu()
    {
        System.out.println("\fAuf geht's!\n");

        String name_spieler_1 = "";

        while (name_spieler_1.trim().equals(""))
        {
            System.out.print("Name (Spieler 1): ");
            name_spieler_1 = new Scanner(System.in).nextLine();

            if (name_spieler_1.trim().equals(""))
            {
                System.out.println("Fehler: Bitte gib einen Namen ein!\n");
            }
        }

        this.spiel.getSpieler1().setName(name_spieler_1);

        String name_spieler_2 = "";

        while (name_spieler_2.trim().equals(""))
        {
            System.out.print("Name (Spieler 2): ");
            name_spieler_2 = new Scanner(System.in).nextLine();

            if (name_spieler_2.trim().equals(""))
            {
                System.out.println("Fehler: Bitte gib einen Namen ein!\n");
            }
        }

        this.spiel.getSpieler2().setName(name_spieler_2);

    }

    // Hauptmenü-Ausgabe
    public void displayMainMenu()
    {   
        System.out.println("\fWillkommen zu KKSDame 0.1 by 12FO3!\n");
        System.out.println("(1) Neues Spiel starten");
        System.out.println("(2) Beenden\n");
        System.out.print("Deine Wahl: ");

        String choice = new Scanner(System.in).nextLine();

        try
        {
            int choice_numeric = Integer.parseInt(choice.trim());

            switch(choice_numeric)
            {
                case 1:
                this.spiel.startGame();
                break;

                case 2:
                System.out.println("\nCiao!");
                System.exit(0);
                break;

                default:
                System.out.println("\nFehler: Bitte geben Sie 1 oder 2 ein!");
                new Scanner(System.in).nextLine();
                this.displayMainMenu();
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("\nFehler: Bitte geben Sie 1 oder 2 ein!");
            new Scanner(System.in).nextLine();
            this.displayMainMenu();
        }

    }

    public void displayZugMenu(boolean onlySchlagMoves)
    {
        this.drawSpiel();

        int coords_numeric[] = this.spiel.getSteinCoordsGewaehlt();
        String coords = Tools.translateCoordsToString(this.spiel.getSteinCoordsGewaehlt()[0], this.spiel.getSteinCoordsGewaehlt()[1]);

        System.out.println("\n\nAktueller Stein (" + coords + "):\n");

        ArrayList<int[]> liste_zuege = null;

        if (!onlySchlagMoves)
        {
            System.out.println("(1) Anderen Stein wählen");
            liste_zuege = this.spiel.getSpielbrett().getBrett()[coords_numeric[0]][coords_numeric[1]].getPossibleMoves();
        }
        else
        {
            liste_zuege = this.spiel.getSpielbrett().getBrett()[coords_numeric[0]][coords_numeric[1]].getPossibleSchlagMoves();
        }

        for (int i = 0; i < liste_zuege.size(); i++)
        {
            int ziel_coords[] = liste_zuege.get(i);

            if (!onlySchlagMoves)
            {
                System.out.println("(" + (i + 2) + ") Auf (" + Tools.translateCoordsToString(ziel_coords[0], ziel_coords[1]) + ") bewegen.");
            }
            else
            {
                System.out.println("(" + (i + 1) + ") Auf (" + Tools.translateCoordsToString(ziel_coords[0], ziel_coords[1]) + ") bewegen.");
            }
        }

        //System.out.println("(2) Beenden\n");
        System.out.print("\nDeine Wahl: ");

        String choice = new Scanner(System.in).nextLine();

        try
        {
            int choice_numeric = Integer.parseInt(choice.trim());

            // Auswahl um 1 zu erhöhen, da Menüpunkt 1 nicht angezeigt wird bei einem Zweit-, Dritt-, ...-Zug, bei dem nur geschlagen werden kann.
            if (onlySchlagMoves)
            {
                choice_numeric++;
            }

            if ((choice_numeric == 1) || (liste_zuege.size() > 0 && (choice_numeric >= 2 && choice_numeric <= (liste_zuege.size() + 2))))
            {
                if (choice_numeric == 1)
                {
                    this.spiel.resetSteinWahl();
                    this.displayMainGameMenu();
                }
                else
                {
                    for (int i = 2; i < (liste_zuege.size() + 2); i++)
                    {
                        if (choice_numeric == i)
                        {
                            Spielstein stein = this.spiel.getSpielbrett().getBrett()[coords_numeric[0]][coords_numeric[1]];
                            int ziel_coords[] = liste_zuege.get(i - 2);

                            boolean geschlagen = stein.moveTo(ziel_coords[0], ziel_coords[1]);

                            if (geschlagen)
                            {
                                ArrayList<int[]> possible_schlag_moves = stein.getPossibleSchlagMoves();
                                // Wenn geschlagen werden kann.
                                if (possible_schlag_moves.size() > 0)
                                {
                                    this.displayZugMenu(true);
                                }
                            }

                            // Spiel geht nur weiter, wenn beide Spieler noch Steine haben ...
                            if ((this.spiel.getSpieler1().countSteineGesamt() > 0) && (this.spiel.getSpieler2().countSteineGesamt() > 0))
                            {
                                if (this.spiel.getCurrentSpieler() == this.spiel.getSpieler1())
                                {
                                    this.spiel.setCurrentSpieler(this.spiel.getSpieler2());
                                }
                                else
                                {
                                    this.spiel.setCurrentSpieler(this.spiel.getSpieler1());
                                }

                                this.spiel.resetSteinWahl();

                                this.displayMainGameMenu();
                            }
                            else
                            {
                                this.drawGameEndScreen();
                            }
                        }
                    }
                }
            }
            else
            {
                System.out.println("\nFehler: Bitte triff eine gültige Wahl.");
                this.displayZugMenu(onlySchlagMoves);
            }

        }
        catch (NumberFormatException e)
        {
            System.out.println("\nFehler: Bitte geben Sie eine Zahl ein!");
            new Scanner(System.in).nextLine();
            this.displayZugMenu(onlySchlagMoves);
        }

    }

    public void displayWaehleStein()
    {
        this.spiel.resetSteinWahl();

        boolean wahlGueltig = false;

        while (!wahlGueltig)
        {
            this.drawSpiel();
            System.out.print("\n\nWähle einen Stein (a-h,1-8): ");

            String choice = new Scanner(System.in).nextLine();

            String choice_split[] = choice.split(",");

            if (choice_split.length != 2)
            {
                if (choice.length() == 2)
                {
                    choice = choice.charAt(0) + "," + choice.charAt(1);
                    choice_split = choice.split(",");
                }
            }

            if ((choice_split.length == 2) && (choice_split[0].toLowerCase().equals("a") || choice_split[0].toLowerCase().equals("b") || choice_split[0].toLowerCase().equals("c") || choice_split[0].toLowerCase().equals("d") || choice_split[0].toLowerCase().equals("e") || choice_split[0].toLowerCase().equals("f") || choice_split[0].toLowerCase().equals("g") || choice_split[0].toLowerCase().equals("h")) && (choice_split[1].toLowerCase().equals("1") || choice_split[1].toLowerCase().equals("2") || choice_split[1].toLowerCase().equals("3") || choice_split[1].toLowerCase().equals("4") || choice_split[1].toLowerCase().equals("5") || choice_split[1].toLowerCase().equals("6") || choice_split[1].toLowerCase().equals("7") || choice_split[1].toLowerCase().equals("8")))
            {
                int zeile = Tools.translateCoordsToInt(choice_split[0], choice_split[1])[0];
                int spalte = Tools.translateCoordsToInt(choice_split[0], choice_split[1])[1];

                char valid_color = this.spiel.getCurrentSpieler().getColor();

                if (this.spiel.getSpielbrett().getBrett()[zeile][spalte] != null)
                {
                    if (this.spiel.getSpielbrett().getBrett()[zeile][spalte].getColor() == valid_color)
                    {
                        this.spiel.getSpielbrett().getBrett()[zeile][spalte].setAusgewaehlt(true);
                        wahlGueltig = true;
                        this.displayMainGameMenu();
                    }
                    else
                    {
                        System.out.println("\nFehler: Du kannst nur Steine Deiner Farbe wählen!");
                        new Scanner(System.in).nextLine();
                    }
                }
                else
                {
                    System.out.println("\nFehler: Auf diesem Feld befindet sich kein Stein!");
                    new Scanner(System.in).nextLine();
                }
            }
            else
            {
                System.out.println("\nFehler: Bitte wähle einen gültigen Stein.");
                new Scanner(System.in).nextLine();
            }
        }

    }

    public void displayMainGameMenu()
    {

        if (!this.spiel.checkSteinGewaehlt())
        {
            this.displayWaehleStein();
        }
        else
        {
            this.displayZugMenu(false);
        }
    }

    public void drawSpiel()
    {
        System.out.print("\f");

        String color = "schwarz";

        if (this.spiel.getCurrentSpieler().getColor() == 'w')
        {
            color = "weiß";
        }

        System.out.print(this.spiel.getCurrentSpieler().getName() + " ist am Zug (" + color + "):\n");
        this.drawSpielfeld();
    }

    public void drawGameEndScreen()
    {
        String name_winner = this.spiel.getSpieler1().getName().toUpperCase();
        String name_loser = this.spiel.getSpieler2().getName();
        
        if (this.spiel.getSpieler1().countSteineGesamt() == 0)
        {
            name_winner = this.spiel.getSpieler2().getName().toUpperCase();
            name_loser = this.spiel.getSpieler1().getName();
        }
        
        System.out.println("\f\n     " + name_winner + " GEWINNT!!!1111elf");
        System.out.println("	 ... und schickt " + name_loser + " in die Offline-Halle!\n");
        System.out.println("        *     (   +      *.  . )");
        System.out.println("           )    .  ____+      (   *  ");
        System.out.println("       .  (  \\||  (//\\_ \\.\\|/       .");
        System.out.println("        + .-\"-\\ \\ `a,a `/ / | + (");
        System.out.println("         /     \\ \\ \\O_ / / /     )  +");
        System.out.println("      .  |#    |\\ \\_) (_/ / * .-\"-. )");
        System.out.println("          \\___/  \\       /   /#    \\  (  ");
        System.out.println("       *   /^     \\.&&&./  . |     |");
        System.out.println("          (    *  |  &  |*    \\___/");
        System.out.println("      .    \\      |__&__|  '    ^\\   + ");
        System.out.println("            )     | \\_/ |     ) . )");
        System.out.println("        *    . @%@%@%@%@%@%@ (   (");
        System.out.println("        (      {           }  )      *");
        System.out.println("         ) *   {           }     +  (");
        System.out.println("        (    @%@%@%@%@%@%@%@%@       ) '");
        System.out.println("      +      {               }  *   (");
        System.out.println("             {   .........   }   ");
        System.out.println("             {               }        (");
        System.out.println("     *      @%@%@%@%@%@%@%@%@%@    +");
        
        new Scanner(System.in).nextLine();
        
        this.displayMainMenu();
    }

    public void drawSpielfeld()
    {
        Spielbrett brett = this.spiel.getSpielbrett();
        String spieler1_color = "weiß";
        String spieler2_color = "schwarz";

        if (this.spiel.getSpieler1().getColor() == 's')
        {
            spieler1_color = "schwarz";
            spieler2_color = "weiß";
        }

        for (int i = 7; i >= 0; i--)
        {

            if (i == 7)
            {
                System.out.print("  +---+---+---+---+---+---+---+---+\n");
            }

            for (int k = 0; k < 8; k++)
            {
                if (k == 0)
                {
                    System.out.print((i + 1) + " |");
                }

                if (brett.getBrett()[i][k] != null)
                {
                    String spielstein_string = "S";

                    if (brett.getBrett()[i][k].getColor() == 'w')
                    {
                        spielstein_string = "W";
                    }

                    if (brett.getBrett()[i][k].getAusgewaehlt() == true)
                    {
                        if (brett.getBrett()[i][k].getIsDame())
                        {
                            spielstein_string = "[" + spielstein_string + "]";
                        }
                        else
                        {
                            spielstein_string += "*";
                        }

                    }
                    else
                    {
                        if (!brett.getBrett()[i][k].getIsDame())
                        {
                            spielstein_string += " ";
                        }

                    }

                    if (brett.getBrett()[i][k].getIsDame())
                    {
                        if (spielstein_string.length() == 1)
                        {
                            System.out.print("(" + spielstein_string + ")|");
                        }
                        else
                        {
                            System.out.print(spielstein_string + "|");
                        }

                    }
                    else
                    {
                        System.out.print(" " + spielstein_string + "|");
                    }
                }
                else
                {
                    System.out.print("   |");
                }

                if (k == 7)
                {
                    if (i == 7)
                    {
                        System.out.print("     Spielstand:");
                    }

                    if (i == 6)
                    {
                        System.out.print("     " + this.spiel.getSpieler1().getName() + " (" + spieler1_color + ")");
                    }

                    if (i == 5)
                    {
                        System.out.print("     Normale Steine:  " + this.spiel.getSpieler1().countNormaleSteine());
                    }

                    if (i == 4)
                    {
                        System.out.print("     " + this.spiel.getSpieler2().getName() + " (" + spieler2_color + ")");
                    }

                    if (i == 3)
                    {
                        System.out.print("     Normale Steine:  " + this.spiel.getSpieler2().countNormaleSteine());
                    }

                    System.out.print("\n");
                }
            }

            System.out.print("  +---+---+---+---+---+---+---+---+");

            if (i == 7)
            {
                System.out.print("     -------------------");
            }

            if (i == 6)
            {
                System.out.print("     Damen:           " + this.spiel.getSpieler1().countDamen());
            }

            if (i == 4)
            {
                System.out.print("     Damen:           " + this.spiel.getSpieler2().countDamen());
            }

            System.out.print("\n");
        }

        System.out.print("    a   b   c   d   e   f   g   h");
    }
}
