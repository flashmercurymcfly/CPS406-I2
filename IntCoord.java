package hobogame;

import java.awt.Point;

final class IntCoord implements Discreet2D {

    public static final IntCoord ORIGIN = new IntCoord(0, 0);

    final int x;
    final int y;

    public IntCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntCoord(Point p) {
        this(p.x, p.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IntCoord add(int x, int y) {
        return new IntCoord(this.x + x, this.y + y);
    }

    public IntCoord add(Discreet2D d) {
        return add(d.getX(), d.getY());
    }

    public Coord add(float x, float y) {
        return new Coord(this.x + x, this.y + y);
    }

    public Coord toCoord() {
        return new Coord(x, y);
    }
}
