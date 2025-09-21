package page;

public class Advertisement implements Runnable {
    int adSec;

    public Advertisement(int adSec) {
        this.adSec = adSec;
    }

    public void run() {
        System.out.println("광고가 재생 됩니다. 5초 뒤부터 건너뛰기 가능합니다.\n");
        while (!FreeCharge.isSkip && adSec > 0) {
            System.out.println("매일 바쁘지? 24시간 하고 싶을 때 하는 1:1영어회화 (튜터링) ..." + adSec);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("광고 시청 중단");
            }
            adSec--;
        }
        if (adSec == 0) {
            FreeCharge.endAd = true;
            System.out.println("\n광고 시청이 종료되었습니다. 엔터를 누르시면 광고창이 종료됩니다.");
        }
    }

}
