package user;

public class User {
    private final String name;
    private int remainCoin;

    public User(String name) {
        this.name = name;
        this.remainCoin = 1; //기본 이용 횟수
    }

    public String getName() {
        return this.name;
    }

    //getter
    public int getRemainCoin() {
        return this.remainCoin;
    }

    //코인 충전 setter
    public void chargeCoin(int chargedCoin) {
        this.remainCoin += chargedCoin;
    }

    //코인 차감 setter
    public void useCoin() {
        this.remainCoin -= 1;
        if (remainCoin <= 0) {
            System.out.println("보유하신 코인을 모두 소진하셨습니다.");
            System.out.println("알쓸오운을 계속 이용하고 싶으시다면 코인을 충전해주세요 💰");
            System.out.println("광고를 시청하시면 무료로 1코인을 얻으실 수 있습니다.");
        }
    }

}

