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
    public int getRemain() {
        return this.remainCoin;
    }

    public void setRemain(int add) {
        if (this.remainCoin + add < 0) {
            this.remainCoin = 0;
        }
        else {
            this.remainCoin = this.remainCoin + add;
        }

    }
}

