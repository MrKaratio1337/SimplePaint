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

    public SimplePaint(){
        setTitle("Simple Paint");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(Color.BLACK);

        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(canvas, 0, 0, null);
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

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(SimplePaint::new);
    }
}
