package factory;
import card.*;

public class CardFactory {

    public Card getCard (int category, int cardNum) {
        if (category == 1) {
            return new MoneyCard(cardNum);
        }
        else if (category == 2) {
            return new LoveCard(cardNum);
        }
        else if (category == 3) {
            return new AcademicCard(cardNum);
        }
        else if (category == 4) {
            return new TotalCard(cardNum);
        }
        else {
            return null;
        }
    }

}
