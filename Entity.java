package hobogame;

import hobogame.Collisions.Collidable;
import hobogame.Collisions.MultiCollider;
import java.awt.Graphics2D;
import java.util.List;

interface Entity extends MultiCollider {

    public void update();

    public void worldRender(Graphics2D g2d);

    public void screenRender(Graphics2D g2d);

    public void setWorld(World world);

    public boolean isComplete();

    public void onComplete();

    public List<? extends Collidable> collidables();
}
