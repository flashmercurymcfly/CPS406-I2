package hobogame;

import java.awt.Graphics2D;

/**
 * @author Gershom
 */
public class HoboImage extends HoboGraphic {

    private boolean complete;

    public HoboImage(World world, int type) {
        super(world, type);
    }

    public void worldRender(Graphics2D g2d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void screenRender(Graphics2D g2d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isComplete() {
        return complete;
    }

}
