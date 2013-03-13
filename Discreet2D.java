package hobogame;

/**
 * @author Gershom
 */
public interface Discreet2D {
    public int getX();

    public int getY();

    public Discreet2D add(int x, int y);

    public Discreet2D add(Discreet2D d);

    public Coord toCoord();
}
