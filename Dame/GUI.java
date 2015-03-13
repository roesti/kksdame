import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * GUI Klasse - Ausgabe des Damespiels mittels der Grafikbibliotheken von Java
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */

public class GUI implements UI, MouseListener, ActionListener
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
    private Image start_splash = new ImageIcon("images/start_splash.png").getImage();

    private JFrame mainWindow;
    private JPanel checkerBoard;
    private ImagePanel startSplash;

    private JLabel zugStatusLabel;
    private JLabel spielstandLabel;
    private JLabel spielstandSpieler1Label;
    private JLabel spielstandSpieler1Damen;
    private JLabel spielstandSpieler1DamenValue;
    private JLabel spielstandSpieler1Steine;
    private JLabel spielstandSpieler1SteineValue;
    private JLabel spielstandSpieler2Label;
    private JLabel spielstandSpieler2Damen;
    private JLabel spielstandSpieler2DamenValue;
    private JLabel spielstandSpieler2Steine;
    private JLabel spielstandSpieler2SteineValue;
    private ArrayList<JLabel> checkerBoardRowColumnLabels;
    private JButton newGameButton;
    private JButton giveUpButton;
    private JButton exitButton;
    private JMenuBar menuBar;
    private JMenu menuSpiel;
    private JMenu menuHilfe;
    private JMenuItem menuItemNeuesSpiel;
    private JMenuItem menuItemNetworkSpiel;
    private JMenuItem menuItemExit;
    private JMenuItem menuItemAbout;
    private NetzwerkLobby netzwerkLobby;

    private boolean steinAngeklickt;
    private ImagePanel bewegtesPanel;
    private boolean uiInitialized;
    private boolean spielEnde;
    private boolean isNetworkGame;

    /**
     * Constructor for objects of class KonsoleUI
     */
    public GUI(Spiel spiel)
    {
        this.spiel = spiel;
        this.steinAngeklickt = false;
        this.bewegtesPanel = null;
        this.uiInitialized = false;
        this.spielEnde = false;
        this.isNetworkGame = false;
    }

    public void setIsNetworkGame(boolean isNetworkGame)
    {
        this.isNetworkGame = isNetworkGame;
    }

    public boolean getIsNetworkGame()
    {
        return this.isNetworkGame;
    }

    public void displayMainMenu()
    {
        this.steinAngeklickt = false;
        this.bewegtesPanel = null;
        this.spielEnde = false;
        this.drawSpiel();
        //this.spiel.startGame();
    }

    public void displayStartGameMenu()
    {

        if (!this.isNetworkGame)
        {
            String spieler1_name = "";
            String spieler2_name = "";
            boolean no_valid_player1_name = true;
            boolean no_valid_player2_name = true;

            while (no_valid_player1_name)
            {
                spieler1_name = JOptionPane.showInputDialog(this.mainWindow, "Name des 1. Spielers:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);

                if (spieler1_name != null)
                {
                    if (!spieler1_name.trim().equals(""))
                    {
                        no_valid_player1_name = false;
                    }
                }
            }

            this.spiel.getSpieler1().setName(spieler1_name);

            while (no_valid_player2_name)
            {
                spieler2_name = JOptionPane.showInputDialog(this.mainWindow, "Name des 2. Spielers:", "Namenseingabe", JOptionPane.PLAIN_MESSAGE);

                if (spieler2_name != null)
                {
                    if (!spieler2_name.trim().equals(""))
                    {
                        no_valid_player2_name = false;
                    }
                }
            }

            this.spiel.getSpieler2().setName(spieler2_name);
        }

        this.checkerBoard.setVisible(true);
        this.zugStatusLabel.setVisible(true);
        this.spielstandLabel.setVisible(true);
        this.spielstandSpieler1Label.setVisible(true);
        this.spielstandSpieler1Damen.setVisible(true);
        this.spielstandSpieler1DamenValue.setVisible(true);
        this.spielstandSpieler1Steine.setVisible(true);
        this.spielstandSpieler1SteineValue.setVisible(true);
        this.spielstandSpieler2Label.setVisible(true);
        this.spielstandSpieler2Damen.setVisible(true);
        this.spielstandSpieler2DamenValue.setVisible(true);
        this.spielstandSpieler2Steine.setVisible(true);
        this.spielstandSpieler2SteineValue.setVisible(true);

        if (!this.isNetworkGame)
        {
            this.newGameButton.setVisible(true);
            this.giveUpButton.setVisible(false);
        }
        else
        {
            this.newGameButton.setVisible(false);
            this.giveUpButton.setVisible(true);
        }

        this.exitButton.setVisible(true);
        this.startSplash.setVisible(false);

        for (JLabel label : this.checkerBoardRowColumnLabels)
        {
            label.setVisible(true);
        }

    }

    public void displayMainGameMenu()
    {
        String color_spieler_current = "schwarz";
        String color_spieler_1 = "schwarz";
        String color_spieler_2 = "schwarz";

        if (this.spiel.getCurrentSpieler().getColor() == 'w')
        {
            color_spieler_current = "weiß";
        }

        if (this.spiel.getSpieler1().getColor() == 'w')
        {
            color_spieler_1 = "weiß";
        }

        if (this.spiel.getSpieler2().getColor() == 'w')
        {
            color_spieler_2 = "weiß";
        }

        this.spielstandSpieler1Label.setText(this.spiel.getSpieler1().getName() + " (" + color_spieler_1 + ")");
        this.spielstandSpieler2Label.setText(this.spiel.getSpieler2().getName() + " (" + color_spieler_2 + ")");

        if (!this.uiInitialized)
        {
            this.uiInitialized = true;
            //this.drawSpiel();
        }

        this.setzeSteine();
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

                background_panel.validate();
                background_panel.repaint();

            }
        }

        String color_spieler_current = "schwarz";

        if (this.spiel.getCurrentSpieler().getColor() == 'w')
        {
            color_spieler_current = "weiß";
        }

        if (!this.spielEnde)
        {
            this.zugStatusLabel.setText(this.spiel.getCurrentSpieler().getName() + " (" + color_spieler_current + ") ist am Zug!");
        }

        this.spielstandSpieler1DamenValue.setText(new Integer(this.spiel.getSpieler1().countDamen()).toString());
        this.spielstandSpieler1SteineValue.setText(new Integer(this.spiel.getSpieler1().countNormaleSteine()).toString());
        this.spielstandSpieler2DamenValue.setText(new Integer(this.spiel.getSpieler2().countDamen()).toString());
        this.spielstandSpieler2SteineValue.setText(new Integer(this.spiel.getSpieler2().countNormaleSteine()).toString());

    }

    public void drawSpiel()
    {
        int zeilen = this.spiel.getSpielbrett().getBrett().length;
        int spalten = this.spiel.getSpielbrett().getBrett()[0].length;

        this.mainWindow = new JFrame();
        this.mainWindow.setSize(1075, 700);

        ArrayList<Image> icons = new ArrayList<Image>();
        icons.add(new ImageIcon("images/icon_16.png").getImage());
        icons.add(new ImageIcon("images/icon_32.png").getImage());
        this.mainWindow.setIconImages(icons);

        this.mainWindow.setContentPane(new ImagePanel(this.game_background));
        this.mainWindow.getContentPane().setLayout(null);
        this.mainWindow.setResizable(false);
        this.mainWindow.setLocationRelativeTo(null);
        this.mainWindow.setTitle("KKSDame 0.1 by 12FO3 (GUI)");

        this.checkerBoard = new JPanel();
        this.checkerBoard.setSize(68 * spalten, 68 * zeilen);
        this.checkerBoard.setLocation(50, 50);
        this.checkerBoard.setLayout(new GridLayout(zeilen, spalten));

        Color temp;

        this.checkerBoardRowColumnLabels = new ArrayList<JLabel>();

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

                this.checkerBoard.add(panel);
            }
        }

        this.mainWindow.getContentPane().add(checkerBoard);

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
            this.checkerBoardRowColumnLabels.add(row_left_label);
            this.mainWindow.getContentPane().add(row_left_label);

            JLabel row_right_label = new JLabel(new Integer(i + 1).toString());
            row_right_label.setLayout(null);
            row_right_label.setLocation(start_pos_right, start_pos_vertical);
            row_right_label.setSize(50, 40);
            row_right_label.setFont(new Font("Arial", Font.BOLD, 14));
            row_right_label.setHorizontalTextPosition(JLabel.LEFT);
            this.checkerBoardRowColumnLabels.add(row_right_label);
            this.mainWindow.getContentPane().add(row_right_label);

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
            this.checkerBoardRowColumnLabels.add(col_top_label);
            this.mainWindow.getContentPane().add(col_top_label);

            JLabel col_bottom_label = new JLabel(letter);
            col_bottom_label.setLayout(null);
            col_bottom_label.setLocation(start_pos_horizontal, start_pos_bottom);
            col_bottom_label.setSize(50, 40);
            col_bottom_label.setFont(new Font("Arial", Font.BOLD, 14));
            col_bottom_label.setHorizontalTextPosition(JLabel.LEFT);
            this.checkerBoardRowColumnLabels.add(col_bottom_label);
            this.mainWindow.getContentPane().add(col_bottom_label);
        }

        this.zugStatusLabel = new JLabel();
        this.zugStatusLabel.setLayout(null);
        this.zugStatusLabel.setLocation(700, 40);
        this.zugStatusLabel.setSize(300, 30);
        this.zugStatusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.zugStatusLabel.setForeground(Color.red);
        this.zugStatusLabel.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(zugStatusLabel);

        this.spielstandLabel = new JLabel("Spielstand:");
        this.spielstandLabel.setLayout(null);
        this.spielstandLabel.setLocation(700, 110);
        this.spielstandLabel.setSize(300, 30);
        this.spielstandLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.spielstandLabel.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandLabel);

        this.spielstandSpieler1Label = new JLabel();
        this.spielstandSpieler1Label.setLayout(null);
        this.spielstandSpieler1Label.setLocation(700, 160);
        this.spielstandSpieler1Label.setSize(300, 30);
        this.spielstandSpieler1Label.setFont(new Font("Arial", Font.BOLD, 16));
        this.spielstandSpieler1Label.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler1Label);

        this.spielstandSpieler1Damen = new JLabel("Damen:");
        this.spielstandSpieler1Damen.setLayout(null);
        this.spielstandSpieler1Damen.setLocation(700, 185);
        this.spielstandSpieler1Damen.setSize(300, 30);
        this.spielstandSpieler1Damen.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler1Damen.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler1Damen);

        this.spielstandSpieler1DamenValue = new JLabel();
        this.spielstandSpieler1DamenValue.setLayout(null);
        this.spielstandSpieler1DamenValue.setLocation(900, 185);
        this.spielstandSpieler1DamenValue.setSize(300, 30);
        this.spielstandSpieler1DamenValue.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler1DamenValue.setHorizontalTextPosition(JLabel.RIGHT);
        this.mainWindow.getContentPane().add(spielstandSpieler1DamenValue);

        this.spielstandSpieler1Steine = new JLabel("Normale Steine:");
        this.spielstandSpieler1Steine.setLayout(null);
        this.spielstandSpieler1Steine.setLocation(700, 210);
        this.spielstandSpieler1Steine.setSize(300, 30);
        this.spielstandSpieler1Steine.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler1Steine.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler1Steine);

        this.spielstandSpieler1SteineValue = new JLabel();
        this.spielstandSpieler1SteineValue.setLayout(null);
        this.spielstandSpieler1SteineValue.setLocation(900, 210);
        this.spielstandSpieler1SteineValue.setSize(300, 30);
        this.spielstandSpieler1SteineValue.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler1SteineValue.setHorizontalTextPosition(JLabel.RIGHT);
        this.mainWindow.getContentPane().add(spielstandSpieler1SteineValue);

        this.spielstandSpieler2Label = new JLabel();
        this.spielstandSpieler2Label.setLayout(null);
        this.spielstandSpieler2Label.setLocation(700, 250);
        this.spielstandSpieler2Label.setSize(300, 30);
        this.spielstandSpieler2Label.setFont(new Font("Arial", Font.BOLD, 16));
        this.spielstandSpieler2Label.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler2Label);

        this.spielstandSpieler2Damen = new JLabel("Damen:");
        this.spielstandSpieler2Damen.setLayout(null);
        this.spielstandSpieler2Damen.setLocation(700, 275);
        this.spielstandSpieler2Damen.setSize(300, 30);
        this.spielstandSpieler2Damen.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler2Damen.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler2Damen);

        this.spielstandSpieler2DamenValue = new JLabel();
        this.spielstandSpieler2DamenValue.setLayout(null);
        this.spielstandSpieler2DamenValue.setLocation(900, 275);
        this.spielstandSpieler2DamenValue.setSize(300, 30);
        this.spielstandSpieler2DamenValue.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler2DamenValue.setHorizontalTextPosition(JLabel.RIGHT);
        this.mainWindow.getContentPane().add(spielstandSpieler2DamenValue);

        this.spielstandSpieler2Steine = new JLabel("Normale Steine:");
        this.spielstandSpieler2Steine.setLayout(null);
        this.spielstandSpieler2Steine.setLocation(700, 300);
        this.spielstandSpieler2Steine.setSize(300, 30);
        this.spielstandSpieler2Steine.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler2Steine.setHorizontalTextPosition(JLabel.LEFT);
        this.mainWindow.getContentPane().add(spielstandSpieler2Steine);

        this.spielstandSpieler2SteineValue = new JLabel();
        this.spielstandSpieler2SteineValue.setLayout(null);
        this.spielstandSpieler2SteineValue.setLocation(900, 300);
        this.spielstandSpieler2SteineValue.setSize(300, 30);
        this.spielstandSpieler2SteineValue.setFont(new Font("Arial", Font.PLAIN, 16));
        this.spielstandSpieler2SteineValue.setHorizontalTextPosition(JLabel.RIGHT);
        this.mainWindow.getContentPane().add(spielstandSpieler2SteineValue);

        this.newGameButton = new JButton("Neues Spiel");
        this.newGameButton.setLayout(null);
        this.newGameButton.setLocation(700, 553);
        this.newGameButton.setSize(150, 40);
        this.newGameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        this.newGameButton.addActionListener(this);
        this.mainWindow.getContentPane().add(newGameButton);

        this.giveUpButton = new JButton("Aufgeben");
        this.giveUpButton.setLayout(null);
        this.giveUpButton.setLocation(700, 553);
        this.giveUpButton.setSize(150, 40);
        this.giveUpButton.setFont(new Font("Arial", Font.PLAIN, 16));
        this.giveUpButton.addActionListener(this);
        this.mainWindow.getContentPane().add(giveUpButton);

        this.exitButton = new JButton("Beenden");
        this.exitButton.setLayout(null);
        this.exitButton.setLocation(870, 553);
        this.exitButton.setSize(150, 40);
        this.exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        this.exitButton.addActionListener(this);
        this.mainWindow.getContentPane().add(exitButton);

        this.startSplash = new ImagePanel(this.start_splash);
        this.startSplash.setLocation(60, 150);
        this.mainWindow.getContentPane().add(this.startSplash);

        this.menuBar = new JMenuBar();
        this.menuSpiel = new JMenu("Spiel");
        this.menuSpiel.setMnemonic('S');
        this.menuHilfe = new JMenu("Hilfe");
        this.menuHilfe.setMnemonic('H');

        this.menuItemNeuesSpiel = new JMenuItem("Neues Spiel");
        this.menuItemNeuesSpiel.setMnemonic('N');
        this.menuItemNeuesSpiel.addActionListener(this);
        this.menuItemNetworkSpiel = new JMenuItem("Netzwerkspiel");
        this.menuItemNetworkSpiel.setMnemonic('E');
        this.menuItemNetworkSpiel.addActionListener(this);
        this.menuItemExit = new JMenuItem("Beenden");
        this.menuItemExit.setMnemonic('B');
        this.menuItemExit.addActionListener(this);
        this.menuItemAbout = new JMenuItem("Über");
        this.menuItemAbout.setMnemonic('Ü');
        this.menuItemAbout.addActionListener(this);

        this.menuBar.add(this.menuSpiel);
        this.menuBar.add(this.menuHilfe);

        this.menuSpiel.add(this.menuItemNeuesSpiel);
        this.menuSpiel.add(this.menuItemNetworkSpiel);
        this.menuSpiel.addSeparator();
        this.menuSpiel.add(this.menuItemExit);

        this.menuHilfe.add(this.menuItemAbout);

        this.checkerBoard.setVisible(false);
        this.zugStatusLabel.setVisible(false);
        this.spielstandLabel.setVisible(false);
        this.spielstandSpieler1Label.setVisible(false);
        this.spielstandSpieler1Damen.setVisible(false);
        this.spielstandSpieler1DamenValue.setVisible(false);
        this.spielstandSpieler1Steine.setVisible(false);
        this.spielstandSpieler1SteineValue.setVisible(false);
        this.spielstandSpieler2Label.setVisible(false);
        this.spielstandSpieler2Damen.setVisible(false);
        this.spielstandSpieler2DamenValue.setVisible(false);
        this.spielstandSpieler2Steine.setVisible(false);
        this.spielstandSpieler2SteineValue.setVisible(false);
        this.newGameButton.setVisible(false);
        this.giveUpButton.setVisible(false);
        this.exitButton.setVisible(false);
        this.startSplash.setVisible(true);

        this.mainWindow.addWindowListener(new java.awt.event.WindowAdapter()
            {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent)
                {
                    if (netzwerkLobby instanceof NetzwerkLobby)
                    {
                        if (netzwerkLobby.getNetworkClient() instanceof DameClient)
                        {
                            netzwerkLobby.getNetworkClient().sendMessageToServer("DISCONNECT");
                        }
                    }

                }
            });

        for (JLabel label : this.checkerBoardRowColumnLabels)
        {
            label.setVisible(false);
        }

        this.mainWindow.setJMenuBar(this.menuBar);

        this.mainWindow.setVisible(true);
    }

    public int[] getSpielfeldPosition(ImagePanel panel)
    {
        int[] position = new int[2];

        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                int grid_index = (i * 8) + k;

                if (this.checkerBoard.getComponent(grid_index) == panel)
                {
                    position[0] = 7 - i;
                    position[1] = k;
                }
            }
        }

        return position;
    }

    public void spielEnde()
    {   
        String name_winner = this.spiel.getSpieler1().getName().toUpperCase();
        String name_loser = this.spiel.getSpieler2().getName();
        String message = null;
        this.spielEnde = true;

        if (this.spiel.getSpieler1().countSteineGesamt() > this.spiel.getSpieler2().countSteineGesamt())
        {
            name_winner = this.spiel.getSpieler1().getName().toUpperCase();
            name_loser = this.spiel.getSpieler2().getName();
            message = name_winner + " gewinnt!";
        }
        else if (this.spiel.getSpieler1().countSteineGesamt() < this.spiel.getSpieler2().countSteineGesamt())
        {
            name_winner = this.spiel.getSpieler2().getName().toUpperCase();
            name_loser = this.spiel.getSpieler1().getName();
            message = name_winner + " gewinnt!";
        }
        else
        {
            message = "Unentschieden!";
        }

        this.zugStatusLabel.setText(message);

        JOptionPane.showMessageDialog(this.mainWindow, message);

    }

    public JFrame getMainWindow()
    {
        return this.mainWindow;
    }

    public void startNetworkGame(String player1_name, char player1_color, String player2_name, char player2_color, int player1_id, int player2_id)
    {

        if (this.netzwerkLobby instanceof NetzwerkLobby)
        {
            this.steinAngeklickt = false;
            this.bewegtesPanel = null;
            this.spielEnde = false;

            if (this.netzwerkLobby.getNetworkClient().getID() == player1_id)
            {
                this.spiel.setNetworkPlayerSelf(this.spiel.getSpieler1());
            }
            else
            {
                this.spiel.setNetworkPlayerSelf(this.spiel.getSpieler2());
            }

            this.spiel.startNetworkGame(player1_name, player1_color, player2_name, player2_color);
        }

    }

    public void networkEndTurn()
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
        this.setzeSteine();
    }

    public void networkOpponentMoved(int stein_zeile, int stein_spalte, int pos_zeile, int pos_spalte)
    {
        Spielstein stein = this.spiel.getSpielbrett().getBrett()[stein_zeile][stein_spalte];

        if (stein != null)
        {
            stein.moveTo(pos_zeile, pos_spalte);
            this.setzeSteine();
        }
    }

    public void networkEndGame()
    {
        this.setzeSteine();
        this.spielEnde();

        /*java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
        netzwerkLobby.getMainWindow().toFront();
        netzwerkLobby.getMainWindow().setModal(false);
        netzwerkLobby.getMainWindow().enable();
        netzwerkLobby.getMainWindow().repaint();
        }
        });*/
    }

    public void networkOpponentQuit()
    {

        this.spielEnde = true;
        this.zugStatusLabel.setText("Spiel abgebrochen!");

        this.giveUpButton.setVisible(false);
        this.newGameButton.setVisible(true);

        JOptionPane.showMessageDialog(this.mainWindow, "Gegner hat das Spiel abgebrochen / verlassen!");
        this.netzwerkLobby.getMainWindow().setVisible(true);

        /*java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
        netzwerkLobby.getMainWindow().toFront();
        netzwerkLobby.getMainWindow().setModal(false);
        netzwerkLobby.getMainWindow().enable();
        netzwerkLobby.getMainWindow().repaint();
        }
        });*/
    }

    // Implementieren der Methoden für den
    // MouseMotionListeners und den MouseListener

    public void mouseEntered(MouseEvent e)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                int grid_index = (i * 8) + k;

                if (this.checkerBoard.getComponent(grid_index) == e.getComponent())
                {

                    if (this.steinAngeklickt)
                    {
                        ImagePanel hoveredField = (ImagePanel) this.checkerBoard.getComponent(grid_index);
                        ImagePanel backgroundField = (ImagePanel) hoveredField.getComponent(0);
                        int arrayPos[] = this.getSpielfeldPosition(this.bewegtesPanel);
                        int newPos[] = new int[2];
                        newPos[0] = 7 - i;
                        newPos[1] = k;

                        Spielstein stein = this.spiel.getSpielbrett().getBrett()[arrayPos[0]][arrayPos[1]];

                        if (backgroundField.getComponentCount() == 0)
                        {
                            backgroundField.removeAll();

                            int offset_stein = 5;
                            ArrayList<int[]> possibleMoves = stein.getPossibleMoves();
                            boolean validMove = false;

                            for (int[] pos : possibleMoves)
                            {
                                if ((pos[0] == newPos[0]) && (pos[1] == newPos[1]))
                                {
                                    validMove = true;
                                }
                            }

                            if (validMove)
                            {
                                backgroundField.setImage(tile_green);
                            }
                            else
                            {
                                backgroundField.setImage(tile_red);
                            }

                            if (stein.getColor() == 'w')
                            {
                                if (stein.getIsDame() == true)
                                {
                                    ImagePanel steinImage = new ImagePanel(dame_weiss);
                                    steinImage.setLocation(offset_stein, offset_stein);
                                    backgroundField.add(steinImage);
                                }
                                else
                                {
                                    ImagePanel steinImage = new ImagePanel(stein_weiss);
                                    steinImage.setLocation(offset_stein, offset_stein);
                                    backgroundField.add(steinImage);
                                }
                            }
                            else
                            {
                                if (stein.getIsDame() == true)
                                {
                                    ImagePanel steinImage = new ImagePanel(dame_schwarz);
                                    steinImage.setLocation(offset_stein, offset_stein);
                                    backgroundField.add(steinImage);
                                }
                                else
                                {
                                    ImagePanel steinImage = new ImagePanel(stein_schwarz);
                                    steinImage.setLocation(offset_stein, offset_stein);
                                    backgroundField.add(steinImage);
                                }
                            }

                            backgroundField.validate();
                            backgroundField.repaint();
                        }

                    }

                }
            }
        }
    }

    public void mouseExited(MouseEvent e)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int k = 0; k < 8; k++)
            {
                int grid_index = (i * 8) + k;

                if (this.checkerBoard.getComponent(grid_index) == e.getComponent())
                {
                    ImagePanel hoveredField = (ImagePanel) this.checkerBoard.getComponent(grid_index);
                    ImagePanel backgroundField = (ImagePanel) hoveredField.getComponent(0);

                    if (this.bewegtesPanel == hoveredField)
                    {
                        backgroundField.setImage(tile_blue);
                    }
                    else
                    {
                        backgroundField.setImage(tile_clear);
                    }

                    if (this.steinAngeklickt)
                    {
                        if (backgroundField.getComponentCount() > 0)
                        {
                            int arrayPos[] = this.getSpielfeldPosition(hoveredField);

                            Spielstein stein = this.spiel.getSpielbrett().getBrett()[arrayPos[0]][arrayPos[1]];

                            if (stein == null)
                            {
                                backgroundField.remove(0);
                            }
                        }

                    }

                    backgroundField.validate();
                    backgroundField.repaint();
                }

            }
        }
    }

    public void mousePressed(MouseEvent e)
    {
        if (!this.spielEnde)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int k = 0; k < 8; k++)
                {
                    int grid_index = (i * 8) + k;

                    if (this.checkerBoard.getComponent(grid_index) == e.getComponent())
                    {
                        ImagePanel panelAngeklickt = (ImagePanel) this.checkerBoard.getComponent(grid_index);

                        int arrayPos[] = this.getSpielfeldPosition(panelAngeklickt);
                        Spielstein stein = this.spiel.getSpielbrett().getBrett()[arrayPos[0]][arrayPos[1]];

                        // Angeklicktes Feld ist ein Stein auf dem Spielfeld..
                        if (stein != null)
                        {
                            boolean canMoveStein = false;

                            if (!this.isNetworkGame)
                            {
                                canMoveStein = (stein.getColor() == this.spiel.getCurrentSpieler().getColor());
                            }
                            else
                            {
                                canMoveStein = ((stein.getColor() == this.spiel.getCurrentSpieler().getColor()) && (this.spiel.getCurrentSpieler() == this.spiel.getNetworkPlayerSelf()));
                            }

                            // Angeklickter Stein gehört zum aktuellen Spieler
                            if (canMoveStein)
                            {
                                // Momentan kein Stein ausgewählt..
                                if (this.bewegtesPanel == null)
                                {
                                    this.bewegtesPanel = panelAngeklickt;
                                    this.steinAngeklickt = true;
                                    ImagePanel backgroundPanel = (ImagePanel) this.bewegtesPanel.getComponent(0);
                                    backgroundPanel.setImage(tile_blue);
                                    backgroundPanel.validate();
                                    backgroundPanel.repaint();

                                    this.spiel.resetSteinWahl();
                                    stein.setAusgewaehlt(true);
                                }
                                else // Es ist bereits ein Stein ausgewählt
                                {
                                    ImagePanel backgroundPanel = (ImagePanel) this.bewegtesPanel.getComponent(0);
                                    backgroundPanel.setImage(tile_clear);
                                    backgroundPanel.validate();
                                    backgroundPanel.repaint();

                                    this.bewegtesPanel = panelAngeklickt;
                                    this.steinAngeklickt = true;

                                    backgroundPanel = (ImagePanel) this.bewegtesPanel.getComponent(0);
                                    backgroundPanel.setImage(tile_blue);
                                    backgroundPanel.validate();
                                    backgroundPanel.repaint();

                                    this.spiel.resetSteinWahl();
                                    stein.setAusgewaehlt(true);
                                }
                            }
                            else // Stein gehört nicht dem Spieler..
                            {
                                // Aktuell ist ein Stein ausgewählt
                                if (this.bewegtesPanel != null)
                                {
                                    this.steinAngeklickt = false;
                                    ImagePanel backgroundPanel = (ImagePanel) this.bewegtesPanel.getComponent(0);
                                    backgroundPanel.setImage(tile_clear);
                                    backgroundPanel.validate();
                                    backgroundPanel.repaint();
                                    this.bewegtesPanel = null;

                                    this.spiel.resetSteinWahl();
                                }
                            }
                        }
                        else
                        {
                            // An der Position ist kein Stein, aber es wurde einer ausgewählt..
                            if (this.steinAngeklickt == true)
                            {
                                int posSteinGewaehlt[] = this.getSpielfeldPosition(this.bewegtesPanel);

                                Spielstein steinGewaehlt = this.spiel.getSpielbrett().getBrett()[posSteinGewaehlt[0]][posSteinGewaehlt[1]];

                                ArrayList<int[]> possibleMoves = steinGewaehlt.getPossibleMoves();
                                boolean validMove = false;

                                for (int[] pos : possibleMoves)
                                {
                                    if ((pos[0] == arrayPos[0]) && (pos[1] == arrayPos[1]))
                                    {
                                        validMove = true;
                                    }
                                }

                                // Gültiger Zug..
                                if (validMove)
                                {
                                    boolean geschlagen = steinGewaehlt.moveTo(arrayPos[0], arrayPos[1]);

                                    if (this.isNetworkGame)
                                    {
                                        this.netzwerkLobby.getNetworkClient().sendMessageToServer("MOVE|" + posSteinGewaehlt[0] + "|" + posSteinGewaehlt[1] + "|" + arrayPos[0] + "|" + arrayPos[1]);
                                    }

                                    if (geschlagen)
                                    {
                                        ArrayList<int[]> possible_schlag_moves = steinGewaehlt.getPossibleSchlagMoves();
                                        // Wenn geschlagen werden kann.
                                        if (possible_schlag_moves.size() > 0)
                                        {
                                            this.bewegtesPanel = panelAngeklickt;
                                            this.spiel.resetSteinWahl();
                                            steinGewaehlt.setAusgewaehlt(true);
                                        }
                                        else
                                        {
                                            this.steinAngeklickt = false;
                                            this.bewegtesPanel = null;
                                        }

                                    }
                                    else
                                    {
                                        this.steinAngeklickt = false;
                                        this.bewegtesPanel = null;
                                    }

                                    if (this.steinAngeklickt == false)
                                    {
                                        if ((this.spiel.getSpieler1().countSteineGesamt() > 0) && (this.spiel.getSpieler2().countSteineGesamt() > 0))
                                        {

                                            Spieler nextPlayer = null;

                                            if (this.spiel.getCurrentSpieler() == this.spiel.getSpieler1())
                                            {
                                                nextPlayer = this.spiel.getSpieler2();
                                            }
                                            else
                                            {
                                                nextPlayer = this.spiel.getSpieler1();
                                            }

                                            if (nextPlayer.canMove() == true)
                                            {
                                                this.spiel.setCurrentSpieler(nextPlayer);

                                                this.spiel.resetSteinWahl();

                                                if (this.isNetworkGame)
                                                {
                                                    this.netzwerkLobby.getNetworkClient().sendMessageToServer("END_TURN");
                                                }
                                            }
                                            else
                                            {
                                                if (this.isNetworkGame)
                                                {
                                                    this.netzwerkLobby.getNetworkClient().sendMessageToServer("GAME_END");
                                                }
                                                else
                                                {
                                                    this.setzeSteine();
                                                    this.spielEnde();
                                                }
                                            }
                                        }
                                        else
                                        {
                                            // Ende

                                            if (this.isNetworkGame)
                                            {
                                                this.netzwerkLobby.getNetworkClient().sendMessageToServer("GAME_END");
                                            }
                                            else
                                            {
                                                this.setzeSteine();
                                                this.spielEnde();
                                            }
                                        }
                                    }

                                    this.setzeSteine();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void actionPerformed (ActionEvent ae)
    {
        if(ae.getSource() == this.newGameButton)
        {
            this.steinAngeklickt = false;
            this.bewegtesPanel = null;
            this.spielEnde = false;
            this.spiel.startGame();
        }
        else if (ae.getSource() == this.exitButton)
        {
            if (this.netzwerkLobby instanceof NetzwerkLobby)
            {
                if (this.netzwerkLobby.getNetworkClient() instanceof DameClient)
                {
                    this.netzwerkLobby.getNetworkClient().sendMessageToServer("DISCONNECT");
                }
            }

            System.exit(0);
        }
        else if (ae.getSource() == this.giveUpButton)
        {
            if (this.netzwerkLobby instanceof NetzwerkLobby)
            {
                if (this.netzwerkLobby.getNetworkClient() instanceof DameClient)
                {
                    this.netzwerkLobby.getNetworkClient().sendMessageToServer("GIVE_UP");
                }
            }

            this.spielEnde = true;
            this.zugStatusLabel.setText("Spiel abgebrochen!");

            this.giveUpButton.setVisible(false);
            this.newGameButton.setVisible(true);

            this.netzwerkLobby.getMainWindow().setVisible(true);

        }
        else if (ae.getSource() == this.menuItemNeuesSpiel)
        {
            this.steinAngeklickt = false;
            this.bewegtesPanel = null;
            this.spielEnde = false;
            this.spiel.startGame();
        }
        else if (ae.getSource() == this.menuItemExit)
        {
            if (this.netzwerkLobby instanceof NetzwerkLobby)
            {
                if (this.netzwerkLobby.getNetworkClient() instanceof DameClient)
                {
                    this.netzwerkLobby.getNetworkClient().sendMessageToServer("DISCONNECT");
                }
            }

            System.exit(0);
        }
        else if (ae.getSource() == this.menuItemNetworkSpiel)
        {
            this.netzwerkLobby = new NetzwerkLobby(this);
        }
    }

}