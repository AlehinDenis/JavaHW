package Server;

import AlarmClock.AlarmClock;
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
    Socket cs;
    DataInputStream is;
    DataOutputStream os;
    Msg msg;
    WatchHmsManager watch;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WCS(Socket cs, WatchHmsManager watch) {
        this.cs = cs;
        this.watch = watch;
        try {
            os = new DataOutputStream(cs.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        watch.subscribe(this);
        this.start();
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(cs.getInputStream());

            while (true){
                String obj = is.readUTF();
                msg = gson.fromJson(obj, Msg.class);
                if(msg.getCommand().equals("alarm")) {
                    watch.addAlarmClock(Integer.parseInt(msg.getAlarmHour()),
                            Integer.parseInt(msg.getAlarmMin()));
                } else if(msg.getCommand().equals("start")) {
                    watch.start();
                } else if(msg.getCommand().equals("pause")) {
                    watch.pause();
                } else if(msg.getCommand().equals("resume")) {
                    watch.resume();
                }

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void event(IWatch w) {
        Vector<AlarmClock> alarms = watch.getAlarms();
        for(int i = 0; i < alarms.size(); i++) {
            if(alarms.get(i).isTriggered(w.getHours(), w.getMinutes())) {
                Msg msg = new Msg("alarm");
                try {
                    os.writeUTF(gson.toJson(msg, Msg.class));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        try {
            Msg msg = new Msg("time",
                    w.getHours() + ":" + w.getMinutes() + ":" + w.getSeconds(),
                    watch.getAlarms());
            os.writeUTF(gson.toJson(msg, Msg.class));
        } catch (Unsupported unsupported) {
            unsupported.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
