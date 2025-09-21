package card;

import java.util.Map;

public class Card {

    private final static String CARD_LINE = "✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰--✰";
    protected int cardNum;
    protected Map<Integer, String> cards;

    public void result() {
        System.out.println("\n"+cardNum+"번 카드가 선택되었습니다!");
        System.out.println("\n"+CARD_LINE);
        System.out.println(cards.get(cardNum));
        System.out.println(CARD_LINE+"\n");
    }
}
