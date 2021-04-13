package Watch;
import Exceptions.*;
import AlarmClock.*;
import WatchManager.WatchEvent;
import WatchManager.WatchManager;

import java.util.Vector;

public class WatchHM implements IWatch{
    private int hours;
    private int minutes;
    private int price;
    private String brand;
    private WatchManager events = new WatchManager();
    private Vector<AlarmClock> alarmClocks = new Vector<AlarmClock>();

    public WatchHM() {
        int hours = 12;
        int minutes = 0;
    }

    public WatchHM(int price, String brand) {
        this.price = price;
        this.brand = brand;
    }

    public WatchHM(int price, String brand, int hours, int minutes) {
        this(price, brand);
        this.hours = hours;
        this.minutes = minutes;
    }
    public WatchHM(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public void subscribe(WatchEvent e) {
        events.subscribe(e);
    }

    public void setHours(int hours) throws WrongInput{
        if(hours > 23 || hours < 0) {
            throw new WrongInput("Hours is out of range!");
        }
        this.hours = hours;
    }
    public void setMinutes(int minutes) throws WrongInput{
        if(minutes > 59 || minutes < 0) {
            throw new WrongInput("Minutes is out of range!");
        }
        this.minutes = minutes;
    }
    public void setSeconds(int seconds) throws Unsupported, WrongInput{
        throw new Unsupported("Seconds aren't supported!");
    }

    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }
    public int getSeconds() throws Unsupported{
        throw new Unsupported("Seconds aren't supported!");
    }

    public void timeForward(int hours, int minutes){
        try {
            if(this.minutes + minutes > 59) {
                setMinutes(this.minutes + minutes - 60);
                setHours(this.hours + 1);
            } else {
                setMinutes(this.minutes + minutes);
            }
            if(this.hours + hours > 23) {
                setHours(this.hours + hours - 24);
            } else {
                setHours(this.hours + hours);
            }
        } catch (WrongInput wrongInput) {
            wrongInput.printStackTrace();
        }
        events.event(this);
    }

    public void addAlarmClock(int hours, int minutes) {
        try {
            alarmClocks.add(new AlarmClock(hours, minutes));
            events.subscribe(alarmClocks.get(alarmClocks.size() - 1));
        } catch (WrongInput wrongInput) {
            wrongInput.printStackTrace();
        }
    }

    public void turnOffAlarmClock(int number) {
        events.unsubscribe(alarmClocks.get(number));
        alarmClocks.remove(number);
    }

    public Vector<AlarmClock> getAlarms() { return alarmClocks; }

    public void print() {
        System.out.println(toString());
    }



    @Override
    public String toString() {
        return hours + ":" + minutes;
    }
}

