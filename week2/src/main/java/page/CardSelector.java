package page;

import card.*;
import factory.CardFactory;
import timer.CountDownTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CardSelector extends Fortune {

    private final static int CARD_NUM = 8;
    private final static int WAITING_SEC = 3;
    private final int category;
    List<String> selectionOptions;

    public CardSelector(int category) {
        this.category = category;
        selectionOptions = new ArrayList<>();
        selectionOptions.add("랜덤으로 선택하기");
        selectionOptions.add("직접 선택하기");
    }
    public void displayCards() {
        System.out.println("\n-------------------------------------------");
        for (int j = 0; j < CARD_NUM; j++) {
            System.out.printf("%-6s", "|");
        }
        System.out.print("\n");
        for (int j = 0; j < (CARD_NUM - 1); j++) {
            System.out.printf("%-6s", "|  "+(j+1));

        }
        System.out.printf("%-6s", "|  ");
        System.out.print("\n");
        for (int j = 0; j < CARD_NUM; j++) {
            System.out.printf("%-6s", "|");
        }
        System.out.print("\n");
        System.out.println("-------------------------------------------\n");
    }

    public int selectCard(boolean isRandom, Scanner scan) {
        if (isRandom) { //랜덤 선택
            Random random = new Random();
            return random.nextInt(7)+1; //1~7 랜덤 숫자 발생
        }
        else { //수동 선택
            while (true) {
                System.out.println("원하는 카드 번호를 입력해주세요!");
                System.out.print("입력: ");
                int selectNum = scan.nextInt();
                if (selectNum > 0 && selectNum < 8) {
                    System.out.println(LINE);
                    return selectNum;
                }
            }
        }

    }

    public void start(Scanner scan) {
        System.out.println("총 7장의 카드 중 원하시는 카드를 고를 차례예요.\n");
        System.out.println("고르기 어려우시다면 랜덤으로 선택할 수 있는 1번을,\n직접 선택하시고 싶으시다면 2번을 입력해주세요!");
        displayCards();

        int isRandom = receiveUserInput(selectionOptions, scan);
        System.out.println(LINE);
        int cardNum = selectCard(isRandom == 1, scan);

        CardFactory cardFactory = new CardFactory();
        Card selectedCard = cardFactory.getCard(this.category, cardNum);

        Thread countDownTimer = new Thread(new CountDownTimer(WAITING_SEC, "카드공개"));
        countDownTimer.start();
        try {
            countDownTimer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        selectedCard.result();
    }
}
