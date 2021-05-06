package Gui;

import AlarmClock.AlarmClock;
import Exceptions.WrongInput;
import Server.Msg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

public class MainForm extends JFrame{
    Socket cs;
    DataInputStream is;
    DataOutputStream os;
    int port = 3124;
    InetAddress host;
    Thread t;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Vector<AlarmClock> alarms = new Vector<AlarmClock>();


    private JPanel panelMain;
    private JTable tblAlarms;
    private JButton btnStartWatch;
    private JButton btnResumeWatch;
    private JButton btnPauseWatch;
    private JLabel lblWatchHM;
    private JLabel lblWatchHMS;
    private JLabel lblAlarmsHms;
    private JTextField textField1;
    private JTextField textField2;
    private JButton btnAddAlarm;
    private JLabel lblAddAlrm;

    {
        lblWatchHM.setFont(new Font("Serif", Font.BOLD, 30));
        lblWatchHMS.setFont(new Font("Serif", Font.BOLD, 30));
        lblAlarmsHms.setFont(new Font("Serif", Font.BOLD, 25));
        lblAddAlrm.setFont(new Font("Serif", Font.BOLD, 25));
        lblWatchHM.setText("12:0");
        lblWatchHMS.setText("12:0:0");
    }

    public void initialize() {
        try {
            host = InetAddress.getLocalHost();
            final Socket cs = new Socket(host, port);
            is = new DataInputStream(cs.getInputStream());
            os = new DataOutputStream(cs.getOutputStream());

            t = new Thread() {
                @Override
                public void run() {
                    try {
                        is = new DataInputStream(cs.getInputStream());
                        os = new DataOutputStream(cs.getOutputStream());
                        while(true) {
                            String obj = is.readUTF();
                            Msg msg = gson.fromJson(obj, Msg.class);
                            if(msg.getCommand().equals("time")) {
                                alarms = msg.getAlarms();
                                printAlarms();
                                lblWatchHM.setText(msg.getTime().substring(0, msg.getTime().lastIndexOf(':')));
                                lblWatchHMS.setText(msg.getTime());
                            } else if(msg.getCommand().equals("alarm")) {
                                Thread thread = new Thread(new Runnable() {
                                    public void run() {
                                        JOptionPane.showConfirmDialog(null,
                                                "Будильник сработал", "Будильник", JOptionPane.DEFAULT_OPTION);
                                    }
                                });
                                thread.start();
                            }
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            };
            t.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void printAlarms() {
        String columns[]={"Alarms"};
        Object[] row = new Object[1];
        DefaultTableModel modelHm = new DefaultTableModel();
        modelHm.setColumnIdentifiers(columns);
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
    }


    public MainForm() {
        initialize();
        btnStartWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Msg msg = new Msg("start");
                try {
                    os.writeUTF(gson.toJson(msg));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnPauseWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Msg msg = new Msg("pause");
                try {
                    os.writeUTF(gson.toJson(msg));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnResumeWatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Msg msg = new Msg("resume");
                try {
                    os.writeUTF(gson.toJson(msg));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnAddAlarm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Msg msg = new Msg("alarm",
                        textField1.getText(),
                        textField2.getText());
                try {
                    alarms.add(new AlarmClock(Integer.parseInt(textField1.getText()),
                            Integer.parseInt(textField2.getText())));
                    printAlarms();
                } catch (WrongInput wrongInput) {
                    wrongInput.printStackTrace();
                }
                try {
                    os.writeUTF(gson.toJson(msg));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        tblAlarms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        tblAlarms.addComponentListener(new ComponentAdapter() {
        });
        tblAlarms.addContainerListener(new ContainerAdapter() {
        });
        tblAlarms.addFocusListener(new FocusAdapter() {
        });
        tblAlarms.addMouseListener(new MouseAdapter() {
        });
        tblAlarms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tblAlarms.getSelectedRow();
                TableModel model = tblAlarms.getModel();
                String alarm = (String) model.getValueAt(index, 0);
                String hours = alarm.substring(0, 2);
                String minutes = "";
                if(alarm.charAt(3) == '0') {
                    minutes = alarm.substring(4, 5);
                } else {
                    minutes = alarm.substring(3, 5);
                }

                Msg msg = new Msg("delete", hours, minutes);
                try {
                    os.writeUTF(gson.toJson(msg));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                ((DefaultTableModel)tblAlarms.getModel()).removeRow(index);
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
