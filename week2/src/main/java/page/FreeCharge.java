package page;
import timer.CountDownTimer;
import timer.TestTimer;
import user.User;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


public class FreeCoin extends Charge {

    public static volatile boolean isSkip;
    public static volatile boolean endAd;
    private final static int REQUIRED_ADTIME = 5; //광고 필수 시청 시간

    public FreeCoin() {
        isSkip = false;
        endAd = false;
    }

    public void startCharge(Scanner scan, User user) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> requiredAdTimerThread = new TestTimer(REQUIRED_ADTIME);
        Future<Boolean> endRequiredAdTimer = executorService.submit(requiredAdTimerThread);

        Thread showAdThread = new Thread(new Advertisement());
        showAdThread.start();

        while (true) {
            if (endRequiredAdTimer.isDone()) {
                System.out.println("\n지금부터 광고 스킵이 가능합니다. 스킵하시려면 엔터를 눌러주세요!\n");
                break;
            }
        }

        scan.nextLine();

        while (!endAd) {
            String input = scan.nextLine();
            if (input.isEmpty()) {
                isSkip = true;
                break;
            }
        }

        user.chargeCoin(1);
        System.out.println("1코인이 무료로 지급되었습니다 🔥");
        executorService.shutdown();
    }

}
