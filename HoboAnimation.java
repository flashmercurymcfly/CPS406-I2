package hobogame;

import java.awt.Graphics2D;

/**
 * @author Gershom
 */
public abstract class HoboAnimation extends HoboGraphic {
    
    private final int length;
    private int frameCount;
    private int startMillis;

    public HoboAnimation(World world, int type, int length) {
        super(world, type);
        this.length = length;
        startMillis = -1;
    }

    public final int maxFrames() {
        return length;
    }

    public void update() {
        if (startMillis == -1) {
            startMillis = (int) System.currentTimeMillis();
        }
        frameCount++;
    }

    public void setLoc(Discreet2D loc) {
        this.loc = loc;
    }

    public void worldRender(Graphics2D g2d) {
        if (type == GRAPHIC_WORLD) {
            Graphics2D gPass = (Graphics2D)g2d.create();
            render(gPass, frameCount);
            gPass.dispose();
        }
    }

    public void screenRender(Graphics2D g2d) {
        if (type == GRAPHIC_SCREEN) {
            Graphics2D gPass = (Graphics2D)g2d.create();
            render(gPass, frameCount);
            gPass.dispose();
        }
    }

    public abstract void render(Graphics2D g2d, int frame);

    public boolean isComplete() {
        return frameCount >= length;
    }

}
