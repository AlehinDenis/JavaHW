package AlarmClock;

import Exceptions.Unsupported;
import Watch.IWatch;
import WatchManager.*;
import Exceptions.WrongInput;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AlarmClock implements WatchEvent {
    int hours;
    int minutes;
    boolean triggered;

    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }

    public AlarmClock(int hours, int minutes) throws WrongInput{
        if(hours > 23 || hours < 0) {
            throw new WrongInput("Hours is out of range!");
        }
        if(minutes > 59 || minutes < 0) {
            throw new WrongInput("Minutes is out of range!");
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    public boolean isTriggered(int hours, int minutes) {
        if(!triggered && minutes == this.minutes && hours == this.hours) {
            triggered = true;
            return true;
        }
        return false;
    }

    public void event(IWatch w) {
        if(w.getMinutes() == minutes && w.getHours() == hours ) {

        }
    }

    @Override
    public String toString() {
        return "Alarm clock is set to " + hours + ":" + minutes;
    }
}
