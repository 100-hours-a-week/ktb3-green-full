package page;

import card.AcademicCard;
import card.LoveCard;
import card.MoneyCard;
import card.TotalCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CardSelector extends Fortune {

    int category;
    int cardNum;
    List<String> selectionOptions;

    public CardSelector(int category) {
        this.category = category;
        selectionOptions = new ArrayList<>();
        selectionOptions.add("랜덤으로 선택하기");
        selectionOptions.add("직접 선택하기");
    }
    public void displayCards() {
        System.out.println("\n-------------------------------------------");
        for (int j = 0; j < 8; j++) {
            System.out.printf("%-6s", "|");
        }
        System.out.print("\n");
        for (int j = 0; j < 7; j++) {
            System.out.printf("%-6s", "|  "+(j+1));

        }
        System.out.printf("%-6s", "|  ");
        System.out.print("\n");
        for (int j = 0; j < 8; j++) {
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
            System.out.println("원하는 카드 번호를 입력해주세요!");
            while (true) {
                System.out.print("입력: ");
                int selectNum = scan.nextInt();
                System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");
                if (selectNum > 0 && selectNum < 8) return selectNum;
            }

        }

    }

    public void start(Scanner scan) {
        System.out.println("총 7장의 카드 중 원하시는 카드를 고를 차례예요.\n");
        System.out.println("고르기 어려우시다면 랜덤으로 선택할 수 있는 1번을,\n직접 선택하시고 싶으시다면 2번을 입력해주세요!");
        displayCards();
        int isRandom = receiveUserInput(selectionOptions, scan);
        System.out.println("\n＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊\n");
        cardNum = selectCard(isRandom == 1, scan);

        switch (this.category) {
            case 1:
                MoneyCard moneyCard = new MoneyCard(cardNum);
                moneyCard.result();
                break;
            case 2:
                LoveCard loveCard = new LoveCard(cardNum);
                loveCard.result();
                break;
            case 3:
                AcademicCard academicCard = new AcademicCard(cardNum);
                academicCard.result();
                break;
            case 4:
                TotalCard totalCard = new TotalCard(cardNum);
                totalCard.result();
                break;
            default:
                System.out.println("올바른 번호를 입력해주세요!");
                break;
        }
    }
}
