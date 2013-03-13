package hobogame;

import hobogame.HoboTrack.TrackSystem;
import java.awt.Color;

/**
 *
 * @author Gershom
 */
public class Hobogame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int w = 500;
        int h = 500;
        Coord mid = new Coord(w >> 1, h >> 1);

        HoboWorld world = new HoboWorld(w, h);

        TrackSystem sys = convergeAtMiddle(world, mid);

        Hobo hobo = new Hobo(world, sys);
        hobo.setLoc(new IntCoord(w >> 1, h >> 1));
        
        world.add(hobo);
        world.start();
    }

    private static TrackSystem convergeAtMiddle(HoboWorld world, Coord mid) {
        HoboTrack track1 = new HoboTrack(world);
        TrackSystem sys = track1.getSystem();
        track1.setColor(new Color(139, 69, 19));
        HoboTrack.generate(track1, new Coord(-10, 250), 1000, 0);
        world.add(track1);
        for (int i = 0; i < 200; i++) {
            HoboTrack track = new HoboTrack(world, sys);
            track.setColor(new Color(139, 69, 19));
            
            float ang = Util.angleStandard(Util.cenRandFloat(Util.RADIAN));
            Coord start = mid.add(Coord.vector(ang, 1000));
            HoboTrack.generate(track, start, Util.randInt(800, 1400),
                    Util.angleSum(ang, Util.HALF_RADIAN));

            world.add(track);
        }

        for (int i = 0; i < 20; i++) {
            HoboTrain train = new HoboTrain(world, sys, Util.randInt(5, 20));
            train.setSize(20, 8);
            train.setSpeed(1.0f);
            train.setGap(23);
            world.add(train);
        }

        return sys;
    }

    private static TrackSystem trainTest(HoboWorld world, Coord mid) {
        HoboTrack track = new HoboTrack(world);

        HoboTrack.generate(track, mid.add(15, 0), 200, 0);

        world.add(track);

        HoboTrain train = new HoboTrain(world, track.getSystem(), 1);
        train.setSize(4, 20);
        train.setSpeed(0.1f);
        world.add(train);

        return track.getSystem();
    }
}
