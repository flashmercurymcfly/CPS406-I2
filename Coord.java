package hobogame;

/**
 * @author Gershom
 */
public final class Coord implements Discreet2D {

    public static final Coord ORIGIN = new Coord(0, 0);
    public final float x;
    public final float y;

    public Coord(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Coord unitVector(float angle) {
        return new Coord(Util.cos(angle), Util.sin(angle));
    }

    public static Coord unitVector2(float angle) {
        return new Coord(-Util.sin(angle), Util.cos(angle));
    }

    public static Coord vector(float angle, float dist) {
        return ORIGIN.trans(angle, dist);
    }

    public static Coord fromRot(Coord c, float angle, float dist) {
        return c.trans(angle, dist);
    }

    /*Geometry Operations*/
    public float angle(Coord other) {
        if (x == other.x) {
            if (y == other.y) {
                return 0;
            } else if (y < other.y) {
                return Util.QUAR_RADIAN;
            } else {
                return -Util.QUAR_RADIAN;
            }
        }
        if (y == other.y) {
            if (x < other.x) {
                return 0;
            } else {
                return Util.HALF_RADIAN;
            }
        }
        return (float) Math.atan2(other.y - y, other.x - x);
    }

    public float angle(Discreet2D other) {
        return angle(other.toCoord());
    }

    public float distSqr() {
        return distSqr(ORIGIN);
    }

    public float dist() {
        return dist(ORIGIN);
    }

    public float distSqr(Coord other) {
        return distSqr(other.x, other.y);
    }

    public float distSqr(Discreet2D other) {
        return distSqr(other.getX(), other.getY());
    }

    public float distSqr(float x, float y) {
        float dx = this.x - x;
        float dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public float dist(Coord other) {
        return dist(other.x, other.y);
    }

    public float dist(float x, float y) {
        return (float) Math.sqrt(distSqr(x, y));
    }

    public Coord trans(float angle, float d) {
        return new Coord(x + Util.cos(angle) * d, y + Util.sin(angle) * d);
    }

    public Coord add(Discreet2D d) {
        return add(d.toCoord());
    }

    public Coord add(Coord c) {
        return add(c.x, c.y);
    }

    public Coord add(int x, int y) {
        return new Coord(this.x + x, this.y + y);
    }

    public Coord add(float x, float y) {
        return new Coord(this.x + x, this.y + y);
    }

    public Coord mult(float s) {
        return mult(s, s);
    }

    public Coord mult(float sx, float sy) {
        return new Coord(x * sx, y * sy);
    }

    /*Vector Operations*/
    public float dot(Coord o) {
        return (x * o.x) + (y * o.y);
    }

    public Coord projectOnto(Coord o) {
        float prodPerLen = dot(o) / o.distSqr();
        return new Coord(prodPerLen * o.x, prodPerLen * o.y);
    }

    public Coord normalize() {
        return setLength(1);
    }

    public Coord setLength(float length) {
        float invDist = length / dist();
        return new Coord(x * invDist, y * invDist);
    }

    /*Utility*/
    public int ix() {
        return (int) x;
    }

    public int iy() {
        return (int) y;
    }

    public int getX() {
        return ix();
    }

    public int getY() {
        return iy();
    }

    public IntCoord toIntCoord() {
        return new IntCoord(ix(), iy());
    }

    public Coord toCoord() {
        //Seems useless - toCoord() is needed to implement Discreet2D
        return this;
    }
}
