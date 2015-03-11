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
        String isPlaying = object_splitted[3];
        String username = object_splitted[1];
        
        
        this.setText(username);
        this.setForeground(color);
        this.setBackground(Color.white);

        return this;
    }
}