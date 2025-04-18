package concrete.goonie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Frame {

    private final JFrame frame;
    private final TitleBar titleBar;
    private JPanel contentPanel;
    private final JPanel paddedWrapper;

    public Frame() {
        this("Untitled", new JPanel(), null);
    }
    public Frame(String title) {
        this(title, new JPanel(), null);

    }
    public Frame(String title, JPanel content) {
        this(title, content, null);
    }

    public Frame(String title, JPanel content, String iconPath) {
        frame = new JFrame(title);
        frame.setUndecorated(true);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleBar = new TitleBar(frame);
        titleBar.setTitle(title);
        if (iconPath != null) {
            titleBar.setIconImage(iconPath);
        }

        paddedWrapper = new JPanel(new BorderLayout());
        paddedWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
        paddedWrapper.setOpaque(false);

        contentPanel = content;
        paddedWrapper.add(contentPanel, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(titleBar.getPanel(), BorderLayout.NORTH);
        frame.add(paddedWrapper, BorderLayout.CENTER);

        WindowDragSupport.addDragSupport(frame, titleBar.getPanel());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
        frame.setTitle(title);
    }

    public void setIconImage(String path) {
        titleBar.setIconImage(path);
    }

    public void setContent(JPanel content) {
        paddedWrapper.remove(contentPanel);
        contentPanel = content;
        paddedWrapper.add(contentPanel, BorderLayout.CENTER);
        paddedWrapper.revalidate();
        paddedWrapper.repaint();
    }

    public void setPadding(int top, int left, int bottom, int right) {
        paddedWrapper.setBorder(new EmptyBorder(top, left, bottom, right));
    }

    public void setContentBackground(Color color) {
        contentPanel.setBackground(color);
    }

    public void setFrameColor(Color color) {
        paddedWrapper.setOpaque(true);
        paddedWrapper.setBackground(color);
        titleBar.setBackground(color);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getPaddedWrapper() {
        return paddedWrapper;
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }
}
