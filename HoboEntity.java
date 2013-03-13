package hobogame;

import hobogame.Collisions.Collidable;
import hobogame.Collisions.MultiCollider;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.List;

abstract class HoboEntity implements Entity, Collidable {

    public static boolean SHOW_BOUNDING = false;
    public static boolean SHOW_COL_AXES = false;

    World world;
    Discreet2D loc;
    private float w;
    private float h;
    private float radHint;
    float rot;
    Color col;
    boolean dead;
    boolean collidable;

    public HoboEntity(World world) {
        setWorld(world);
        loc = Coord.ORIGIN;
        setSize(10, 10);
        col = Color.BLACK;
        dead = false;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public HoboWorld getWorld() {
        return (HoboWorld) world;
    }

    public void worldRender(Graphics2D g2d) {
        g2d.setColor(getColor());
        Graphics2D g2d2 = (Graphics2D) g2d.create();
        g2d2.translate(loc.getX(), loc.getY());

        g2d2.rotate(rot);
        g2d2.translate(-(int) w >> 1,  -(int) h >> 1);
        g2d2.fillRect(0, 0, (int) w, (int) h);
        g2d2.dispose();

        if (SHOW_BOUNDING) {
            Coord[] corners = Collisions.getCorners(this);
            g2d.setColor(Color.RED);
            for (int i = 0; i < corners.length; i++) {
                g2d.fillRect(corners[i].getX() - 1,
                        corners[i].getY() - 1, 2, 2);
            }
        }
        if (SHOW_COL_AXES) {
            g2d.setColor(Color.GREEN);
            Coord[] axes = Collisions.getAxesOfInterest(this);
            Discreet2D vec = getLoc().add(axes[0].mult(20));
            Discreet2D vec2 = getLoc().add(axes[1].mult(20));
            g2d.drawLine(getLoc().getX(), getLoc().getY(), vec.getX(), vec.getY());
            g2d.drawLine(getLoc().getX(), getLoc().getY(), vec2.getX(), vec2.getY());
        }
    }

    public void screenRender(Graphics2D g2d) {
    }

    public boolean isComplete() {
        return dead;
    }

    public void onComplete() {
    }

    public void setColor(Color col) {
        this.col = col;
    }

    public Color getColor() {
        return col;
    }

    public void setLoc(Discreet2D loc) {
        this.loc = loc;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public float getRadiusHint() {
        return radHint;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
        radHint = Coord.ORIGIN.dist(w * 0.5f, h * 0.5f);
    }

    public Discreet2D getLoc() {
        return loc;
    }

    public float getRot() {
        return rot;
    }
    
    public List<? extends Collidable> collidables() {
        return collidable ? Collections.singletonList(this) : null;
    }
}
