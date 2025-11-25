package Project.Game;

public class GameManager {

    private static int money = 0;
    private static int score = 0;

    public static int getMoney() {
        return money;
    }

    public static void addMoney(int amount) {
        money += amount;
        System.out.println("Dinheiro adicionado, agora é " + money);
    }

    public static void spendMoney(int amount) {
        if ((money - amount) >= 0) {
            money -= amount;
            System.out.println("Dinheiro gasto, agora é " + money);
        }
    }

    public static void setMoney(int money) {
        GameManager.money = money;
    }

    public static int getScore() {
        return score;
    }

    public static void addScore() {
        score += 50;
    }
}
