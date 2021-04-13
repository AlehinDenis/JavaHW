package Watch;

public class BWatch {
    public static IWatch buildHM(int hours, int minutes) {
        return new WatchHM(hours, minutes);
    }

    public static IWatch buildHMS(int hours, int minutes, int seconds) {
        return new WatchHMS(hours, minutes, seconds);
    }

}
