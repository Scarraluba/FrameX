package concrete.goonie;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame customFrame = new Frame("Goonie App");
            customFrame.setFrameColor(new Color(253, 75, 75));

            JPanel content = new JPanel();
            content.setBackground(Color.CYAN);
            content.add(new JLabel("Welcome to my custom window!"));
            //frame.setContent(content);
        });
    }
}
