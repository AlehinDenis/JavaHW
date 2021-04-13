package WatchManager;

import Watch.IWatch;

import java.util.ArrayList;

public class WatchManager {
    ArrayList<WatchEvent> listeners = new ArrayList<WatchEvent>();
    public void  subscribe(WatchEvent listener) {
        listeners.add(listener);
    }
    public void unsubscribe(WatchEvent listener) {
        listeners.remove(listener);
    }

    public void event(IWatch watch) {
        for(WatchEvent listener: listeners) {
            listener.event(watch);
        }
    }
}
