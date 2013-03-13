package hobogame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gershom
 */
public class HoboTrack extends HoboEntity {

    private final List<IntCoord> trackPoints;
    private TrackSystem trackSystem;

    public HoboTrack(World world) {
        this(world, null);
    }

    public HoboTrack(World world, TrackSystem trackSystem) {
        super(world);
        setSystem(trackSystem);

        trackPoints = new ArrayList();
    }

    public void update() {
    }

    public void worldRender(Graphics2D g2d) {
        g2d.setColor(getColor());

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            IntCoord c = trackPoints.get(i);
            IntCoord c2 = trackPoints.get(i + 1);
            g2d.drawLine(c.x, c.y, c2.x, c2.y);
        }
    }

    public void addTrack(IntCoord c) {
        trackPoints.add(c);
    }

    public void setSystem(TrackSystem trackSystem) {
        if (trackSystem == null) {
            trackSystem = new TrackSystem();
        }
        if (this.trackSystem != null) {
            this.trackSystem.tracks.remove(this);
        }
        this.trackSystem = trackSystem;
        this.trackSystem.tracks.add(this);
    }

    public TrackSystem getSystem() {
        return trackSystem;
    }

    /**
     * Returns a set of points that run along the track however they are spaced
     * at intervals defined by dist. If an entity can move along
     * the track at a speed of 5 px/frame, it can call getPoints(5) to obtain a
     * list of points and then each frame simple move to the next point.
     *
     * @param dist
     * @return
     */
    public Track getPoints(float dist) {
        float distSqr = dist * dist;
        final List<Discreet2D> points = new ArrayList();
        final List<Float> angles = new ArrayList();

        Coord cur = trackPoints.get(0).toCoord();
        points.add(cur);

        Coord n = trackPoints.get(1).toCoord();
        int next = 1;
        
        float ang = cur.angle(n);
        angles.add(ang);

        while (true) {
            float dd = cur.distSqr(n);
            cur = cur.trans(ang, dist);
            points.add(cur);
            angles.add(ang);

            if (dd < distSqr) {
                if (++next == trackPoints.size()) {
                    break;
                }
                n = trackPoints.get(next).toCoord();
                ang = cur.angle(n);
            }
        }

        return new Track(){
            public Discreet2D getPoint(int num) {
                return points.get(num);
            }

            public float getAngle(int num) {
                return angles.get(num);
            }

            public int length() {
                return points.size();
            }
        };
    }

    public class TrackSystem {
        private final List<HoboTrack> tracks;

        private TrackSystem() {
            tracks = new ArrayList();
        }

        public HoboTrack randomTrack() {
            return Util.randInList(tracks);
        }
    }

    public static void generate(HoboTrack track, Coord start, int length,
            float angle) {

        float dAngle = 0;

        float segs = length / 10;

        for (int i = 0; i < segs; i++) {
            track.addTrack(start.toIntCoord());
            start = start.trans(angle, 10);
            dAngle += Util.cenRandFloat((float) (Math.PI * 0.03f));
            angle += Util.cenRandFloat((float) (Math.PI * 0.13f)) +
                    dAngle * 0.05f;
        }
    }
}