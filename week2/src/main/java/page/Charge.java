package page;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Charge extends Page {

    private final static String CHARGE_TITLE = "코인을 충전하시고 알쓸오운을 더 많이 즐겨보세요 💰";
    private final static int PAID_OPTION = 1;
    private final static int FREE_OPTION = 2;

    List<String> chargeOptions;

    public Charge() {
        chargeOptions = new ArrayList<>();
        chargeOptions.add("유료 코인 충전하기");
        chargeOptions.add("광고 시청하고 무료 코인 얻기");
    }

    public void startCharge(Scanner scan, User user) {
        System.out.println("올바른 번호를 입력해주세요!");
    }

    public void start(Scanner scan, User user) {
        System.out.println(CHARGE_TITLE+"\n");
        Charge charge = switch (receiveUserInput(chargeOptions, scan)) {
            case PAID_OPTION -> new PaidCharge();
            case FREE_OPTION -> new FreeCharge();
            default -> new Charge();
        };
        System.out.println(LINE);

        charge.startCharge(scan, user);

        System.out.println("\n정상적으로 충전이 완료되었습니다!");
        printRemainCoin(user);
    }
}
