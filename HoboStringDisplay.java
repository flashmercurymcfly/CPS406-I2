package hobogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * @author Gershom
 */
public class HoboStringDisplay extends HoboAnimation {

    private Font font;
    private Color col;
    private final String string;

    public HoboStringDisplay(World world, int type, int length, String string) {
        super(world, length, type);
        this.string = string;
        font = new Font("default", 0, 20);
        col = Color.BLACK;
    }

    public void render(Graphics2D g2d, int frame) {

        g2d.setFont(font);
        g2d.setColor(col);
        g2d.drawString(string, loc.getX(), loc.getY());
    }
}
