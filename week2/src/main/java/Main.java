import java.util.Scanner;

import user.User;
import page.Home;


public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        Home home = new Home();

        String name = home.receiveUserName(scan);
        User user = new User(name);

        while(true) {
            if (!home.start(scan, user)) break;
        }

        System.out.println("알쓸오운을 이용해주셔서 감사합니다! 🔮");
        System.out.println("운세는 운세일 뿐 :) 오늘도 기분 좋은 하루 보내세요 🔥");

        scan.close();
    }
}
