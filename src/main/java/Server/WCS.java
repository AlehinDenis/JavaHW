package Server;

import AlarmClock.AlarmClock;
import DB.Database;
import Exceptions.Unsupported;
import Watch.IWatch;
import Watch.WatchHmsManager;
import WatchManager.WatchEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class WCS extends Thread implements WatchEvent {
    static int countWCS = 0;
    static int currentAlarm = -1;
    Socket cs;
    DataInputStream is;
    DataOutputStream os;
    Msg msg;
    WatchHmsManager watch;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Database db;

    public WCS(Socket cs, WatchHmsManager watch) {
        countWCS++;
        db = new Database();
        db.connect();
        this.cs = cs;
        this.watch = watch;
        try {
            os = new DataOutputStream(cs.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        watch.subscribe(this);
        this.start();
        Msg msg = new Msg("time",
                12 + ":" + 0 + ":" + 0,
                db.getAlarms());
        try {
            os.writeUTF(gson.toJson(msg, Msg.class));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(cs.getInputStream());

            while (true){
                String obj = is.readUTF();
                msg = gson.fromJson(obj, Msg.class);
                if(msg.getCommand().equals("alarm")) {
                    db.addAlarm(
                            msg.getAlarmHour(),
                            msg.getAlarmMin());
                } else if(msg.getCommand().equals("start")) {
                    watch.start();
                } else if(msg.getCommand().equals("pause")) {
                    watch.pause();
                } else if(msg.getCommand().equals("resume")) {
                    watch.resume();
                } else if(msg.getCommand().equals("delete")) {
                    db.deleteAlarm(msg.getAlarmHour(), msg.getAlarmMin());
                }
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void event(IWatch w) {
        Vector<AlarmClock> alarms = db.getAlarms();
        for(int i = 0; i < alarms.size(); i++) {
            if(alarms.get(i).isTriggered(w.getHours(), w.getMinutes())) {
                try {
                    if(currentAlarm == -1) {
                        currentAlarm = countWCS;
                    }
                    if(currentAlarm == 1) {
                        db.deleteAlarm(Integer.toString(w.getHours()),
                                Integer.toString(w.getMinutes()));
                        currentAlarm = -1;
                    } else {
                        currentAlarm--;
                    }

                    Msg msg = new Msg("alarm");
                    os.writeUTF(gson.toJson(msg, Msg.class));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        try {
            Msg msg = new Msg("time",
                    w.getHours() + ":" + w.getMinutes() + ":" + w.getSeconds(),
                    db.getAlarms());
            os.writeUTF(gson.toJson(msg, Msg.class));
        } catch (Unsupported unsupported) {
            unsupported.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
