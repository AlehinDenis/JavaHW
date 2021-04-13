package Gui;

import AlarmClock.AlarmClock;
import Exceptions.Unsupported;
import Watch.*;
import WatchManager.WatchEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainForm extends JFrame implements WatchEvent {
    WatchHmManager watchHm = new WatchHmManager(12, 0);
    WatchHmsManager watchHms = new WatchHmsManager(12,0,55);
    private JPanel panelMain;
    private JTable tblAlarms;
    private JButton btnStartWatch;
    private JButton btnResumeWatch;
    private JButton btnPauseWatch;
    private JLabel lblWatchHM;
    private JLabel lblWatchHMS;
    private JLabel lblAlarmsHms;
    private JLabel lblAlarmsHm;
    private JTable tblAlarmsHm;
    private JButton a1MinButton;
    private JButton a1HourButton;
    private JButton a10secButton;
    private JButton a1minButtonHMS;
    private JButton a1hourButtonHMS;
    private JLabel lblMoveClock;
    Thread showMessageThread = null;

    final Integer[] response = {-1};
    public void event(IWatch w) {
        try {
            lblWatchHMS.setText(w.getHours() +
                    ":" + w.getMinutes() + ":" + w.getSeconds());
        } catch (Unsupported unsupported) {
            lblWatchHM.setText(w.getHours() +
                    ":" + w.getMinutes());
        }
        Vector<AlarmClock> alarmsHm = watchHm.getAlarms();
        Vector<AlarmClock> alarmsHms = watchHms.getAlarms();
        UIManager.put("OptionPane.okButtonText", "Выключить");
        for(int i = 0; i < alarmsHm.size(); i++) {
            if(alarmsHm.get(i).isTriggered(w.getHours(),w.getMinutes())) {
                if(showMessageThread == null) {
                    showMessageThread = new Thread(new Runnable() {
                        public void run() {
                            response[0] = JOptionPane.showConfirmDialog(null,
                                    "Будильник сработал", "Будильник", JOptionPane.DEFAULT_OPTION);
                        }
                    });
                    showMessageThread.start();
                    alarmsHms.get(i).turnOffAlarm();
                }
            }
        }

        for(int i = 0; i < alarmsHms.size(); i++) {
            if(alarmsHms.get(i).isTriggered(w.getHours(),w.getMinutes())) {
                if(showMessageThread == null) {
                    showMessageThread = new Thread(new Runnable() {
                        public void run() {
                            response[0] = JOptionPane.showConfirmDialog(null,
                                    "Будильник сработал", "Будильник", JOptionPane.DEFAULT_OPTION);
                        }
                    });
                    showMessageThread.start();
                    alarmsHms.get(i).turnOffAlarm();
                }
            }
        }
    }

    {
        lblWatchHM.setText(watchHm.toString());
        lblWatchHMS.setText(watchHms.toString());
        lblWatchHM.setFont(new Font("Serif", Font.BOLD, 30));
        lblWatchHMS.setFont(new Font("Serif", Font.BOLD, 30));
        lblAlarmsHm.setFont(new Font("Serif", Font.BOLD, 25));
        lblAlarmsHms.setFont(new Font("Serif", Font.BOLD, 25));
        lblMoveClock.setFont(new Font("Serif", Font.BOLD, 30));
    }

    public void initialize() {
        watchHm.subscribe(this);
        watchHms.subscribe(this);
        watchHm.addAlarmClock(7, 40);
        watchHm.addAlarmClock(7, 50);
        watchHm.addAlarmClock(8, 0);
        watchHm.addAlarmClock(8, 30);
        watchHms.addAlarmClock(12, 1);
        watchHms.addAlarmClock(12, 2);
        watchHms.addAlarmClock(12, 30);
        watchHms.addAlarmClock(13, 0);

        Vector<AlarmClock> alarms = watchHm.getAlarms();
        DefaultTableModel modelHms = new DefaultTableModel();
        String columns[]={"Alarms"};
        modelHms.setColumnIdentifiers(columns);
        Object[] row = new Object[1];
        for(int i = 0; i < alarms.size(); i++) {
            row[0] = alarms.get(i).getHours() + ":";
            if(alarms.get(i).getMinutes() < 10) {
                row[0] += "0" + alarms.get(i).getMinutes();
            } else {
                row[0] += "" + alarms.get(i).getMinutes();
            }

            modelHms.addRow(row);
        }

        tblAlarmsHm.setRowHeight(40);
        tblAlarmsHm.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tblAlarmsHm.setFont(new Font("Serif", Font.PLAIN, 20));
        tblAlarmsHm.setModel(modelHms);


        DefaultTableModel modelHm = new DefaultTableModel();
        modelHm.setColumnIdentifiers(columns);
        alarms = watchHms.getAlarms();
        for(int i = 0; i < alarms.size(); i++) {
            row[0] = alarms.get(i).getHours() + ":";
            if(alarms.get(i).getMinutes() < 10) {
                row[0] += "0" + alarms.get(i).getMinutes();
            } else {
                row[0] += "" + alarms.get(i).getMinutes();
            }

            modelHm.addRow(row);
        }

        tblAlarms.setRowHeight(40);
        tblAlarms.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tblAlarms.setFont(new Font("Serif", Font.PLAIN, 20));
        tblAlarms.setModel(modelHm);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblAlarms.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblAlarmsHm.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    }

    public MainForm() {
        initialize();
        btnStartWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHm.start();
                watchHms.start();
            }
        });
        btnPauseWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHm.pause();
                watchHms.pause();
            }
        });
        btnResumeWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHm.resume();
                watchHms.resume();
            }
        });
        a1MinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHm.timeForward(0,1);
            }
        });
        a1HourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHm.timeForward(1,0);
            }
        });
        a10secButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHms.timeForward(0,0,10);
            }
        });
        a1minButtonHMS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHms.timeForward(0,1,0);
            }
        });
        a1hourButtonHMS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                watchHms.timeForward(1,0,0);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Watch");
        frame.setContentPane(new MainForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000,600);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
