package concrete.goonie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class WindowDragSupport {

    private static Point dragStart = null;
    private static Rectangle dragStartBounds = null;
    private static int dragEdge = 0;

    // Add new variable to track fullscreen state
    private static boolean isFullscreen = false;
    private static Dimension previousSize = null;
    private static Point previousLocation = null;

    public static void addDragSupport(JFrame frame, JPanel titleBar) {
        final int BORDER = 6;
        final int MIN_WIDTH = 300;
        final int MIN_HEIGHT = 200;

        addWindowMouseListener(frame, BORDER);

        addWindowMouseMotionListener(frame, BORDER, MIN_WIDTH, MIN_HEIGHT);

        addTitleBarMouseListener(frame, titleBar, BORDER);

        addTitleBarMouseMotionListener(frame, titleBar, BORDER, MIN_WIDTH, MIN_HEIGHT);
    }

    private static void addTitleBarMouseMotionListener(JFrame frame, JPanel titleBar, int BORDER, int MIN_WIDTH, int MIN_HEIGHT) {
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int edge = 0;
                if (e.getY() < BORDER) edge |= 4;
                if (e.getX() < BORDER) edge |= 1;
                if (e.getX() > frame.getWidth() - BORDER) edge |= 2;

                frame.setCursor(getCursorForEdge(edge));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point current = e.getLocationOnScreen();

                if (dragStart != null && isFullscreen) {
                    restoreFromFullscreen(frame, current);
                }

                if (dragStart != null && dragEdge == -1) {

                    frame.setLocation(
                            current.x - dragStart.x + dragStartBounds.x,
                            current.y - dragStart.y + dragStartBounds.y
                    );
                } else if (dragStart != null && dragEdge != 0) {
                    // Resize from title bar if dragging from top or corners

                    Rectangle newBounds = new Rectangle(dragStartBounds);

                    if ((dragEdge & 1) != 0) {
                        int newWidth = dragStartBounds.width + dragStartBounds.x - current.x;
                        if (newWidth >= MIN_WIDTH) {
                            newBounds.x = current.x;
                            newBounds.width = newWidth;
                        }
                    }
                    if ((dragEdge & 2) != 0) {
                        int newWidth = current.x - dragStartBounds.x;
                        if (newWidth >= MIN_WIDTH) {
                            newBounds.width = newWidth;
                        }
                    }
                    if ((dragEdge & 4) != 0) {
                        int newHeight = dragStartBounds.height + dragStartBounds.y - current.y;
                        if (newHeight >= MIN_HEIGHT) {
                            newBounds.y = current.y;
                            newBounds.height = newHeight;
                        }
                    }

                    frame.setBounds(newBounds);
                }
            }
        });
    }

    private static void addTitleBarMouseListener(JFrame frame, JPanel titleBar, int BORDER) {
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    toggleFullscreen(frame);
                    return;
                }

                int edge = 0;
                if (e.getY() < BORDER) edge |= 4;
                if (e.getX() < BORDER) edge |= 1;
                if (e.getX() > frame.getWidth() - BORDER) edge |= 2;

                if (edge != 0) {
                    dragEdge = edge;
                } else {
                    dragEdge = -1;
                }

                dragStart = e.getLocationOnScreen();
                dragStartBounds = frame.getBounds();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                dragStartBounds = null;
                dragEdge = 0;
                frame.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    static void toggleFullscreen(JFrame frame) {
        if (isFullscreen) {
            restoreFromFullscreen(frame, null);
        } else {
            maximizeToFullscreen(frame);
        }
    }

    static void maximizeToFullscreen(JFrame frame) {
        previousSize = frame.getSize();
        previousLocation = frame.getLocation();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        frame.setBounds(bounds);
        isFullscreen = true;
    }

    static void restoreFromFullscreen(JFrame frame, Point currentMouseLocation) {
        if (previousSize != null && previousLocation != null) {
            isFullscreen = false;
            frame.setSize(previousSize);

            if (currentMouseLocation != null && dragStart != null) {
                // Get the mouse position relative to the window before fullscreen
                Point relativeMousePos = new Point(
                        dragStart.x - dragStartBounds.x,
                        dragStart.y - dragStartBounds.y
                );

                // Set new position so mouse maintains same relative position
                int newX = currentMouseLocation.x - relativeMousePos.x;
                int newY = currentMouseLocation.y - relativeMousePos.y;

                // Ensure window stays on screen
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Rectangle screenBounds = env.getMaximumWindowBounds();
                newX = Math.max(screenBounds.x, Math.min(newX, screenBounds.x + screenBounds.width - frame.getWidth()));
                newY = Math.max(screenBounds.y, Math.min(newY, screenBounds.y + screenBounds.height - frame.getHeight()));

                frame.setLocation(newX, newY);
            } else {
                frame.setLocation(previousLocation);
            }
        }
    }

    private static void checkAndSnapToFullscreen(JFrame frame, JPanel titleBar) {
        Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Point titleBarLocation = titleBar.getLocationOnScreen();
        int visibleWidth = Math.min(screenBounds.x + screenBounds.width, titleBarLocation.x + titleBar.getWidth()) - Math.max(screenBounds.x, titleBarLocation.x);

        if (visibleWidth < titleBar.getWidth() / 2) {
            maximizeToFullscreen(frame);
        }
    }


    private static void addWindowMouseMotionListener(JFrame frame, int BORDER, int MIN_WIDTH, int MIN_HEIGHT) {
        frame.getRootPane().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                frame.setCursor(getCursorForPosition(frame, e.getPoint(), BORDER));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null || dragEdge == 0) return;

                Point current = e.getLocationOnScreen();
                Rectangle newBounds = new Rectangle(dragStartBounds);

                if ((dragEdge & 1) != 0) {
                    int newWidth = dragStartBounds.width + dragStartBounds.x - current.x;
                    if (newWidth >= MIN_WIDTH) {
                        newBounds.x = current.x;
                        newBounds.width = newWidth;
                    }
                }
                if ((dragEdge & 2) != 0) {
                    int newWidth = current.x - dragStartBounds.x;
                    if (newWidth >= MIN_WIDTH) {
                        newBounds.width = newWidth;
                    }
                }
                if ((dragEdge & 4) != 0) {
                    int newHeight = dragStartBounds.height + dragStartBounds.y - current.y;
                    if (newHeight >= MIN_HEIGHT) {
                        newBounds.y = current.y;
                        newBounds.height = newHeight;
                    }
                }
                if ((dragEdge & 8) != 0) {
                    int newHeight = current.y - dragStartBounds.y;
                    if (newHeight >= MIN_HEIGHT) {
                        newBounds.height = newHeight;
                    }
                }

                frame.setBounds(newBounds);
            }
        });
    }

    private static void addWindowMouseListener(JFrame frame, int BORDER) {
        frame.getRootPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                dragStartBounds = frame.getBounds();
                dragEdge = getEdge(frame, dragStart, BORDER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                dragStartBounds = null;
                dragEdge = 0;
                frame.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private static int getEdge(JFrame frame, Point p, int border) {
        int edge = 0;
        if (p.x < border) edge |= 1; // Left
        if (p.x > frame.getWidth() - border) edge |= 2; // Right
        if (p.y < border) edge |= 4; // Top
        if (p.y > frame.getHeight() - border) edge |= 8; // Bottom
        return edge;
    }

    private static Cursor getCursorForPosition(JFrame frame, Point p, int border) {
        boolean left = p.x < border;
        boolean right = p.x > frame.getWidth() - border;
        boolean top = p.y < border;
        boolean bottom = p.y > frame.getHeight() - border;

        if (left && top) return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
        if (right && top) return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        if (left && bottom) return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        if (right && bottom) return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        if (left) return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        if (right) return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        if (top) return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        if (bottom) return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

        return Cursor.getDefaultCursor();
    }

    private static Cursor getCursorForEdge(int edge) {
        return switch (edge) {
            case 5 -> Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR); // Top-left
            case 6 -> Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR); // Top-right
            case 4 -> Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);  // Top
            default -> Cursor.getDefaultCursor();
        };
    }

    public static boolean isFullscreen() {
        return isFullscreen;
    }


}
