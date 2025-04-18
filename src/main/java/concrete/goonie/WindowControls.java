package concrete.goonie;

import javax.swing.*;
import java.awt.Button;
import java.awt.Frame;
import java.awt.*;

import static concrete.goonie.Button.createButton;

public class WindowControls {

    private JButton closeButton;
    private JButton minButton;
    private JButton maxButton;
    private JFrame frame;

    public WindowControls(JFrame frame) {
        this.frame = frame;

        closeButton = createButton("X", Color.RED);
        minButton = createButton("_", new Color(80, 80, 80));
        maxButton = createButton("▢", new Color(80, 80, 80));

        closeButton.addActionListener(e -> frame.dispose());
        minButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        maxButton.addActionListener(e -> toggleMaximize());
    }

    private void toggleMaximize() {
        if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
            WindowDragSupport.restoreFromFullscreen(frame, null);
            frame.setExtendedState(Frame.NORMAL);
        } else {
            WindowDragSupport.maximizeToFullscreen(frame);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public JButton getMinButton() {
        return minButton;
    }

    public JButton getMaxButton() {
        return maxButton;
    }
}
