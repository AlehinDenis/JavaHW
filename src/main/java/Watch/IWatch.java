package Watch;
import Exceptions.*;

public interface IWatch {
    void addAlarmClock(int hours, int minutes);
    void turnOffAlarmClock(int number);

    void setHours(int hours) throws WrongInput;
    void setMinutes(int minutes) throws WrongInput;
    void setSeconds(int seconds) throws Unsupported, WrongInput;
    int getHours();
    int getMinutes();
    int getSeconds() throws Unsupported;

}
