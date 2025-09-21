package page;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PaidCharge extends Charge {
    private final static String PAID_TITLE = "충전하실 코인의 개수에 맞게 번호를 입력해주세요!";
    List<String> chargeOptions;
    List<Integer> coinPrice;
    List<Integer> coinNum;
    public PaidCharge() {
        chargeOptions = new ArrayList<>();
        chargeOptions.add("1 코인 = 500원");
        chargeOptions.add("3 코인 = 1450원");
        chargeOptions.add("5 코인 = 2300원");
        chargeOptions.add("10 코인 = 4500원");
        coinPrice = new ArrayList<>();
        coinPrice.add(500);
        coinPrice.add(1450);
        coinPrice.add(2300);
        coinPrice.add(4500);
        coinNum = new ArrayList<>();
        coinNum.add(1);
        coinNum.add(3);
        coinNum.add(5);
        coinNum.add(10);
    }

    private void insertMoney(int num) {
        System.out.println("\n"+coinPrice.get(num-1)+"원을 입금해주세요!");
    }


    @Override
    public void startCharge(Scanner scan, User user) {
        System.out.println(PAID_TITLE+"\n");
        int num = receiveUserInput(chargeOptions, scan);
        insertMoney(num);
        user.chargeCoin(coinNum.get(num-1));
    }
}
