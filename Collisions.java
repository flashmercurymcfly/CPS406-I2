package hobogame;

import java.util.List;

/**
 *
 * @author Gershom
 */
public final class Collisions {

    private Collisions() {
    }

    public static interface Collidable {

        public float getWidth();

        public float getHeight();

        public Discreet2D getLoc();

        public float getRot();

        public float getRadiusHint();
    }

    public static interface MultiCollider {

        public List<? extends Collidable> collidables();
    }

    public static Discreet2D closestPoint(Collidable focus, Collidable c) {
        return Coord.ORIGIN;
    }

    private static Projection projOn(Collidable c, Coord vec) {
        Coord[] points = getCorners(c);
        float min = vec.dot(points[0]);
        float max = min;
        for (int i = 1; i < points.length; i++) {
            float len = vec.dot(points[i]);
            if (len < min) {
                min = len;
            } else if (len > max) {
                max = len;
            }
        }
        return new Projection(min, max);
    }

    private static class Projection {

        private final float min;
        private final float max;

        private Projection(float min, float max) {
            this.min = min;
            this.max = max;
        }

        private float length() {
            return max - min;
        }

        private boolean intersects(Projection o) {
            return (min > o.min && min < o.max)
                    || (max < o.max && max > o.min)
                    || (o.min > min && o.min < max)
                    || (o.max < max && o.max > min);
        }
    }

    public static Coord[] getCorners(Collidable c) {
        float hw = c.getWidth() * 0.5f;
        float hh = c.getHeight() * 0.5f;
        Coord loc = c.getLoc().toCoord();
        float cos = Util.cos(c.getRot());
        float sin = Util.sin(c.getRot());
        Coord[] rett = {
            loc.add(hw * cos - hh * sin, hw * sin + hh * cos),
            loc.add(hw * cos + hh * sin, hw * sin - hh * cos),
            loc.add(-hw * cos + hh * sin, -hw * sin - hh * cos),
            loc.add(-hw * cos - hh * sin, -hw * sin + hh * cos),
        };
        return rett;
    }

    public static Coord[] getAxesOfInterest(Collidable... c) {
        Coord[] ret = new Coord[c.length * 2];
        for (int i = 0; i < c.length; i++) {
            //Two trigonometry calls is the least manageable I think.
            float cos = Util.cos(c[i].getRot());
            float sin = Util.sin(c[i].getRot());
            ret[i * 2] = new Coord(cos, sin);
            ret[i * 2 + 1] = new Coord(-sin, cos);
        }
        return ret;
    }

    /**
     * This is the tricky collision logic. We have two objects and for each of
     * them we know width height location and rotation. Do they penetrate????
     *
     * NOTE: We are assuming we are only dealing with rectangular objects.
     *
     * TODO: Add polygons and circles???
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean collideMono(Collidable c1, Collidable c2) {
        if (c1 == c2) {
            return false;
        }
        float rad = c1.getRadiusHint() + c2.getRadiusHint();
        float dist = c1.getLoc().toCoord().distSqr(c2.getLoc().toCoord());
        if (rad * rad < dist) {
            return false;
        }

        /*Coord[] axes = {
            Coord.vector(c1.getRot(), 1), Coord.vector(c2.getRot(), 1),
            Coord.vector(Util.angleSum(c1.getRot(), Util.QUAR_RADIAN), 1),
            Coord.vector(Util.angleSum(c2.getRot(), Util.QUAR_RADIAN), 1)
        };*/
        Coord[] axes = getAxesOfInterest(c1, c2);

        for (Coord c : axes) {
            if (!projOn(c1, c).intersects(projOn(c2, c))) {
                return false;
            }
        }
        return true;
    }

    public static boolean collideMulti(MultiCollider mc1, MultiCollider mc2) {
        List<? extends Collidable> coll1 = mc1.collidables();
        List<? extends Collidable> coll2 = mc2.collidables();
        if (coll1 == null || coll2 == null || coll1.isEmpty() || coll2.isEmpty()) {
            return false;
        }
        for (Collidable c : coll1) {
            for (Collidable c2 : coll2) {
                if (Collisions.collideMono(c, c2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean collideSemi(MultiCollider mc1, Collidable col) {
        List<? extends Collidable> colliders = mc1.collidables();
        if (colliders == null || colliders.isEmpty()) {
            return false;
        }
        for (Collidable c : colliders) {
            if (Collisions.collideMono(c, col)) {
                return true;
            }
        }
        return false;
    }

    public static class Collision {
        public final float dist;

        public Collision(float dist) {
            this.dist = dist;
        }
    }
}
