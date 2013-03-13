package hobogame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Gershom
 */
public class HoboExplosion extends HoboAnimation {

    private int size;

    public HoboExplosion(World world, int type, int length) {
        super(world, length, type);
        size = 300;
    }

    public void render(Graphics2D g2d, int frame) {
        if (frame < size) {
            int w = (int) (Math.log(frame * 0.08f) * 100 + (frame * 0.15f));
            g2d.setColor(new Color(255, 60, 30, 30));
            for (int i = w; i > 25; i -= 25) {
                g2d.fillOval(loc.getX() - (i >> 1), loc.getY() - (i >> 1), i, i);
            }
        } else {
            int w = (int) (Math.log(size * 0.03f) * 100 + (size * 0.5f));
            float ratio = (float) (frame - size) / (maxFrames() - size);
            g2d.setColor(new Color(255, 60 + (int) (ratio * 195),
                    30 + (int) (ratio * 225), 10 - (int) (ratio * 10)));
            for (int i = w; i > 25; i -= 25) {
                g2d.fillOval(loc.getX() - (i >> 1), loc.getY() - (i >> 1), i, i);
            }
        }
    }

}
