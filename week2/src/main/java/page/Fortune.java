package page;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fortune extends Page {

    private final static String FORTUNE_TITLE = "확인하고 싶으신 운세의 종류를 선택해주세요!";
    List<String> categoryOptions;
    public Fortune() {
        categoryOptions = new ArrayList<>();
        categoryOptions.add("금전운💰");
        categoryOptions.add("연애운💘");
        categoryOptions.add("학업운📝");
        categoryOptions.add("총운⭐️");
    }

    public void start(Scanner scan, User user) {
        System.out.println(FORTUNE_TITLE+"\n");
        int categoryNum = receiveUserInput(categoryOptions, scan);
        System.out.println(LINE);

        CardSelector cardSelector = new CardSelector(categoryNum);
        cardSelector.start(scan);

        user.useCoin();
    }

}
