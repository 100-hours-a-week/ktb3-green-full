package page;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fortune extends Page {

    static String fortuneTitle = "확인하고 싶으신 운세의 종류를 선택해주세요!";
    int categoryNum;
    List<String> categoryOptions;
    public Fortune() {
        categoryOptions = new ArrayList<>();
        categoryOptions.add("금전운💰");
        categoryOptions.add("연애운💘");
        categoryOptions.add("학업운📝");
        categoryOptions.add("총운⭐️");
    }

    void subtractCoin(User user) {
        user.setRemain(-1);
    }

    public void start(Scanner scan, User user) {
        System.out.println(fortuneTitle);
        System.out.print("\n");
        categoryNum = receiveUserInput(categoryOptions, scan);
        System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");

        CardSelector cardSelector = new CardSelector(categoryNum);
        cardSelector.start(scan);

        subtractCoin(user); //이용권 1 차감
    }

}
