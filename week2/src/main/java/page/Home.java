package page;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Home extends Page {
    int startNum;
    List<String> startOptions;

    public Home() {
        startOptions = new ArrayList<>();
        startOptions.add("시작하기");
        startOptions.add("이용권 구매하기");
        startOptions.add("종료하기");
    }

    public void printTitle(){
        System.out.println("＊*•̩̩͙✩•̩̩͙*＊＊*•̩̩͙✩•̩̩͙*＊＊*•̩̩͙✩•̩̩͙*＊＊*•̩̩͙✩•̩̩͙*＊＊*•̩̩͙✩•̩̩͙*＊\n");
        System.out.println("🔮 알아봐도 쓸데없는 오늘의 운세 🔮");
        System.out.println("오늘 나의 운세를 점쳐보세요!\n");
    }

    public String receiveUserName(Scanner scan) {
        System.out.print("이름을 입력해주세요: ");
        String name = scan.nextLine();
        System.out.println("안녕하세요! "+name+"님 :)\n\n");
        return name;
    }

    //return 값이 false 인 경우 종료
    public boolean start(Scanner scan, User user) {
        printTitle();
        printRemainCoin(user);
        
        startNum = receiveUserInput(startOptions, scan);
        System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");

        if (startNum == 1) {
            if (user.getRemainCoin() < 1) {
                System.out.println("이용권이 부족합니다!\n이용권 구입 후 다시 이용해주세요.");
                System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");
            }
            else {
                Fortune fortune = new Fortune();
                fortune.start(scan, user);
                return isContinued(scan);
            }
        }
        else if (startNum == 2) {
            Charge charge = new Charge();
            charge.start(scan, user);
            return isContinued(scan);
        }
        else if (startNum == 3) {
            return false;
        }
        else {
            System.out.println("올바른 번호를 입력해주세요!");
        }
        return true;
    }
}
