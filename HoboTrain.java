package hobogame;

import hobogame.Collisions.Collidable;
import hobogame.Collisions.MultiCollider;
import hobogame.HoboTrack.TrackSystem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gershom
 */
public class HoboTrain implements Entity, MultiCollider {

    private TrackSystem tracks;
    private Track track;
    private float speed;
    private int distance;
    private final List<TrainCar> cars;
    private int headIndex = 0;

    public HoboTrain(World world, TrackSystem tracks, int numCars) {
        cars = new ArrayList();
        for (int i = 0; i < numCars; i++) {
            cars.add(new TrainCar(world, i));
        }
        cars.get(0).setColor(Color.BLUE);
        this.tracks = tracks;
        speed = 2f;
        setGap(10);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setGap(float gap) {
        distance = (int) (gap / speed);
    }

    public void setSize(float w, float h) {
        for (TrainCar tc : cars) {
            tc.setSize(w, h);
        }
    }

    public void update() {
        if (track == null) {
            track = tracks.randomTrack().getPoints(speed);
            headIndex = 0;
        }
        for (TrainCar tc : cars) {
            tc.update();
        }
        headIndex++;
        if (headIndex >= track.length() + (cars.size() - 1) * (distance)) {
            track = null;
        }
    }

    public void worldRender(Graphics2D g2d) {
        Graphics2D gen = (Graphics2D) g2d.create();
        for (int i = cars.size() - 1; i >= 0; i--) {
            cars.get(i).worldRender(gen);
        }
    }

    public void screenRender(Graphics2D g2d) {
    }

    public void setWorld(World world) {
        for (TrainCar tc : cars) {
            tc.setWorld(world);
        }
    }

    public boolean isComplete() {
        return false;
    }

    public void onComplete() {
    }

    public List<? extends Collidable> collidables() {
        return cars;
    }

    private class TrainCar extends HoboEntity{

        private final int index;

        private TrainCar(World world, int index) {
            super(world);
            this.index = index;
        }

        public void update() {
            int ind = headIndex - index * distance;
            if (ind >= 0 && ind < track.length()) {
                setLoc(track.getPoint(ind));
                rot = track.getAngle(ind);
            }
        }
    }
}
