import java.awt.*;
import javax.swing.*;

class ImagePanel extends JPanel {

    private Image img;
    private Spielstein stein;

    public ImagePanel(String img)
    {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img)
    {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }
    
    public void setImage(Image img)
    {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setOpaque(false);
    }
    
    public void setSpielstein(Spielstein stein)
    {
        this.stein = stein;
    }
    
    public Spielstein getSpielstein()
    {
        return this.stein;
    }

    public void paintComponent(Graphics g)
    {
        //super.paintComponent(g);
        g.drawImage(img, 0, 0, new Color(0, 0, 0, 0), null);
    }

}
