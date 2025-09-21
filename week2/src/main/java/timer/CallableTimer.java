package timer;

import java.util.concurrent.Callable;

public class CallableTimer implements Callable<Boolean> {
    private int sec;
    public CallableTimer(int sec) {
        this.sec = sec;
    }
    @Override
    public Boolean call() {
        while (sec > 0) {
            sec--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
