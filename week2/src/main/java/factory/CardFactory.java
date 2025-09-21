package factory;
import card.*;

public class CardFactory {

    private final static int MONEY = 1;
    private final static int LOVE = 2;
    private final static int ACADEMIC = 3;
    private final static int TOTAL = 4;


    public Card getCard (int category, int cardNum) {
        if (category == MONEY) {
            return new MoneyCard(cardNum);
        }
        else if (category == LOVE) {
            return new LoveCard(cardNum);
        }
        else if (category == ACADEMIC) {
            return new AcademicCard(cardNum);
        }
        else if (category == TOTAL) {
            return new TotalCard(cardNum);
        }
        else {
            return null;
        }
    }

}
