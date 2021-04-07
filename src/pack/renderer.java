package pack;

import java.awt.Graphics;
import java.io.Serial;

import javax.swing.JPanel;

public class renderer extends JPanel
{
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Flappy_Bird.call.repaint(g);
    }
}