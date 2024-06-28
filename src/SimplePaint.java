import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class SimplePaint extends JFrame {

    private BufferedImage canvas;
    private Graphics2D g2d;
    private int prevX, prevY;
    private boolean dragging;
    private Color currentColor = Color.BLACK;
    private Color currentBackgroundColor = Color.WHITE;

    public SimplePaint(){
        setTitle("Simple Paint");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearCanvas();

        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(canvas, 0, 0, null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(canvas.getWidth(), canvas.getHeight());
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(dragging){
                    int x = e.getX();
                    int y = e.getY();

                    g2d.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    panel.repaint();
                }
            }
        });

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout());

        Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA};
        for(Color color : colors){
            JButton colorButton = new JButton();

            colorButton.setBackground(color);

            colorButton.setPreferredSize(new Dimension(30, 30));

            colorButton.addActionListener(e -> {
                currentColor = color;
                g2d.setColor(currentColor);
            });

            colorPanel.add(colorButton);
        }

        JPanel backgroundColorPanel = new JPanel();
        backgroundColorPanel.setLayout(new FlowLayout());

        for(Color color : colors){
            JButton backgroundColorButton = new JButton();
            backgroundColorButton.setBackground(color);

            backgroundColorButton.setPreferredSize(new Dimension(30, 30));

            backgroundColorButton.addActionListener(e -> {
                currentBackgroundColor = color;
                clearCanvas();
                panel.repaint();
            });

            backgroundColorPanel.add(backgroundColorButton);
        }

        setLayout(new BorderLayout());
        add(backgroundColorPanel, BorderLayout.SOUTH);
        add(new JScrollPane(colorPanel), BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void clearCanvas(){
        g2d.setColor(currentBackgroundColor);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColor);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(SimplePaint::new);
    }
}
