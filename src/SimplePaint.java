import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class SimplePaint extends JFrame {

    private BufferedImage canvas;
    private Graphics2D g2d;
    private int prevX, prevY;
    private boolean dragging;
    private Color currentColor = Color.BLACK;
    private Color currentBackgroundColor = Color.WHITE;
    private boolean paintBucketSelected = false;

    public SimplePaint(){
        setTitle("Simple Paint");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearCanvas();

        JPanel drawPanel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(canvas, 0, 0, null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(canvas.getWidth(), canvas.getHeight());
            }
        };

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(paintBucketSelected){
                    int x = e.getX();
                    int y = e.getY();
                    Color targetColor = new Color(canvas.getRGB(x, y));
                    if(!targetColor.equals(currentColor)){
                        floodFill(x, y, targetColor, currentColor);
                        drawPanel.repaint();
                    }
                } else{
                    prevX = e.getX();
                    prevY = e.getY();
                    dragging = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(!paintBucketSelected && dragging){
                    int x = e.getX();
                    int y = e.getY();

                    g2d.setColor(currentColor);

                    g2d.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    drawPanel.repaint();
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
                if(!paintBucketSelected){
                    currentColor = color;
                    g2d.setColor(currentColor);
                }
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
                drawPanel.repaint();
            });

            backgroundColorPanel.add(backgroundColorButton);
        }

        JButton paintBucketButton = new JButton("Paint Bucket");

        paintBucketButton.addActionListener(e -> {
            paintBucketSelected = !paintBucketSelected;
            if(paintBucketSelected){
                drawPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else{
                drawPanel.setCursor(Cursor.getDefaultCursor());
            }
        });

        colorPanel.add(paintBucketButton);

        setLayout(new BorderLayout());
        add(backgroundColorPanel, BorderLayout.SOUTH);
        add(new JScrollPane(colorPanel), BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void clearCanvas(){
        g2d.setColor(currentBackgroundColor);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColor);
    }

    private void floodFill(int x, int y, Color targetColor, Color replacementColor){
        if(targetColor.equals(replacementColor)) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while(!queue.isEmpty()){
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if(px < 0 || px >= canvas.getWidth() || py < 0 || py >= canvas.getHeight()) continue;
            if(!new Color(canvas.getRGB(px, py)).equals(targetColor)) continue;

            canvas.setRGB(px, py, replacementColor.getRGB());

            queue.add(new Point(px + 1, py));
            queue.add(new Point(px - 1, py));
            queue.add(new Point(px, py + 1));
            queue.add(new Point(px, py - 1));
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(SimplePaint::new);
    }
}
