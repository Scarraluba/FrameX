package concrete.goonie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button {

    public static JButton createButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(50, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setOpaque(true);
            }

            public void mouseExited(MouseEvent e) {
                button.setOpaque(false);
            }
        });

        return button;
    }
}
