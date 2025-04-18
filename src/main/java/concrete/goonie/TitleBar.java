package concrete.goonie;

import javax.swing.*;
import java.awt.*;

public class TitleBar {

    private final JLabel iconLabel;
    private final JPanel panel;
    private final JLabel title;
    private JPanel controls;
    private Image scaled;
    private ImageIcon icon;
    private ImageIcon imageIcon;
    private JFrame frame;

    public TitleBar(JFrame frame) {
        this.frame = frame;

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(frame.getWidth(), 35));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Left & right padding

        // === LEFT SIDE (icon + title) ===
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
        leftSide.setOpaque(false);

        iconLabel = new JLabel();
        setIconImage("src/main/resources/java.png");

        title = new JLabel("Custom Frame");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        leftSide.add(iconLabel);
        leftSide.add(title);

        // === RIGHT SIDE (window controls) ===
        controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controls.setOpaque(false);

        WindowControls windowControls = new WindowControls(frame);
        controls.add(windowControls.getMinButton());
        controls.add(windowControls.getMaxButton());
        controls.add(windowControls.getCloseButton());

        // Add sides to main panel
        panel.add(leftSide, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);
    }

    void setBackground(Color color) {
        panel.setBackground(color);
    }

    public void setIconImage(String path, int width, int height) {
        icon = new ImageIcon(path);
        scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaled);
        iconLabel.setIcon(imageIcon);
        frame.setIconImage(icon.getImage());
    }

    public void setIconImage(String path) {
        setIconImage(path, 18, 18);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public JPanel getPanel() {
        return panel;
    }
}
