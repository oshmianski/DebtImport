package by.oshmianski.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory method for a handful of icons.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class Icons {

    /**
     * An 'x' icon.
     */
    public static Icon x(int size, int fraction, Color foreground) {
        return new X(size, fraction, foreground);
    }

    /**
     * A '+' icon.
     */
    public static Icon plus(int size, int width, Color foreground) {
        return new Plus(size, width, foreground);
    }

    /**
     * A '+' icon.
     */
    public static Icon minus(int size, int width, Color foreground) {
        return new Minus(size, width, foreground);
    }

    /**
     * A triangle icon.
     */
    public static Icon triangle(int size, int direction, Color foreground) {
        return new Triangle(size, direction, foreground);
    }

    public static Icon roundArrow(int size, Color color) {
        return new RoundArrow(size, color);
    }

    /**
     * all icons are drawn in high quality
     */
    public static final Map<RenderingHints.Key, Object> RENDERING_HINTS = new HashMap<RenderingHints.Key, Object>();

    static {
        RENDERING_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private static final class RoundArrow implements Icon {
        private final int size;
        private final Color color;

        public RoundArrow(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int SIZE = 256;
            int a = SIZE / 2;
            int b = a;
            int r = 4 * SIZE / 5;
            int n = 9;

            Shape circle = new Ellipse2D.Float(0F, 0F, 350F, 350F);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDERING_HINTS);
            g2d.setColor(color);

            a = 500 / 2;
            b = 500 / 2;
            int m = Math.min(a, b);
            r = 4 * m / 5;
            int r2 = Math.abs(m - r) / 2;
            g2d.drawOval(x, y, 12, 12);
//            g2d.setColor(Color.blue);
//            for (int i = 0; i < n; i++) {
//                double t = 2 * Math.PI * i / n;
//                int xA = (int) Math.round(a + r * Math.cos(t));
//                int yA = (int) Math.round(b + r * Math.sin(t));
//                g2d.fillOval(xA - r2, yA - r2, 2 * r2, 2 * r2);
//            }
//
//            int left = (size - 2) / 2;
//            int center = y + size / 2;
//
//            g2d.fillRect(x, center - 4, 2, 8);
//
//            g2d.fillRect(x + 1, center - 6, 2, 2);
//            g2d.fillRect(x + 1, center + 4, 2, 2);
//
//            g2d.fillRect(x + 2, center - 7, 4, 2);
//            g2d.fillRect(x + 2, center + 5, 4, 2);
//
//            g2d.fillRect(x + 3, center + 3, 4, 2);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Implement the 'X' icon.
     */
    private static final class X implements Icon {
        private final int size;
        private final int fraction;
        private final Color foreground;

        public X(int size, int fraction, Color foreground) {
            this.size = size;
            this.fraction = fraction;
            this.foreground = foreground;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDERING_HINTS);
            g2d.setColor(foreground);

            int oneSixth = size / fraction;
            int fiveSixths = size - oneSixth;
            g2d.fillPolygon(
                    new int[]{x + oneSixth, x + size, x + fiveSixths, x},
                    new int[]{y, y + fiveSixths, y + size, y + oneSixth},
                    4
            );
            g2d.fillPolygon(
                    new int[]{x, x + fiveSixths, x + size, x + oneSixth},
                    new int[]{y + fiveSixths, y, y + oneSixth, y + size},
                    4
            );
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Implement the '+' icon.
     */
    private static final class Plus implements Icon {
        private final int size;
        private final int width;
        private final Color foreground;

        public Plus(int size, int width, Color foreground) {
            this.size = size;
            this.width = width;
            this.foreground = foreground;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDERING_HINTS);
            g2d.setColor(foreground);
            int left = (size - width) / 2;
            g2d.fillRect(x + left, y, width, width + 2 * left);
            g2d.fillRect(x, y + left, width + 2 * left, width);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Implement the '-' icon.
     */
    private static final class Minus implements Icon {
        private final int size;
        private final int width;
        private final Color foreground;

        public Minus(int size, int width, Color foreground) {
            this.size = size;
            this.width = width;
            this.foreground = foreground;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDERING_HINTS);
            g2d.setColor(foreground);
            int left = (size - width) / 2;
//            g2d.fillRect(x + left, y, width, width + 2 * left);
            g2d.fillRect(x, y + left, width + 2 * left, width);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Implement the triangle icon.
     */
    private static final class Triangle implements Icon {
        private final int size;
        private final int direction;
        private final Color foreground;

        public Triangle(int size, int direction, Color foreground) {
            this.size = size;
            this.direction = direction;
            this.foreground = foreground;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDERING_HINTS);
            g2d.setColor(foreground);
            int center = size / 2;
            int[] xPoints;
            int[] yPoints;
            if (direction == SwingConstants.NORTH) {
                xPoints = new int[]{x, x + center, x + center * 2};
                yPoints = new int[]{y + center * 2, y, y + center * 2};
            } else if (direction == SwingConstants.SOUTH) {
                xPoints = new int[]{x, x + center, x + center * 2};
                yPoints = new int[]{y, y + center * 2, y};
            } else if (direction == SwingConstants.WEST) {
                xPoints = new int[]{x + center * 2, x, x + center * 2};
                yPoints = new int[]{y, y + center, y + center * 2};
            } else if (direction == SwingConstants.EAST) {
                xPoints = new int[]{x, x + center * 2, x, x};
                yPoints = new int[]{y, y + center, y + center * 2};
            } else {
                throw new IllegalStateException();
            }

            g2d.fillPolygon(xPoints, yPoints, 3);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }


    /**
     * test the icons
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TestRunnable());
    }

    private static class TestRunnable implements Runnable {
        public void run() {
            JPanel panel = new JPanel(new FlowLayout());
//            for (int size = 10; size <= 15; size += 1) {
//                for (int fraction = 10; fraction >= 5; fraction--) {
//                    JLabel label = new JLabel("" + size, x(size, fraction, Color.BLACK), JLabel.CENTER);
//                    label.setHorizontalTextPosition(JLabel.CENTER);
//                    label.setVerticalTextPosition(JLabel.BOTTOM);
//                    panel.add(label);
//                }
//            }
//
            for (int size = 10; size <= 15; size += 1) {
                for (int width = 1; width <= 5; width++) {
                    JLabel label = new JLabel("" + size, minus(size, width, Color.BLACK), JLabel.CENTER);
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    panel.add(label);
                }
            }
//
//            int[] directions = new int[]{SwingConstants.NORTH, SwingConstants.EAST, SwingConstants.SOUTH, SwingConstants.WEST};
//            for (int size = 10; size <= 15; size += 1) {
//                for (int d = 0; d < directions.length; d++) {
//                    JLabel label = new JLabel("" + size, triangle(size, directions[d], Color.BLACK), JLabel.CENTER);
//                    label.setHorizontalTextPosition(JLabel.CENTER);
//                    label.setVerticalTextPosition(JLabel.BOTTOM);
//                    panel.add(label);
//                }
//            }

            JFrame frame = new JFrame();
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    public static ImageIcon createDisabledCopy(ImageIcon imageIcon) {
        return new ImageIcon(createDisabledCopy(imageIcon.getImage()));
    }

    public static BufferedImage createDisabledCopy(Image img) {
        BufferedImage bi = createGrayscaleCopy(img);

        BufferedImage bi2 = createCompatibleImage(bi);
        Graphics2D g2d = bi2.createGraphics();
        setupAlphaComposite(g2d, 0.7f);
        g2d.drawImage(bi, 0, 0, null);
        g2d.dispose();

        return bi2;
    }

    /**
     * Creates grayscale image copy
     */

    public static ImageIcon createGrayscaleCopy(ImageIcon imageIcon) {
        return new ImageIcon(createGrayscaleCopy(imageIcon.getImage()));
    }

    public static BufferedImage createGrayscaleCopy(Image img) {
        return createGrayscaleCopy(getBufferedImage(img));
    }

    public static BufferedImage createGrayscaleCopy(BufferedImage img) {
        return applyGrayscaleFilter(img, null);
    }

    /**
     * Applies grayscale filter to image
     */

    private static ColorConvertOp grayscaleColorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public static BufferedImage applyGrayscaleFilter(Image src, Image dst) {
        return applyGrayscaleFilter(getBufferedImage(src), getBufferedImage(dst));
    }

    public static BufferedImage applyGrayscaleFilter(BufferedImage src, BufferedImage dst) {
        return grayscaleColorConvert.filter(src, dst);
    }

    /**
     * Retrieves BufferedImage from Image
     */

    public static BufferedImage getBufferedImage(URL url) {
        return getBufferedImage(new ImageIcon(url));
    }

    public static BufferedImage getBufferedImage(String iconSrc) {
        return getBufferedImage(new ImageIcon(iconSrc));
    }

    public static BufferedImage getBufferedImage(ImageIcon imageIcon) {
        return getBufferedImage(imageIcon.getImage());
    }

    public static BufferedImage getBufferedImage(Image image) {
        if (image == null || image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
            return null;
        } else if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        //        else if ( image instanceof ToolkitImage && ( ( ToolkitImage ) image ).getBufferedImage () != null )
        //        {
        //            return ( ( ToolkitImage ) image ).getBufferedImage ();
        //        }
        else {
            BufferedImage bi = createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);
            Graphics2D g2d = bi.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            return bi;
        }
    }

    /**
     * Creates a compatible image using given data
     */

    public static BufferedImage createCompatibleImage(int width, int height) {
        return getGraphicsConfiguration().createCompatibleImage(width, height);
    }

    public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        return getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
    }

    public static BufferedImage createCompatibleImage(BufferedImage image) {
        return createCompatibleImage(image, image.getWidth(), image.getHeight());
    }

    public static BufferedImage createCompatibleImage(BufferedImage image, int transparency) {
        return createCompatibleImage(image.getWidth(), image.getHeight(), transparency);
    }

    public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
        return createCompatibleImage(width, height, image.getTransparency());
    }

    /**
     * Returns default GraphicsConfiguration for main screen.
     *
     * @return mail scren GraphicsConfiguration
     */
    public static GraphicsConfiguration getGraphicsConfiguration() {
        return getGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    /**
     * Returns default GraphicsEnvironment.
     *
     * @return default GraphicsEnvironment
     */
    private static GraphicsEnvironment getGraphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    /**
     * Setting AlphaComposite by taking old AlphaComposite settings into account
     */

    public static Composite setupAlphaComposite(Graphics2D g2d, float alpha) {
        return setupAlphaComposite(g2d, alpha, true);
    }

    public static Composite setupAlphaComposite(Graphics2D g2d, float alpha, boolean shouldSetup) {
        return setupAlphaComposite(g2d, g2d.getComposite(), alpha, shouldSetup);
    }

    public static Composite setupAlphaComposite(Graphics2D g2d, Composite composeWith, float alpha) {
        return setupAlphaComposite(g2d, composeWith, alpha, true);
    }

    public static Composite setupAlphaComposite(Graphics2D g2d, Composite composeWith, float alpha, boolean shouldSetup) {
        Composite comp = g2d.getComposite();
        if (!shouldSetup) {
            return comp;
        }

        // Determining old composite alpha
        float currentComposite = 1f;
        if (composeWith != null && composeWith instanceof AlphaComposite) {
            currentComposite = ((AlphaComposite) composeWith).getAlpha();
        }

        // Creating new composite
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentComposite * alpha);
        g2d.setComposite(newComposite);

        return comp;
    }

    public static Image iconToImage(Icon icon) {
        try {
            if (icon instanceof ImageIcon) {
                return ((ImageIcon) icon).getImage();
            } else {
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                BufferedImage image = gc.createCompatibleImage(w, h);
                Graphics2D g = image.createGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();
                return image;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}