package DB;

import AlarmClock.AlarmClock;
import Exceptions.WrongInput;

import java.sql.*;
import java.util.Vector;

public class Database {
    Connection connection;
    public void connect() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/Alarms";
            String username = "root";
            String password = "password";
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Vector<AlarmClock> getAlarms() {
        Vector<AlarmClock> alarms = new Vector<AlarmClock>();
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from alarms");
            while(res.next()) {
                alarms.add(new AlarmClock(
                        Integer.parseInt(res.getString("hours")),
                        Integer.parseInt(res.getString("minutes"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (WrongInput wrongInput) {
            wrongInput.printStackTrace();
        }

        return alarms;
    }

    public void addAlarm(String hours, String minutes) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(
                            "INSERT INTO alarms(hours, minutes) VALUES(?,?)");
            preparedStatement.setString(1, hours);
            preparedStatement.setString(2, minutes);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteAlarm(String hours, String minutes) {
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from alarms");
            while(res.next()) {
                if(res.getString("hours").equals(hours) &&
                        res.getString("minutes").equals(minutes)) {
                    String id = res.getString("id");
                    PreparedStatement preparedStatement =
                            connection.prepareStatement(
                                    "DELETE FROM alarms WHERE id = ?");
                    preparedStatement.setString(1, id);
                    preparedStatement.executeUpdate();
                    return;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
