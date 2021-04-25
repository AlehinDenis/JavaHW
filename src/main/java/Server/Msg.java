package Server;

import AlarmClock.AlarmClock;

import java.util.ArrayList;
import java.util.Vector;

public class Msg {
    private String command;
    private String alarmHour;
    private String alarmMin;
    private String time;
    private Vector<AlarmClock> alarms;

    public Msg(String command, String alarmHour, String alarmMin) {
        this.command = command;
        this.alarmHour = alarmHour;
        this.alarmMin = alarmMin;
    }

    public Msg(String command) {
        this.command = command;
    }

    public Msg(String command, String time, Vector<AlarmClock> alarms) {
        this.command = command;
        this.time = time;
        this.alarms = alarms;
    }

    public Vector<AlarmClock> getAlarms() {
        return alarms;
    }

    public String getTime() {
        return time;
    }

    public String getCommand() {
        return command;
    }

    public String getAlarmHour() {
        return alarmHour;
    }

    public String getAlarmMin() {
        return alarmMin;
    }
}
