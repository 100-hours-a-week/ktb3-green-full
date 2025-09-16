package page;
import user.User;

import java.util.List;
import java.util.Scanner;

public abstract class Page {

    public void printRemainCoin(User user) {
        System.out.println(user.getName()+"님이 보유하신 이용권은 "+user.getRemainCoin()+"매입니다!\n");
    }

    public boolean isContinued (Scanner scan) {
        System.out.println("1) 메인으로 돌아가기 2) 종료하기");
        System.out.print("입력: ");

        if (scan.nextInt() == 1) {
            System.out.println("\n");
            return true;
        }
        else {
            System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");
            return false;
        }
    }

    public int receiveUserInput (List<String> options, Scanner scan) {
        for (int i = 0; i < options.size(); i++) {
            System.out.print((i+1)+") "+options.get(i) + " ");
        }
        System.out.print("\n");
        System.out.print("입력: ");
        return scan.nextInt();
    }

}
