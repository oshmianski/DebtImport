package by.oshmianski.docks.Setup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 17.12.12
 * Time: 10:48
 */
public class StatusBar extends JPanel {
    private JPanel heapPanel;
    private JLabel lHeap;
    private Font labelHeapFont = new Font("Tahoma", Font.PLAIN, 11);

    private static final boolean showMaximumMemory = true;

    private static final long KB = 1024;
    private static final long MB = 1024 * KB;
    private static final long GB = 1024 * MB;
    private static final long PB = 1024 * GB;

    private long usedMemory = 0;
    private long allocatedMemory = 0;
    private long maxMemory = 0;

    private static final Color bgColor = new Color(0xD6D6D6);
    private static final Color bgColorUsed = new Color(0x7E8AA8);
    private static final Color bgColorAlloceated = new Color(0xBDBDBD);
    private static final Color borderColor = new Color(0x919191);

    private static final Color colorWhite = Color.WHITE;

    private Timer timer;

    public StatusBar() {
        super();

        setLayout(new GridBagLayout());

        lHeap = new JLabel();
        lHeap.setFont(labelHeapFont);
        lHeap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearHeap();
            }
        });

        heapPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                if (!isOpaque()) {
                    super.paintComponent(g);
                    return;
                }

                int w = getWidth();
                int h = getHeight();

                // Paint a gradient from top to bottom
                GradientPaint gpParent = new GradientPaint(
                        0, 0, colorWhite,
                        0, h, bgColor);

                g2d.setPaint(gpParent);
                g2d.fillRect(0, 0, w, h);

                //Paint a gradient from top to bottom
                GradientPaint gpAl = new GradientPaint(
                        0, 0, colorWhite,
                        0, h, bgColorAlloceated);

                g2d.setPaint(gpAl);
                g2d.fillRect(0, 0, (int) ((allocatedMemory * w) / (maxMemory == 0 ? 1 : maxMemory)), h);

                //Paint a gradient from top to bottom
                GradientPaint gpUs = new GradientPaint(
                        0, 0, colorWhite,
                        0, h, bgColorUsed);

                g2d.setPaint(gpUs);
                g2d.fillRect(0, 0, (int) ((usedMemory * w) / (maxMemory == 0 ? 1 : maxMemory)), h);

                setOpaque(false);
                super.paintComponent(g);
                setOpaque(true);
            }
        };

        heapPanel.add(lHeap, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        heapPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderColor));
        heapPanel.setOpaque(true);

        add(heapPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        heapPanel.setPreferredSize(new Dimension(100, 17));

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));
        setOpaque(true);

        timer = new Timer();
        timer.schedule(new RemindTask(this), 0, 2 * 1000);
    }

    private void setHeapData() {
        // Determining current memory usage state
        MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        usedMemory = mu.getUsed() / 1024 / 1204;
        allocatedMemory = mu.getCommitted() / 1024 / 1204;
        maxMemory = mu.getMax() / 1024 / 1204;

        long total = showMaximumMemory ? maxMemory : allocatedMemory;
        lHeap.setText(usedMemory + "М из " + total + "М");
        lHeap.setToolTipText("Использовано: " + usedMemory + "МБ, выделено: " + allocatedMemory + "МБ, максимально доступно: " + maxMemory + "МБ");

        heapPanel.repaint();
    }

    private static class RemindTask extends TimerTask {
        private StatusBar statusBar;

        private RemindTask(StatusBar statusBar) {
            this.statusBar = statusBar;
        }

        public void run() {
            statusBar.setHeapData();
//            timer.cancel(); //Terminate the timer thread
        }
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void clearHeap() {
        System.gc();
        setHeapData();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (!isOpaque()) {
            super.paintComponent(g);
            return;
        }

        int w = getWidth();
        int h = getHeight();

        // Paint a gradient from top to bottom
        GradientPaint gp = new GradientPaint(
                0, 0, colorWhite,
                0, h, bgColor);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);
    }
}
