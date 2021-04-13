package Watch;

import Watch.WatchHM;

public class WatchHmManager extends WatchHM implements IWatchRunnable{
    Thread thread = null;
    boolean pause = false;

    public WatchHmManager(int hours, int minutes) {
        super(hours, minutes);
    }

    public void start() {
        if(thread == null) {
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while(true){
                            for(int i = 0 ; i < 600; i++) {
                                Thread.sleep(100);
                                if(pause) {
                                    synchronized (thread) {
                                        thread.wait();
                                    }
                                }
                                pause = false;
                            }
                            timeForward(0, 1);
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
