package Watch;
import Exceptions.*;

public class WatchHMS extends WatchHM implements IWatch{
    int seconds;

    public WatchHMS() {
        super();
        seconds = 0;
    }

    public WatchHMS(int price, String brand) {
        super(price, brand);
    }

    public WatchHMS(int price, String brand, int hours, int minutes, int seconds) {
        super(price, brand, hours, minutes);
        this.seconds = seconds;
    }
    public WatchHMS(int hours, int minutes, int seconds) {
        super(hours, minutes);
        this.seconds = seconds;
    }

    public void setSeconds(int seconds) throws WrongInput{
        if(seconds > 59 || seconds < 0) {
            throw new WrongInput("Seconds is out of range!");
        }
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void timeForward(int hours, int minutes, int seconds) {
        try {
            if(this.seconds + seconds > 59) {
                setSeconds(this.seconds + seconds - 60);
                super.timeForward(hours, minutes + 1);
            } else {
                setSeconds(this.seconds + seconds);
                super.timeForward(hours, minutes);
            }
        } catch (WrongInput wrongInput) {
            wrongInput.printStackTrace();
        }
    }

    public void print() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return super.toString() + ":" + seconds;
    }
}
