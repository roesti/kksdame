import java.awt.*;
import javax.swing.*;

public class ClientCellRenderer extends JLabel implements ListCellRenderer<Object> {

    public ClientCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Assumes the stuff in the list has a pretty toString
        
        String object_string = value.toString();
        String object_splitted[] = object_string.split(";;;");
        Color color = Color.decode(object_splitted[2]);
        boolean isPlaying = Boolean.parseBoolean(object_splitted[3]);
        String username = object_splitted[1];
        
        
        if (!isSelected)
        {
            this.setForeground(color);
            this.setBackground(Color.decode("#464646"));
        }
        else
        {
            this.setForeground(Color.white);
            this.setBackground(Color.blue);
        }
        
        if (!isPlaying)
        {
            this.setText(username);
            Font newFont = new Font(this.getFont().getName(), Font.BOLD, this.getFont().getSize());
            this.setFont(newFont);
        }
        else
        {
            this.setText(username + " (spielt...)");
            Font newFont = new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize());
            this.setFont(newFont);
        }
        

        return this;
    }
}