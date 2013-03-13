package hobogame;

import hobogame.Collisions.Collidable;
import java.awt.Graphics2D;
import java.util.List;

/**
 * @author Gershom
 */
public abstract class HoboGraphic implements Entity {

    public static final int GRAPHIC_WORLD = 0;
    public static final int GRAPHIC_SCREEN = 1;
    
    private World world;
    Discreet2D loc;
    final int type;

    public HoboGraphic(World world, int type) {
        setWorld(world);
        this.type = type;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void onComplete() {
    }

    public List<? extends Collidable> collidables() {
        return null;
    }

    public void update() {
    }
}
