package hobogame;

import hobogame.Collisions.MultiCollider;
import hobogame.HoboTrack.TrackSystem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

class Hobo extends HoboEntity {

    private TrackSystem system;
    private Track track;
    private int index;
    private int speed;
    private float angle;
    private float health;
    private float maxHealth;
    private int hit;

    public Hobo(World world, TrackSystem system) {
        super(world);
        speed = 2;
        health = maxHealth = 100;
        hit = 0;
        this.system = system;
    }

    public void update() {
        if (track == null) {
            track = system.randomTrack().getPoints(speed);
            index = track.length() >> 1;
        }
        ButtonInput keyboard = world.getKeyboard();
        Coord dir = Coord.ORIGIN;
        if (keyboard.buttonAvailable(KeyEvent.VK_LEFT)) {
            dir = dir.add(-1, 0);
            index = Math.max(0, index - 1);
        }
        if (keyboard.buttonAvailable(KeyEvent.VK_RIGHT)) {
            dir = dir.add(1, 0);
            index = Math.min(index + 1, track.length() - 1);
        }
        if (keyboard.buttonAvailable(KeyEvent.VK_UP)) {
            dir = dir.add(0, -1);
        }
        if (keyboard.buttonAvailable(KeyEvent.VK_DOWN)) {
            dir = dir.add(0, 1);
        }
        loc = loc.add(dir.setLength(keyboard.buttonDown(KeyEvent.VK_SHIFT) ? 1 : speed));
        //loc = track.getPoint(index);
        HoboWorld w = getWorld();
        Coord view = w.view.toCoord();
        float dist = view.distSqr(getLoc());
        //w.view = view.trans(view.angle(getLoc()), dist * 0.001f);
        //w.scale = Math.min(2, Math.max(0.3f, dist * 0.001f));
        //w.setView(loc);

        List<Entity> list = w.entities;
        boolean noHits = true;
        for (Entity en : list) {
            if (en != this) {
                if (Collisions.collideSemi((MultiCollider) en, this)) {
                    noHits = false;
                    if (hit == 0) {
                        health -= 10f;
                    } else {
                        health -= 0.5f;
                    }
                    hit++;
                }
            }
        }
        if (noHits && hit > 0) {
            hit = 0;
        }
    }

    public boolean isComplete() {
        return health <= 0;
    }

    public void onComplete() {
        HoboFlash flash = HoboFlash.screenFlash(getWorld(), Color.WHITE);
        getWorld().add(flash);

        HoboExplosion exp = new HoboExplosion(getWorld(), 500,
                HoboAnimation.GRAPHIC_WORLD);
        exp.setLoc(loc);
        getWorld().add(exp);

        HoboStringDisplay message = new HoboStringDisplay(getWorld(), 2000,
                HoboAnimation.GRAPHIC_SCREEN, "You died, chump!");
        message.setLoc(new IntCoord(20, 20));
        getWorld().add(message);
    }

    public void screenRender(Graphics2D g2d) {
        HoboWorld hw = getWorld();

        g2d.setColor(Color.RED);
        g2d.fillRect(2, hw.height() - 22, 100, 20);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(2, hw.height() - 22, Math.round(100 * health / maxHealth), 20);
    }

    public void worldRender(Graphics2D g2d) {
        super.worldRender(g2d);
        if (hit > 0) {
            int hit = this.hit * 2;
            g2d.setColor(new Color(255, 0, 0, 126));
            g2d.fillOval(loc.getX() - (hit >> 1), loc.getY() - (hit >> 1),
                    hit, hit);
        }
    }
}
