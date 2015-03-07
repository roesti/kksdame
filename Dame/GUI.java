import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * GUI Klasse - Ausgabe des Damespiels mittels der Grafikbibliotheken von Java
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */

public class GUI implements UI, MouseListener
{
    private Spiel spiel;

    private Image game_background = new ImageIcon("images/game_background.jpg").getImage();

    private Image bg_schwarz = new ImageIcon("images/tile_black.png").getImage();
    private Image bg_weiss = new ImageIcon("images/tile_white.png").getImage();
    private Image stein_weiss = new ImageIcon("images/stein_white.png").getImage();
    private Image dame_weiss = new ImageIcon("images/dame_white.png").getImage();
    private Image stein_schwarz = new ImageIcon("images/stein_black.png").getImage();
    private Image dame_schwarz = new ImageIcon("images/dame_black.png").getImage();
    private Image tile_clear = new ImageIcon("images/tile_clear.png").getImage();
    private Image tile_blue = new ImageIcon("images/tile_chosen.png").getImage();
    private Image tile_red = new ImageIcon("images/tile_red.png").getImage();
    private Image tile_green = new ImageIcon("images/tile_green.png").getImage();

    private JFrame mainWindow;
    private JPanel checkerBoard;

    private JLabel zugStatusLabel;
    private JLabel spielstandLabel;
    private JLabel spielstandSpieler1Label;
    private JLabel spielstandSpieler1Damen;
    private JLabel spielstandSpieler1Steine;
    private JLabel spielstandSpieler2Label;
    private JLabel spielstandSpieler2Damen;
    private JLabel spielstandSpieler2Steine;

    /**
     * Constructor for objects of class KonsoleUI
     */
    public GUI(Spiel spiel)
    {
        this.spiel = spiel;
    }

    public void displayMainMenu()
    {
        
        this.spiel.startGame();
        
    }

    public void displayStartGameMenu()
    {
        String spieler1_name = "";
        String spieler2_name = "";
        
        while (spieler1_name.trim().equals(""))
        {
            spieler1_name = JOptionPane.showInputDialog(this.mainWindow, "Name des 1. Spielers:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);
        }
        
        this.spiel.getSpieler1().setName(spieler1_name);
        
        while (spieler2_name.trim().equals(""))
        {
            spieler2_name = JOptionPane.showInputDialog(this.mainWindow, "Name des 2. Spielers:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);
        }
        
        this.spiel.getSpieler2().setName(spieler2_name);
    }

    public void displayMainGameMenu()
    {
        this.drawSpiel();
        
        
        this.setzeSteine();
    }

    public void drawSpielfeld()
    {

    }

    public void setzeSteine()
    {
        int offset_stein = 5;

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                int grid_index = (i * 8) + k;
                ImagePanel gridCell = (ImagePanel) this.checkerBoard.getComponent(grid_index);
                ImagePanel background_panel = (ImagePanel) gridCell.getComponent(0);
                background_panel.removeAll();

                Spielstein stein = this.spiel.getSpielbrett().getBrett()[(7 - i)][k];

                if (stein != null)
                {
                    if (stein.getAusgewaehlt() == true)
                    {
                        background_panel.setImage(tile_blue);
                    }
                    else
                    {
                        background_panel.setImage(tile_clear);
                    }

                    if (stein.getColor() == 'w')
                    {
                        if (stein.getIsDame() == true)
                        {
                            ImagePanel steinImage = new ImagePanel(dame_weiss);
                            steinImage.setLocation(offset_stein, offset_stein);
                            background_panel.add(steinImage);
                        }
                        else
                        {
                            ImagePanel steinImage = new ImagePanel(stein_weiss);
                            steinImage.setLocation(offset_stein, offset_stein);
                            background_panel.add(steinImage);
                        }
                    }
                    else
                    {
                        if (stein.getIsDame() == true)
                        {
                            ImagePanel steinImage = new ImagePanel(dame_schwarz);
                            steinImage.setLocation(offset_stein, offset_stein);
                            background_panel.add(steinImage);
                        }
                        else
                        {
                            ImagePanel steinImage = new ImagePanel(stein_schwarz);
                            steinImage.setLocation(offset_stein, offset_stein);
                            background_panel.add(steinImage);
                        }
                    }
                }
                else
                {
                    background_panel.setImage(tile_clear);
                }

            }
        }
    }

    public void drawSpiel()
    {
        int zeilen = this.spiel.getSpielbrett().getBrett().length;
        int spalten = this.spiel.getSpielbrett().getBrett()[0].length;

        this.mainWindow = new JFrame();
        mainWindow.setSize(1100, 800);

        mainWindow.setContentPane(new ImagePanel(this.game_background));
        mainWindow.getContentPane().setLayout(null);
        mainWindow.setResizable(false);
        mainWindow.setLocationRelativeTo(null);

        this.checkerBoard = new JPanel();
        checkerBoard.setSize(68 * spalten, 68 * zeilen);
        checkerBoard.setLocation(50, 50);
        mainWindow.setTitle("KKSDame 0.1 by 12FO3");
        checkerBoard.setLayout(new GridLayout(zeilen, spalten));
        Color temp;

        for (int i = 0; i < zeilen; i++)
        {

            for (int k = 0; k < spalten; k++)
            {
                Image bg_image = null;

                if ((k % 2) == 0)
                {
                    if ((i % 2) == 0)
                    {
                        bg_image = this.bg_weiss;
                    }
                    else
                    {
                        bg_image = this.bg_schwarz;
                    }
                }
                else
                {
                    if ((i % 2) == 0)
                    {
                        bg_image = bg_schwarz;
                    }
                    else
                    {
                        bg_image = bg_weiss;
                    }
                }

                ImagePanel background_panel = new ImagePanel(tile_clear);
                ImagePanel panel = new ImagePanel(bg_image);
                panel.addMouseListener(this);
                panel.add(background_panel);

                checkerBoard.add(panel);
            }
        }

        mainWindow.getContentPane().add(checkerBoard);

        // Vertikale Spielfeldbeschriftungen setzen (1 bis 8)
        for (int i = 0; i < 8; i++)
        {
            int start_pos_vertical = this.checkerBoard.getHeight() + this.checkerBoard.getY() - (i * 68) - 55;
            int start_pos_left = this.checkerBoard.getX() - 30;
            int start_pos_right = this.checkerBoard.getX() + this.checkerBoard.getWidth() + 25;

            JLabel row_left_label = new JLabel(new Integer(i + 1).toString());
            row_left_label.setLayout(null);
            row_left_label.setLocation(start_pos_left, start_pos_vertical);
            row_left_label.setSize(50, 40);
            row_left_label.setFont(new Font("Arial", Font.BOLD, 14));
            row_left_label.setHorizontalTextPosition(JLabel.LEFT);
            mainWindow.getContentPane().add(row_left_label);

            JLabel row_right_label = new JLabel(new Integer(i + 1).toString());
            row_right_label.setLayout(null);
            row_right_label.setLocation(start_pos_right, start_pos_vertical);
            row_right_label.setSize(50, 40);
            row_right_label.setFont(new Font("Arial", Font.BOLD, 14));
            row_right_label.setHorizontalTextPosition(JLabel.LEFT);
            mainWindow.getContentPane().add(row_right_label);

        }

        // Horizontale Spielfeldbeschriftungen setzen (A - H)
        for (int i = 0; i < 8; i++)
        {
            int start_pos_horizontal = this.checkerBoard.getX() + (i * 68) + 30;
            int start_pos_top = this.checkerBoard.getY() - 40;
            int start_pos_bottom = this.checkerBoard.getY() + this.checkerBoard.getHeight() + 5;

            String letter = null;

            switch (i)
            {
                case 0:
                letter = "A";
                break;

                case 1:
                letter = "B";
                break;

                case 2:
                letter = "C";
                break;

                case 3:
                letter = "D";
                break;

                case 4:
                letter = "E";
                break;

                case 5:
                letter = "F";
                break;

                case 6:
                letter = "G";
                break;

                case 7:
                letter = "H";
                break;
            }

            JLabel col_top_label = new JLabel(letter);
            col_top_label.setLayout(null);
            col_top_label.setLocation(start_pos_horizontal, start_pos_top);
            col_top_label.setSize(50, 40);
            col_top_label.setFont(new Font("Arial", Font.BOLD, 14));
            col_top_label.setHorizontalTextPosition(JLabel.LEFT);
            mainWindow.getContentPane().add(col_top_label);

            JLabel col_bottom_label = new JLabel(letter);
            col_bottom_label.setLayout(null);
            col_bottom_label.setLocation(start_pos_horizontal, start_pos_bottom);
            col_bottom_label.setSize(50, 40);
            col_bottom_label.setFont(new Font("Arial", Font.BOLD, 14));
            col_bottom_label.setHorizontalTextPosition(JLabel.LEFT);
            mainWindow.getContentPane().add(col_bottom_label);

        }
        
        String color = "schwarz";
        
        if (this.spiel.getCurrentSpieler().getColor() == 'w')
        {
            color = "weiß";
        }

        this.zugStatusLabel = new JLabel(this.spiel.getCurrentSpieler().getName() + " (" + color + ") ist am Zug!");
        zugStatusLabel.setLayout(null);
        zugStatusLabel.setLocation(700, 40);
        zugStatusLabel.setSize(300, 30);
        zugStatusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        zugStatusLabel.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(zugStatusLabel);
        
        this.spielstandLabel = new JLabel("Spielstand:");
        spielstandLabel.setLayout(null);
        spielstandLabel.setLocation(700, 80);
        spielstandLabel.setSize(300, 30);
        spielstandLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        spielstandLabel.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandLabel);
        
        
        
        
        /*private JLabel zugStatusLabel;

        private JLabel spielstandLabel;
        private JLabel spielstandSpieler1Label;
        private JLabel spielstandSpieler1Damen;
        private JLabel spielstandSpieler1Steine;
        private JLabel spielstandSpieler2Label;
        private JLabel spielstandSpieler2Damen;
        private JLabel spielstandSpieler2Steine;*/

        mainWindow.setVisible(true);
    }

    // Implementieren der Methoden für den
    // MouseMotionListeners und den MouseListener
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseClicked(MouseEvent e)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                int grid_index = (i * 8) + k;

                if (this.checkerBoard.getComponent(grid_index) == e.getComponent())
                {
                    JOptionPane.showMessageDialog(mainWindow, (7 - i) + "," + k);
                }

            }
        }

    }

    public void mouseReleased(MouseEvent e) {}

}