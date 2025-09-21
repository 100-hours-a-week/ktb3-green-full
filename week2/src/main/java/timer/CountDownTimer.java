package timer;

public class CountDownTimer implements Runnable {

    private int sec;
    private String purpose;
    public CountDownTimer(int sec, String purpose) {
        this.sec = sec;
        this.purpose = purpose;
    }
    public CountDownTimer(int sec) {
        this.sec = sec;
        this.purpose = null;
    }
    @Override
    public void run() {
        while(sec > 0) {
            if (purpose != null) {
                System.out.println(this.purpose+"까지 "+this.sec+"초 남았습니다...");
            }
            sec--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
