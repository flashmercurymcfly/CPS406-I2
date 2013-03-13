package hobogame;

abstract class RepeatingThread extends Thread {

    private final int delay;

    RepeatingThread(int delay) {
        this.delay = delay;
    }

    public void run() {
        while (true) {
            try {
                innerRun();
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    abstract void innerRun();
}
