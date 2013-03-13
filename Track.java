package hobogame;

/**
 * @author Gershom
 */

public interface Track {
    public Discreet2D getPoint(int num);

    public float getAngle(int num);

    public int length();
}
