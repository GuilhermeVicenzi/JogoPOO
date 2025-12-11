package Project.Game;

import com.badlogic.gdx.utils.Timer;
import java.util.function.Consumer;
import java.util.function.BiConsumer;


public class GameManager {

    private static int money = 0;
    private static int score = 0;
    private static BiConsumer<Float, Float> flySpawnCallback;
    private static float timer = 0f;
    private static int minutes = 0;
    private static int seconds = 0;

    public static void setFlySpawnCallback(BiConsumer<Float, Float> callback) {
        flySpawnCallback = callback;
    }


    public static void spawnFly(float x, float y) {
        if (flySpawnCallback != null) {
            // Executa a função registrada pelo GameScreen
            flySpawnCallback.accept(x, y);
        } else {
            System.err.println("Erro: Callback de spawn de mosca não foi registrado!");
        }
    }

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

    public static void addScore(int amount) {
        score += amount;
    }

    public static void handleTimer(float delta) {
        timer += delta;

        minutes = (int) (timer / 60);
        seconds = (int) (timer % 60);
    }

    // Adicione os getters para acessar o tempo fora da classe
    public static int getMinutes() {
        return minutes;
    }

    public static int getSeconds() {
        return seconds;
    }

    public static float getTimer() {
        return timer;
    }

    // 🛑 MÉTODO RESET ADICIONADO
    public static void reset() {
        money = 0;
        score = 0;
        timer = 0f;
        minutes = 0;
        seconds = 0;
        // Não reseta o flySpawnCallback, pois ele deve ser redefinido pelo GameScreen.create() se necessário
        System.out.println("GameManager resetado.");
    }
}
