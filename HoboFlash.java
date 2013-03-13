package hobogame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Gershom
 */
public class HoboFlash extends HoboAnimation {

    private int w;
    private int h;
    private int peak;
    private Color col;

    public HoboFlash(World world, int type, int length) {
        super(world, length, type);
        peak = 10;
    }

    public void render(Graphics2D g2d, int frame) {
        g2d.setColor(genCol(frame));
        g2d.fillRect(loc.getX() - (w >> 1), loc.getY() - (h >> 1), w, h);
    }

    private Color genCol(int frame) {
        float rat;
        if (frame < peak) {
            rat = ((float) frame * frame) / (peak * peak);
        } else {
            rat = 1 - ((float) (frame - peak) / (maxFrames() - peak));
        }
        return new Color(col.getRed(), col.getGreen(), col.getBlue(),
                (int) Math.ceil(255 * rat));
    }

    public static HoboFlash screenFlash(HoboWorld world, Color col) {
        HoboFlash ret = new HoboFlash(world, 300, HoboAnimation.GRAPHIC_SCREEN);
        ret.peak = 40;
        ret.w = world.width();
        ret.h = world.height();
        ret.loc = new IntCoord(ret.w >> 1, ret.h >> 1);
        ret.col = col;
        return ret;
    }
}
