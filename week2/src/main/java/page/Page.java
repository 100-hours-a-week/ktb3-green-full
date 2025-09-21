package page;
import user.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public abstract class Page {

    private final static int MAIN = 1;
    private final static int EXIT = 2;
    public final static String LINE = "\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n";
    private final static String INPUT_WARNING = "\n[⚠️ 올바른 범위의 숫자값을 입력해주세요 ⚠️]\n";

    public void printRemainCoin(User user) {
        System.out.println(user.getName()+"님이 보유하신 이용권은 "+user.getRemainCoin()+"매입니다!\n");
    }

    public boolean isContinued (Scanner scan) {

        while (true) {
            System.out.println("1) 메인으로 돌아가기 2) 종료하기");
            System.out.print("입력: ");
            try {
                int input = scan.nextInt();
                if (input == MAIN) {
                    System.out.println("\n");
                    return true;
                }
                else if (input == EXIT) {
                    System.out.println(LINE);
                    return false;
                }
                else {
                    System.out.println(INPUT_WARNING);
                }
            } catch (InputMismatchException e) {
                System.out.println(INPUT_WARNING);
                scan.nextLine();
            }
        }

    }

    public int receiveUserInput (List<String> options, Scanner scan) {

        int input;

        while (true) {
            for (int i = 0; i < options.size(); i++) {
                System.out.print((i+1)+") "+options.get(i) + "\n");
            }
            System.out.print("\n입력: ");
            try {
                input = scan.nextInt();
                if (input < 1 || input > options.size()) {
                    System.out.println(INPUT_WARNING);
                }
                else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println(INPUT_WARNING);
                scan.nextLine();
            }
        }

        return input;
    }

}
