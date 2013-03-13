package hobogame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HoboWorld implements World {

    private final List<Entity> add;
    private final List<Entity> rem;
    final List<Entity> entities;
    private final HoboFrame display;
    private final HoboPane pane;
    private final Thread[] threads;
    private final Mouse mouse;
    private final ButtonInput keyboard;
    private boolean running;
    private boolean pause;
    protected Discreet2D view;
    protected float scale;
    private int hw;
    private int hh;

    HoboWorld() {
        this(500, 500);
    }

    HoboWorld(int w, int h) {
        hw = w >> 1;
        hh = h >> 1;
        running = false;
        pause = false;
        keyboard = new ButtonInput(512);
        mouse = new Mouse(3);
        add = new ArrayList();
        rem = new ArrayList();
        entities = new ArrayList();
        pane = new HoboPane(this);
        display = new HoboFrame(w, h, keyboard, mouse, pane);
        view = new IntCoord(hw, hh);
        scale = 1;
        /*
         * === Threaded processes ===
         * Update every 10ms
         * Render every 10ms
         * Request focus every 10ms
         */
        threads = new Thread[3];
        threads[0] = new RepeatingThread(10) {

            void innerRun() {
                update();
            }
        };
        threads[1] = new RepeatingThread(15) {

            void innerRun() {
                pane.repaint();
            }
        };
        threads[2] = new RepeatingThread(100) {

            public void innerRun() {
                pane.requestFocus();
            }
        };
    }

    public void add(Entity ent) {
        if (running) {
            synchronized (entities) {
                add.add(ent);
            }
        } else {
            entities.add(ent);
        }
    }

    public void start() {
        display.setVisible(true);
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        running = true;
    }

    public void end() {
        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
        System.exit(0);
    }

    public int width() {
        return hw << 1;
    }

    public int height() {
        return hh << 1;
    }

    public void setView(Discreet2D view) {
        this.view = view;
    }

    void update() {
        try {
            if (!pause) {
                for (Entity en : entities) {
                    en.update();
                    if (en.isComplete()) {
                        rem.add(en);
                        en.onComplete();
                    }
                }
                if (!add.isEmpty() || !rem.isEmpty()) {
                    //synchronize when modifying the list
                    synchronized (entities) {
                        if (!add.isEmpty()) {
                            entities.addAll(add);
                            add.clear();
                        }
                        if (!rem.isEmpty()) {
                            entities.removeAll(rem);
                            rem.clear();
                        }
                    }
                }
                if (keyboard.buttonDown(KeyEvent.VK_D)) {
                    view = view.add(2, 0);
                }
                if (keyboard.buttonDown(KeyEvent.VK_A)) {
                    view = view.add(-2, 0);
                }
                if (keyboard.buttonDown(KeyEvent.VK_S)) {
                    view = view.add(0, 2);
                }
                if (keyboard.buttonDown(KeyEvent.VK_W)) {
                    view = view.add(0, -2);
                }
                if (keyboard.buttonDown(KeyEvent.VK_E)) {
                    scale = Math.min(scale * 1.01f, 10);
                }
                if (keyboard.buttonDown(KeyEvent.VK_Q)) {
                    scale = Math.max(scale * (1 / 1.01f), 0.1f);
                }
                if (keyboard.buttonDown(KeyEvent.VK_ESCAPE)) {
                    end();
                }
                if (keyboard.buttonClaim(KeyEvent.VK_B)) {
                    HoboEntity.SHOW_BOUNDING = !HoboEntity.SHOW_BOUNDING;
                }
                if (keyboard.buttonClaim(KeyEvent.VK_V)) {
                    HoboEntity.SHOW_COL_AXES = !HoboEntity.SHOW_COL_AXES;
                }
            }
            if (keyboard.buttonClaim(KeyEvent.VK_P)) {
                pause = !pause;
            }
        } catch (Exception e) {
            System.err.println("Unhandled error: " + e);
            System.err.println(e.getMessage());
            e.printStackTrace();
            end();
        }
    }

    public ButtonInput getKeyboard() {
        return keyboard;
    }

    public Mouse getMouse() {
        return mouse;
    }

    private class HoboFrame extends JFrame implements KeyListener,
            MouseListener, MouseMotionListener {

        HoboFrame(int w, int h, ButtonInput keyboard, Mouse mouse,
                HoboPane pane) {
            super("Hobo game yay!");
            setUndecorated(true);
            setSize(w, h);
            setLocationRelativeTo(null);

            add(pane);

            pane.addKeyListener(this);
            pane.addMouseListener(this);
            pane.addMouseMotionListener(this);
            pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            keyboard.down(e.getKeyCode());
        }

        public void keyReleased(KeyEvent e) {
            keyboard.up(e.getKeyCode());
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            mouse.down(e.getButton());
        }

        public void mouseReleased(MouseEvent e) {
            mouse.up(e.getButton());
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            mouse.setLoc(new IntCoord(e.getPoint()));
        }

        public void mouseMoved(MouseEvent e) {
            mouse.setLoc(new IntCoord(e.getPoint()));
        }
    }

    private class HoboPane extends JPanel {

        /*This world's entity list is also the synch lock*/
        private final HoboWorld world;

        public HoboPane(HoboWorld world) {
            super();
            this.world = world;
        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D gScreen = (Graphics2D) g;
            Graphics2D gWorld = (Graphics2D) g.create();

            gWorld.translate(hw, hh);
            gWorld.scale(scale, scale);
            gWorld.translate(-view.getX(), -view.getY());

            synchronized (world.entities) {
                for (Entity e : world.entities) {
                    e.worldRender(gWorld);
                }
                for (Entity e : world.entities) {
                    e.screenRender(gScreen);
                }
            }

            gWorld.dispose();
        }
    }
}
