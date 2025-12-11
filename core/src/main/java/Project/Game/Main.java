package Project.Game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

// Project/Game/Main.java

public class Main extends ApplicationAdapter {

    private GameScreen game;

    @Override
    public void create() {
        game = new GameScreen();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        float dt = Gdx.graphics.getDeltaTime();

        // CORREÇÃO: Chamada para a lógica
        game.update(dt);

        // Chamada para o desenho
        game.render(dt);
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
