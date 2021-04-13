package Watch;

public class WatchHmsManager extends WatchHMS implements IWatchRunnable{
    Thread thread = null;
    boolean pause = false;

    public WatchHmsManager(int hours, int minutes, int seconds) {
        super(hours, minutes, seconds);
    }

    public void start() {
        if(thread == null) {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while(true){
                            for(int i = 0 ; i < 10; i++) {
                                Thread.sleep(100);
                                if(pause) {
                                    synchronized (thread) {
                                        thread.wait();
                                    }
                                    pause = false;
                                }
                            }
                            timeForward(0, 0, 1);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    public void pause() {
        pause = true;
    }

    public void resume() {
        synchronized (thread) {
            thread.notifyAll();
        }
    }
}
