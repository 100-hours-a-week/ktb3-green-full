package page;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Charge extends Page {

    static String chargeTitle = "충전하실 코인의 개수에 맞게 번호를 입력해주세요!";
    int chargeNum;
    List<String> chargeOptions;
    public Charge() {
        chargeOptions = new ArrayList<>();
        chargeOptions.add("1 코인 = 500원");
        chargeOptions.add("3 코인 = 1450원");
        chargeOptions.add("5 코인 = 2300원");
        chargeOptions.add("10 코인 = 4500원");
    }

    @Override
    public int receiveUserInput (List<String> options, Scanner scan) {
        for (int i = 0; i < options.size(); i++) {
            System.out.print((i+1)+") "+options.get(i) + "\n");
        }

        System.out.print("입력: ");
        return scan.nextInt();
    }

    private void chargeCoin(int chargeNum, User user) {
        int change;
        switch (chargeNum) {
            case 1: change = 1; break;
            case 2: change = 3; break;
            case 3: change = 5; break;
            case 4: change = 10; break;
            default: change = 0; break;
        }
        user.setRemain(change);
        System.out.println("정상적으로 충전이 완료되었습니다!");
        printRemainCoin(user);
    }

    public void start(Scanner scan, User user) {
        System.out.println(chargeTitle);
        System.out.print("\n");
        chargeNum = receiveUserInput(chargeOptions, scan);
        System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");
        chargeCoin(chargeNum, user);
    }
}
